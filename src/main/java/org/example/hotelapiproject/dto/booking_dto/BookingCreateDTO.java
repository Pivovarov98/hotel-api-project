package org.example.hotelapiproject.dto.booking_dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class BookingCreateDTO {

    private Long roomId;
    private BigDecimal totalPrice;
    private LocalDate reserveFrom;
    private LocalDate reserveTo;
}
