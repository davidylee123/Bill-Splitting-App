package com.cwru.bill_splitting_app.controller;

import com.cwru.bill_splitting_app.model.Bill;
import com.cwru.bill_splitting_app.service.BillService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BillController.class)
@WithMockUser(username = "testuser", roles = {"USER"})
public class BillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BillService billService;

    @Autowired
    private ObjectMapper objectMapper;

    private Bill sampleBill;
    private ObjectId sampleId;

    @BeforeEach
    public void setUp() {
        sampleId = new ObjectId();
        sampleBill = new Bill();
        sampleBill.setId(sampleId);
        sampleBill.setTitle("Sample Bill");
    }

    @Test
    public void testGetAllBills() throws Exception {
        Mockito.when(billService.getAllBills()).thenReturn(Collections.singletonList(sampleBill));

        mockMvc.perform(get("/api/bills"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Sample Bill"));
    }

    @Test
    public void testGetBillById() throws Exception {
        Mockito.when(billService.getBillById(sampleId)).thenReturn(Optional.of(sampleBill));

        mockMvc.perform(get("/api/bills/" + sampleId.toHexString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Sample Bill"));
    }

    @Test
    public void testGetBillByExpenseId() throws Exception {
        Mockito.when(billService.getBillByExpenseId(sampleId)).thenReturn(Optional.of(sampleBill));

        mockMvc.perform(get("/api/bills/expenses/" + sampleId.toHexString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Sample Bill"));
    }

    @Test
    public void testCreateBill() throws Exception {
        Mockito.when(billService.createBill(Mockito.any(Bill.class))).thenReturn(sampleBill);

        mockMvc.perform(post("/api/bills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleBill))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Sample Bill"));
    }

    @Test
    public void testUpdateBill() throws Exception {
        Mockito.when(billService.updateBill(Mockito.eq(sampleId), Mockito.any(Bill.class))).thenReturn(Optional.of(sampleBill));

        mockMvc.perform(put("/api/bills/" + sampleId.toHexString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleBill))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Sample Bill"));
    }

    @Test
    public void testDeleteBill() throws Exception {
        Mockito.when(billService.deleteBill(sampleId)).thenReturn(true);

        mockMvc.perform(delete("/api/bills/" + sampleId.toHexString()).with(csrf()))
                .andExpect(status().isNoContent());
    }
}