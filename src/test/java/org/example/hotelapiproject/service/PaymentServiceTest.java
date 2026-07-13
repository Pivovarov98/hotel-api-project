package org.example.hotelapiproject.service;

import org.example.hotelapiproject.entity.Booking;
import org.example.hotelapiproject.entity.Payment;
import org.example.hotelapiproject.entity.enums.BookingStatus;
import org.example.hotelapiproject.entity.enums.PaymentStatus;
import org.example.hotelapiproject.exeption.payment.PaymentNotFoundException;
import org.example.hotelapiproject.repository.BookingRepository;
import org.example.hotelapiproject.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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
    void confirmPaymentPaymentSuccess() {

        Payment payment = mockPayment();

        when(paymentRepository.findByStripeSessionId("stripeSession"))
                .thenReturn(Optional.of(payment));

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        paymentService.confirmPayment("stripeSession");

        assertEquals(PaymentStatus.SUCCESS, payment.getStatus());

        verify(paymentRepository).save(payment);
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

    private Payment mockPayment() {
        return Payment.builder()
                .id(7L)
                .booking(Booking.builder()
                        .id(3L)
                        .status(BookingStatus.PENDING)
                        .build())
                .amount(BigDecimal.valueOf(235))
                .providerPaymentId("123-qwer-456-asdf")
                .status(PaymentStatus.PENDING)
                .stripeSessionId("stripeSession")
                .stripePaymentIntentId("stripePaymentIntent")
                .build();
    }

}