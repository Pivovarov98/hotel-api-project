package org.example.hotelapiproject.controller;

import com.stripe.exception.StripeException;
import org.example.hotelapiproject.dto.payments_dto.PaymentResponse;
import org.example.hotelapiproject.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/{booking_id}")
    public ResponseEntity<PaymentResponse> createPayment(@PathVariable Long booking_id) throws StripeException {
        return ResponseEntity.ok().body(paymentService.createPayment(booking_id));
    }
}
