package org.example.hotelapiproject.dto.review_dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateReviewDTO {

    String title;
    String description;

    @NotNull
    int rating;

    @NotNull
    Long hotelId;
}
