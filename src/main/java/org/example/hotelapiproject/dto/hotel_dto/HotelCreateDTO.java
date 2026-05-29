package org.example.hotelapiproject.dto.hotel_dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class HotelCreateDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private BigDecimal latitude;

    @NotBlank
    private BigDecimal longitude;

}
