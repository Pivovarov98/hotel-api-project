package org.example.hotelapiproject.dto.card;

import java.math.BigDecimal;

public record CardHotelResponseDTO(
        Long id,
        String name,
        String description,
        BigDecimal minAvailablePrice,
        boolean favorite
) {}
