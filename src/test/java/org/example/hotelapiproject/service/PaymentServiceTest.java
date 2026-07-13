package org.example.hotelapiproject.service;

import com.stripe.exception.StripeException;
import org.example.hotelapiproject.dto.checkout_session_dto.CheckoutSessionData;
import org.example.hotelapiproject.dto.payments_dto.PaymentResponse;
import org.example.hotelapiproject.entity.Account;
import org.example.hotelapiproject.entity.Booking;
import org.example.hotelapiproject.entity.Payment;
import org.example.hotelapiproject.entity.Room;
import org.example.hotelapiproject.entity.enums.BookingStatus;
import org.example.hotelapiproject.entity.enums.PaymentStatus;
import org.example.hotelapiproject.exeption.payment.PaymentNotFoundException;
import org.example.hotelapiproject.repository.BookingRepository;
import org.example.hotelapiproject.repository.PaymentRepository;
import org.example.hotelapiproject.service.payment_integration.StripeService;
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

    @Mock
    private StripeService stripeService;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void createPayment() throws StripeException {

        Booking booking = mockBook();

        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        when(stripeService.createCheckoutSession(booking))
                .thenReturn(
                        new CheckoutSessionData(
                                "session123",
                                "https://checkout.stripe.com/test"
                        )
                );

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PaymentResponse response = paymentService.createPayment(booking.getId());

        assertEquals("PENDING", response.paymentStatus());
        assertEquals("https://checkout.stripe.com/test",
                response.checkoutUrl());

        verify(paymentRepository).save(any(Payment.class));
        verify(stripeService).createCheckoutSession(booking);
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
    void confirmPaymentBookingConfirmed() {

        Payment payment = mockPayment();

        when(paymentRepository.findByStripeSessionId("stripeSession"))
                .thenReturn(Optional.of(payment));

        when(bookingRepository.save(any(Booking.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        paymentService.confirmPayment("stripeSession");

        assertEquals(BookingStatus.CONFIRMED, payment.getBooking().getStatus());

        verify(bookingRepository).save(payment.getBooking());
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
                .booking(mockBook())
                .amount(BigDecimal.valueOf(235))
                .providerPaymentId("123-qwer-456-asdf")
                .status(PaymentStatus.PENDING)
                .stripeSessionId("stripeSession")
                .stripePaymentIntentId("stripePaymentIntent")
                .build();
    }

    private Booking mockBook() {
        return Booking.builder()
                .id(5L)
                .room(Room.builder()
                        .id(9L)
                        .build())
                .account(Account.builder()
                        .id(1L)
                        .build())
                .status(BookingStatus.PENDING)
                .totalPrice(BigDecimal.valueOf(185))
                .build();
    }
}