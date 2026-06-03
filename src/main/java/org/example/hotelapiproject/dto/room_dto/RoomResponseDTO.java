package org.example.hotelapiproject.dto.room_dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomResponseDTO {

    private Long hotelId;
    private BigDecimal price;
    private String roomTitle;
    private String roomDescription;
}
