package org.example.hotelapiproject.service;

import org.example.hotelapiproject.dto.review_dto.CreateReviewDTO;
import org.example.hotelapiproject.dto.review_dto.ReviewResponseDTO;
import org.example.hotelapiproject.dto.review_dto.UpdateReviewDTO;
import org.example.hotelapiproject.entity.Hotel;
import org.example.hotelapiproject.entity.Review;
import org.example.hotelapiproject.exeption.hotel.HotelNotFoundException;
import org.example.hotelapiproject.exeption.review.ReviewNotFoundException;
import org.example.hotelapiproject.repository.HotelRepository;
import org.example.hotelapiproject.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    HotelRepository hotelRepository;

    public ReviewResponseDTO createReview(CreateReviewDTO dto){

        Hotel hotel = hotelRepository.findById(dto.getHotelId())
                .orElseThrow(() -> new HotelNotFoundException("Hotel not found"));

        Review review = Review.builder()
                .reviewTitle(dto.getTitle())
                .reviewDescription(dto.getDescription())
                .rating(dto.getRating())
                .hotel(hotel)
                .build();

        reviewRepository.save(review);
        return responseDTO(review);
    }

    public ReviewResponseDTO updateReview(Long reviewId, UpdateReviewDTO dto){

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found"));

        review.setReviewTitle(dto.getTitle());
        review.setReviewDescription(dto.getDescription());
        review.setRating(dto.getRating());

        reviewRepository.save(review);

        return responseDTO(review);
    }

    public void deleteReview(Long reviewId){

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found"));

        reviewRepository.deleteById(reviewId);
    }

    private ReviewResponseDTO responseDTO(Review review){
        return new ReviewResponseDTO(review.getReviewTitle(),
                                     review.getReviewDescription(),
                                     review.getRating(),
                                     review.getHotel().getId());
    }
}
