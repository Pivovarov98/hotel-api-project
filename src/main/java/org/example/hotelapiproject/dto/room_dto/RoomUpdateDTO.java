package org.example.hotelapiproject.dto.room_dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomUpdateDTO {

    @NotBlank
    private BigDecimal price;

    @NotBlank
    private String roomTitle;

    @NotBlank
    private String roomDescription;
}
