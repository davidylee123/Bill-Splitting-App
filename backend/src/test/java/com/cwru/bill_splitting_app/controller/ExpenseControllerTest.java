package com.cwru.bill_splitting_app.controller;

import com.cwru.bill_splitting_app.model.Expense;
import com.cwru.bill_splitting_app.model.User;
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

  private User user1;
  private User user2;

  @BeforeEach
  public void setup() {
    expense1 = new Expense();
    expense1Id = new ObjectId();
    expense1.set_id(expense1Id);
    expense1.setTitle("Groceries");
    expense1.setAmount(50.0);
    user1 = new User();
    user2 = new User();
    expense1.setPaidBy(user1);
    expense1.setUsers(Arrays.asList(user1, user2));

    expense2 = new Expense();
    expense2Id = new ObjectId();
    expense2.set_id(expense2Id);
    expense2.setTitle("Electricity");
    expense2.setAmount(75.5);
    expense2.setPaidBy(user2);
    expense2.setUsers(Arrays.asList(user1, user2));
  }

  @Test
  public void testCreateExpense() throws Exception {
    user1 = new User();
    user1.set_id(new ObjectId());
    user1.setName("Alice Johnson");
    user1.setEmail("alice.johnson@example.com");

    user2 = new User();
    user2.set_id(new ObjectId());
    user2.setName("Bob Williams");
    user2.setEmail("bob.williams@example.com");

    Expense expenseToCreate = new Expense();
    expenseToCreate.set_id(expense1Id);
    expenseToCreate.setTitle("Groceries");
    expenseToCreate.setAmount(50.0);
    expenseToCreate.setPaidBy(user1);
    expenseToCreate.setUsers(Arrays.asList(user1, user2));

    given(expenseService.createExpense(any(Expense.class))).willReturn(expenseToCreate);

    String expenseJson = objectMapper.writeValueAsString(expenseToCreate);

    mockMvc.perform(post("/api/expenses")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(expenseJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title", is("Groceries")))
            .andExpect(jsonPath("$.amount", is(50.0)))
            .andExpect(jsonPath("$.paidBy.name", is("Alice Johnson")))
            .andExpect(jsonPath("$.paidBy.email", is("alice.johnson@example.com")))
            .andExpect(jsonPath("$.users[0].name", is("Alice Johnson")))
            .andExpect(jsonPath("$.users[1].name", is("Bob Williams")));
  }

  @Test
  public void testGetExpenseById() throws Exception {
    given(expenseService.getExpenseById(expense1Id)).willReturn(Optional.of(expense1));

    mockMvc.perform(get("/api/expenses/" + expense1Id.toHexString()))
        .andExpect(status().isOk())
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
    updatedExpense.setPaidBy(user1);
    updatedExpense.setUsers(Arrays.asList(user1, user2));

    given(expenseService.updateExpense(eq(expense1Id), any(Expense.class))).willReturn(Optional.of(updatedExpense));

    String updatedExpenseJson = objectMapper.writeValueAsString(updatedExpense);

    mockMvc.perform(put("/api/expenses/" + expense1Id.toHexString())
        .contentType(MediaType.APPLICATION_JSON)
        .content(updatedExpenseJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title", is("Updated Groceries")))
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
