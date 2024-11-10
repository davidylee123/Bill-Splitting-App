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

    private Expense expense1;
    private Expense expense2;

    @BeforeEach
    public void setup() {
        expense1 = new Expense();
        expense1.setId("exp1");
        expense1.setName("Groceries");
        expense1.setAmount(50.0);
        expense1.setPaidBy("David Lee");
        expense1.setSplitBetween(Arrays.asList("David Lee", "Jose Kim"));

        expense2 = new Expense();
        expense2.setId("exp2");
        expense2.setName("Electricity");
        expense2.setAmount(75.5);
        expense2.setPaidBy("Jose Kim");
        expense2.setSplitBetween(Arrays.asList("David Lee", "Jose Kim"));
    }

    @Test
    public void testGetAllExpenses() throws Exception {
        given(expenseService.getAllExpenses()).willReturn(Arrays.asList(expense1, expense2));

        mockMvc.perform(get("/api/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is("exp1")))
                .andExpect(jsonPath("$[0].name", is("Groceries")))
                .andExpect(jsonPath("$[1].id", is("exp2")))
                .andExpect(jsonPath("$[1].name", is("Electricity")));
    }

    @Test
    public void testCreateExpense() throws Exception {
        given(expenseService.createExpense(any(Expense.class))).willReturn(expense1);

        String expenseJson = objectMapper.writeValueAsString(expense1);

        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expenseJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("exp1")))
                .andExpect(jsonPath("$.name", is("Groceries")));
    }

    @Test
    public void testGetExpenseById() throws Exception {
        given(expenseService.getExpenseById("exp1")).willReturn(Optional.of(expense1));

        mockMvc.perform(get("/api/expenses/exp1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("exp1")))
                .andExpect(jsonPath("$.name", is("Groceries")));
    }

    @Test
    public void testGetExpenseById_NotFound() throws Exception {
        given(expenseService.getExpenseById("exp3")).willReturn(Optional.empty());

        mockMvc.perform(get("/api/expenses/exp3"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateExpense() throws Exception {
        Expense updatedExpense = new Expense();
        updatedExpense.setId("exp1");
        updatedExpense.setName("Updated Groceries");
        updatedExpense.setAmount(60.0);
        updatedExpense.setPaidBy("David Lee");
        updatedExpense.setSplitBetween(Arrays.asList("David Lee", "Jose Kim"));

        given(expenseService.updateExpense(eq("exp1"), any(Expense.class))).willReturn(Optional.of(updatedExpense));

        String updatedExpenseJson = objectMapper.writeValueAsString(updatedExpense);

        mockMvc.perform(put("/api/expenses/exp1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedExpenseJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Groceries")))
                .andExpect(jsonPath("$.amount", is(60.0)));
    }

    @Test
    public void testDeleteExpense() throws Exception {
        given(expenseService.deleteExpense("exp1")).willReturn(true);

        mockMvc.perform(delete("/api/expenses/exp1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteExpense_NotFound() throws Exception {
        given(expenseService.deleteExpense("exp3")).willReturn(false);

        mockMvc.perform(delete("/api/expenses/exp3"))
                .andExpect(status().isNotFound());
    }
}