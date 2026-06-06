package org.example.hotelapiproject.dto.card;

public record CardHotelResponseDTO(
        Long id,
        String name,
        String description,
        boolean favorite
) {}
