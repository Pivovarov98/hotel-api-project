package org.example.hotelapiproject.service.payment_integration;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.example.hotelapiproject.dto.checkout_session_dto.CheckoutSessionData;
import org.example.hotelapiproject.entity.Booking;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StripeService {

    public CheckoutSessionData createCheckoutSession(Booking booking) throws StripeException {

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

        return new CheckoutSessionData(session.getId(), session.getUrl());
    }
}
