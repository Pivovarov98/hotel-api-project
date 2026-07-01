package org.example.hotelapiproject.dto.payments_dto;

public record PaymentResponse(Long paymentId, String paymentStatus, String checkoutUrl) {
}
