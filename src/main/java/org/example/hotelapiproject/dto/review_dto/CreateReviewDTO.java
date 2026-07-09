package org.example.hotelapiproject.dto.review_dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateReviewDTO {

    String title;
    String description;

    @NotNull
    int rating;

    @NotNull
    Long hotelId;
}
