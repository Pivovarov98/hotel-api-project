package org.example.hotelapiproject.service;

import org.example.hotelapiproject.entity.Review;
import org.example.hotelapiproject.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    public ReviewResponceDTO createReview(CreateReviewDTO dto){

        Review review = Review.builder()
                .reviewTitle(dto.getTitle)
                .reviewDescription(dto.getDescription)
                .rating(dto.getRating)
                .hotel(dto.getHotelId)
                .build();

        reviewRepository.save(review);
        return responceDTO(review);
    }
}
