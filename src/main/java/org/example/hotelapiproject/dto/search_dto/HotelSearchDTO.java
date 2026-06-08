package org.example.hotelapiproject.dto.search_dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class HotelSearchDTO {
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private LocalDate reserveFrom;
    private LocalDate reserveTo;
}
