package org.example.hotelapiproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.hotelapiproject.entity.enums.PaymentStatus;

import java.math.BigDecimal;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    Booking booking;

    private BigDecimal amount;

    private String providerPaymentId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(unique = true)
    private String stripeSessionId;

    private String stripePaymentIntentId;
}
