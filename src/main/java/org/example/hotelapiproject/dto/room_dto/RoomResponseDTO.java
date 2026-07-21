package org.example.hotelapiproject.dto.room_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomResponseDTO {

    private Long hotelId;
    private BigDecimal price;
    private String roomTitle;
    private String roomDescription;
}
