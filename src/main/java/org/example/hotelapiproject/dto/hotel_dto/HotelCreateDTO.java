package org.example.hotelapiproject.dto.hotel_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class HotelCreateDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private BigDecimal latitude;

    @NotNull
    private BigDecimal longitude;

}
