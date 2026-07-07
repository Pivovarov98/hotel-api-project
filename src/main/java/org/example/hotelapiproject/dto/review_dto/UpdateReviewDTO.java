package org.example.hotelapiproject.dto.review_dto;

import lombok.Data;

@Data
public class UpdateReviewDTO {
    String title;
    String description;
    int rating;
}
