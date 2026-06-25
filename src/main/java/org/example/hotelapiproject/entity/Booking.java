package org.example.hotelapiproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.hotelapiproject.entity.enums.BookingStatus;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id")
    private Account account;

    private LocalDate reserveFrom;

    private LocalDate reserveTo;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
