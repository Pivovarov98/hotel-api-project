package org.example.hotelapiproject.dto.booking_dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingCreateDTO {
    private LocalDate reserveFrom;
    private LocalDate reserveTo;
}
