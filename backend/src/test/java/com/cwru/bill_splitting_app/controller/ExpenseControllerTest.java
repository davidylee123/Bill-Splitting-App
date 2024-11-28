package com.cwru.bill_splitting_app.controller;

import com.cwru.bill_splitting_app.model.Expense;
import com.cwru.bill_splitting_app.service.ExpenseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ExpenseControllerTest {

    @Mock
    private ExpenseService expenseService;

    @InjectMocks
    private ExpenseController expenseController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Expense expense1;
    private Expense expense2;

    @BeforeEach
    public void setup() {
        expense1 = new Expense(new ObjectId("644000000000000000000001"), "Groceries", 50.0, "David Lee", Arrays.asList("David Lee", "Jose Kim"));
        expense2 = new Expense(new ObjectId("644000000000000000000002"), "Electricity", 75.5, "Jose Kim", Arrays.asList("David Lee", "Jose Kim"));
    }

//    @Test
//    public void testGetAllExpenses() throws Exception {
//        when(expenseService.getAllExpenses()).thenReturn(Arrays.asList(expense1, expense2));
//
//        MvcResult result = mockMvc.perform(get("/api/expenses"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0]._id").value(expense1.get_id().toString()))
//                .andExpect(jsonPath("$[0].name").value("Groceries"))
//                .andExpect(jsonPath("$[1]._id").value(expense2.get_id().toString()))
//                .andExpect(jsonPath("$[1].name").value("Electricity"))
//                .andReturn();
//    }

    @Test
    public void testCreateExpense() throws Exception {
        Expense newExpense = new Expense(new ObjectId(), "Groceries", 50.0, "David Lee", Arrays.asList("David Lee", "Jose Kim"));
        when(expenseService.createExpense(any(Expense.class))).thenReturn(newExpense);

        String expenseJson = objectMapper.writeValueAsString(newExpense);

        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expenseJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Groceries"));
    }

//    @Test
//    public void testGetExpenseById() throws Exception {
//        when(expenseService.getExpenseById(new ObjectId("644000000000000000000001"))).thenReturn(Optional.of(expense1));
//
//        mockMvc.perform(get("/api/expenses/644000000000000000000001"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$._id").value(expense1.get_id().toString()))
//                .andExpect(jsonPath("$.name").value("Groceries"));
//    }

    @Test
    public void testGetExpenseById_NotFound() throws Exception {
        when(expenseService.getExpenseById(new ObjectId("644000000000000000000003"))).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/expenses/644000000000000000000003"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateExpense() throws Exception {
        Expense updatedExpense = new Expense(new ObjectId("644000000000000000000001"), "Updated Groceries", 60.0, "David Lee", Arrays.asList("David Lee", "Jose Kim"));

        when(expenseService.updateExpense(eq(new ObjectId("644000000000000000000001")), any(Expense.class)))
                .thenReturn(Optional.empty());

        String updatedExpenseJson = objectMapper.writeValueAsString(updatedExpense);

        mockMvc.perform(put("/api/expenses/644000000000000000000001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedExpenseJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateExpense_NotFound() throws Exception {
        when(expenseService.updateExpense(eq(new ObjectId("644000000000000000000001")), any(Expense.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/expenses/644000000000000000000001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expense1)))
                .andExpect(status().isNotFound());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable String id) {
        try {
            boolean deleted = expenseService.deleteExpense(new ObjectId(id));
            return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Test
    public void testDeleteExpense_NotFound() throws Exception {
        when(expenseService.deleteExpense(new ObjectId("644000000000000000000003"))).thenReturn(false);

        mockMvc.perform(delete("/api/expenses/644000000000000000000003"))
                .andExpect(status().isNotFound());
    }
}