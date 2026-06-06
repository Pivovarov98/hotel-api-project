package org.example.hotelapiproject.dto.card;

import java.math.BigDecimal;

public record CardRoomResponseDTO(
        Long id,
        String title,
        String description,
        BigDecimal price,
        boolean favorite
) {}
