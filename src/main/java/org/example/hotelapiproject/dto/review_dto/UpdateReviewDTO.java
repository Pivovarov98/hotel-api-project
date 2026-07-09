package org.example.hotelapiproject.dto.review_dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateReviewDTO {
    String title;
    String description;
    int rating;
}
