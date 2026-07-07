package org.example.hotelapiproject.dto.hotel_dto;

import lombok.Data;
import org.example.hotelapiproject.dto.review_dto.ReviewShortResponseDTO;
import org.example.hotelapiproject.dto.room_dto.RoomShortDTO;

import java.math.BigDecimal;
import java.util.List;

@Data
public class HotelResponseDTO {

    private String name;
    private String description;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private List<RoomShortDTO> rooms;
    private List<ReviewShortResponseDTO> reviews;
}
