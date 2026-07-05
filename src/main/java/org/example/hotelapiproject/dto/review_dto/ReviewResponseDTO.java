package org.example.hotelapiproject.dto.review_dto;

public record ReviewResponseDTO(String title, String description, int rating, Long hotelId) {
}
