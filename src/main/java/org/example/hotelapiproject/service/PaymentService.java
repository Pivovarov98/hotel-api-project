package org.example.hotelapiproject.service;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.example.hotelapiproject.dto.booking_dto.BookingResponseDTO;
import org.example.hotelapiproject.dto.payments_dto.PaymentResponse;
import org.example.hotelapiproject.entity.Booking;
import org.example.hotelapiproject.entity.Payment;
import org.example.hotelapiproject.entity.enums.BookingStatus;
import org.example.hotelapiproject.entity.enums.PaymentStatus;
import org.example.hotelapiproject.repository.BookingRepository;
import org.example.hotelapiproject.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    BookingRepository bookingRepository;

    public PaymentResponse createPayment(Long booking_id) throws StripeException {
        Booking booking = bookingRepository.findById(booking_id)
                .orElseThrow(() -> new RuntimeException("Book not find"));

        String checkoutUrl = createCheckoutSession(booking);

        Payment payment = Payment.builder()
                .booking(booking)
                .amount(booking.getTotalPrice())
                .status(PaymentStatus.PENDING)
                .build();

        paymentRepository.save(payment);

        bookingRepository.save(booking);
        return new PaymentResponse(checkoutUrl);
    }

    public String createCheckoutSession(Booking booking) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/api/v1/success")
                .setCancelUrl("http://localhost:8080/api/v1/cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(SessionCreateParams
                                        .LineItem
                                        .PriceData
                                        .builder()
                                        .setCurrency("eur")
                                        .setUnitAmount(
                                                booking
                                                        .getTotalPrice()
                                                        .multiply(BigDecimal.valueOf(100))
                                                        .longValue()
                                        )
                                        .setProductData(
                                                SessionCreateParams
                                                        .LineItem
                                                        .PriceData
                                                        .ProductData
                                                        .builder()
                                                        .setName("Hotel booking")
                                                        .build()
                                        )
                                        .build()
                                )
                                .build()
                )
                .build();

        Session session = Session.create(params);

        return session.getUrl();
    }
}
