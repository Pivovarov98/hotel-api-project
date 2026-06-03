package org.example.hotelapiproject.dto.room_dto;

import java.math.BigDecimal;

public record RoomShortDTO(Long id, BigDecimal price, String title) {
}
