package org.example.hotelapiproject.dto.room_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomCreateDTO {

    @NotNull
    private BigDecimal price;

    @NotBlank
    private String roomTitle;

    @NotBlank
    private String roomDescription;
}
