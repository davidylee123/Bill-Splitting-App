package com.cwru.bill_splitting_app.controller;

import com.cwru.bill_splitting_app.model.Expense;
import com.cwru.bill_splitting_app.service.ExpenseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ExpenseControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ExpenseService expenseService;

  @Autowired
  private ObjectMapper objectMapper;

  private ObjectId expense1Id;
  private Expense expense1;
  private ObjectId expense2Id;
  private Expense expense2;

  private ObjectId user1Id;
  private ObjectId user2Id;

  @BeforeEach
  public void setup() {
    expense1 = new Expense();
    expense1Id = new ObjectId();
    expense1.set_id(expense1Id);
    expense1.setTitle("Groceries");
    expense1.setAmount(50.0);
    user1Id = new ObjectId();
    user2Id = new ObjectId();
    expense1.setPaidBy(user1Id);
    expense1.setUsers(Arrays.asList(user1Id, user2Id));

    expense2 = new Expense();
    expense2Id = new ObjectId();
    expense2.set_id(expense2Id);
    expense2.setTitle("Electricity");
    expense2.setAmount(75.5);
    expense2.setPaidBy(user2Id);
    expense2.setUsers(Arrays.asList(user1Id, user2Id));
  }

  @Test
  public void testGetAllExpenses() throws Exception {
    given(expenseService.getAllExpenses()).willReturn(Arrays.asList(expense1, expense2));

    mockMvc.perform(get("/api/expenses"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0]._id", is(expense1Id)))
        .andExpect(jsonPath("$[0].title", is("Groceries")))
        .andExpect(jsonPath("$[1]._id", is(expense2Id)))
        .andExpect(jsonPath("$[1].title", is("Electricity")));
  }

  @Test
  public void testCreateExpense() throws Exception {
    given(expenseService.createExpense(any(Expense.class))).willReturn(expense1);

    String expenseJson = objectMapper.writeValueAsString(expense1);

    mockMvc.perform(post("/api/expenses")
        .contentType(MediaType.APPLICATION_JSON)
        .content(expenseJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$._id", is("exp1")))
        .andExpect(jsonPath("$.title", is("Groceries")));
  }

  @Test
  public void testGetExpenseById() throws Exception {
    given(expenseService.getExpenseById(expense1Id)).willReturn(Optional.of(expense1));

    mockMvc.perform(get("/api/expenses/" + expense1Id.toHexString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$._id", is(expense1Id)))
        .andExpect(jsonPath("$.title", is("Groceries")));
  }

  @Test
  public void testGetExpenseById_NotFound() throws Exception {
    ObjectId non_existing_expense_id = new ObjectId();
    given(expenseService.getExpenseById(non_existing_expense_id)).willReturn(Optional.empty());

    mockMvc.perform(get("/api/expenses/" + non_existing_expense_id.toHexString()))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testUpdateExpense() throws Exception {
    Expense updatedExpense = new Expense();
    updatedExpense.set_id(expense1Id);
    updatedExpense.setTitle("Updated Groceries");
    updatedExpense.setAmount(60.0);
    updatedExpense.setPaidBy(user1Id);
    updatedExpense.setUsers(Arrays.asList(user1Id, user2Id));

    given(expenseService.updateExpense(eq(expense1Id), any(Expense.class))).willReturn(Optional.of(updatedExpense));

    String updatedExpenseJson = objectMapper.writeValueAsString(updatedExpense);

    mockMvc.perform(put("/api/expenses/" + expense1Id.toHexString())
        .contentType(MediaType.APPLICATION_JSON)
        .content(updatedExpenseJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("Updated Groceries")))
        .andExpect(jsonPath("$.amount", is(60.0)));
  }

  @Test
  public void testDeleteExpense() throws Exception {
    given(expenseService.deleteExpense(expense1Id)).willReturn(true);

    mockMvc.perform(delete("/api/expenses/" + expense1Id.toHexString()))
        .andExpect(status().isNoContent());
  }

  @Test
  public void testDeleteExpense_NotFound() throws Exception {
    ObjectId non_existing_expense_id = new ObjectId();
    given(expenseService.deleteExpense(non_existing_expense_id)).willReturn(false);

    mockMvc.perform(delete("/api/expenses/" + non_existing_expense_id.toHexString()))
        .andExpect(status().isNotFound());
  }
}
