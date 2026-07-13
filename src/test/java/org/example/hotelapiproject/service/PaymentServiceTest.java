package org.example.hotelapiproject.service;

import org.example.hotelapiproject.exeption.payment.PaymentNotFoundException;
import org.example.hotelapiproject.repository.BookingRepository;
import org.example.hotelapiproject.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void createPayment() {
    }

    @Test
    void confirmPayment() {
    }

    @Test
    void confirmPaymentPaymentNotFound() {

        when(paymentRepository.findByStripeSessionId(anyString()))
                .thenReturn(Optional.empty());

        PaymentNotFoundException exception = assertThrows(
                PaymentNotFoundException.class,
                () -> paymentService.confirmPayment("session123"));

        assertEquals("Payment not found", exception.getMessage());
    }

}