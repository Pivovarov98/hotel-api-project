package org.example.hotelapiproject.service;

import com.stripe.exception.StripeException;
import jakarta.transaction.Transactional;
import org.example.hotelapiproject.dto.checkout_session_dto.CheckoutSessionData;
import org.example.hotelapiproject.dto.payments_dto.PaymentResponse;
import org.example.hotelapiproject.entity.Booking;
import org.example.hotelapiproject.entity.Payment;
import org.example.hotelapiproject.entity.enums.BookingStatus;
import org.example.hotelapiproject.entity.enums.PaymentStatus;
import org.example.hotelapiproject.exeption.booking.BookNotFoundException;
import org.example.hotelapiproject.exeption.payment.PaymentNotFoundException;
import org.example.hotelapiproject.repository.BookingRepository;
import org.example.hotelapiproject.repository.PaymentRepository;
import org.example.hotelapiproject.service.payment_integration.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private StripeService stripeService;

    public PaymentResponse createPayment(Long booking_id) throws StripeException {
        Booking booking = bookingRepository.findById(booking_id)
                .orElseThrow(() -> new BookNotFoundException("Book not find"));

        CheckoutSessionData sessionData = stripeService.createCheckoutSession(booking);

        Payment payment = Payment.builder()
                .booking(booking)
                .amount(booking.getTotalPrice())
                .status(PaymentStatus.PENDING)
                .stripeSessionId(sessionData.sessionId())
                .build();

        paymentRepository.save(payment);

        bookingRepository.save(booking);
        return new PaymentResponse(payment.getId(), payment.getStatus().name(), sessionData.checkoutUrl());
    }

    @Transactional
    public void confirmPayment(String stripeSessionId) {

        Payment payment =
                paymentRepository
                        .findByStripeSessionId(stripeSessionId)
                        .orElseThrow(() -> new PaymentNotFoundException("Payment not found"));

        payment.setStatus(PaymentStatus.SUCCESS);

        Booking booking = payment.getBooking();

        booking.setStatus(BookingStatus.CONFIRMED);

        bookingRepository.save(booking);
        paymentRepository.save(payment);
    }
}
