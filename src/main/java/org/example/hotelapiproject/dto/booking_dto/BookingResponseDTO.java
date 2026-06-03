package org.example.hotelapiproject.dto.booking_dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingResponseDTO {

    private Long id;
    private Long roomId;
    private Long accountId;
    private LocalDate reserveFrom;
    private LocalDate reserveTo;
}
