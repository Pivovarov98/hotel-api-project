package org.example.hotelapiproject.controller;

import org.example.hotelapiproject.dto.payments_dto.PaymentResponse;
import org.example.hotelapiproject.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(PaymentController.class)
@AutoConfigureMockMvc(addFilters = false)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService paymentService;

    @Test
    void createPayment() throws Exception {

        PaymentResponse response = new PaymentResponse(
                7L,
                "PENDING",
                "https://checkout.stripe.com/test"
        );

        when(paymentService.createPayment(1L))
                .thenReturn(response);

        mockMvc.perform(post("/payments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value(7))
                .andExpect(jsonPath("$.paymentStatus").value("PENDING"))
                .andExpect(jsonPath("$.checkoutUrl")
                        .value("https://checkout.stripe.com/test"));

        verify(paymentService).createPayment(1L);
    }
}