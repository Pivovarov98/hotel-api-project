package org.example.hotelapiproject.dto.hotel_dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class HotelUpdateDTO {

    private String name;
    private String description;
    private BigDecimal latitude;
    private BigDecimal longitude;

}
