package org.example.hotelapiproject.service;

import org.example.hotelapiproject.dto.review_dto.CreateReviewDTO;
import org.example.hotelapiproject.dto.review_dto.ReviewResponseDTO;
import org.example.hotelapiproject.dto.review_dto.UpdateReviewDTO;
import org.example.hotelapiproject.entity.Hotel;
import org.example.hotelapiproject.entity.Review;
import org.example.hotelapiproject.repository.HotelRepository;
import org.example.hotelapiproject.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void createReview() {

        CreateReviewDTO dto = CreateReviewDTO.builder()
                .title("Test title")
                .description("Test Description")
                .rating(4)
                .hotelId(1L)
                .build();

        Hotel hotel = new Hotel();
        hotel.setId(1L);

        when(hotelRepository.findById(1L))
                .thenReturn(Optional.of(hotel));

        Review savedReview = Review.builder()
                .reviewTitle(dto.getTitle())
                .reviewDescription(dto.getDescription())
                .rating(dto.getRating())
                .hotel(hotel)
                .build();

        when(reviewRepository.save(any(Review.class)))
                .thenReturn(savedReview);

        ReviewResponseDTO responseDTO = reviewService.createReview(dto);

        assertEquals("Test title", responseDTO.title());
        assertEquals("Test Description", responseDTO.description());
        assertEquals(4, responseDTO.rating());
        assertEquals(1L, responseDTO.hotelId());
    }

    @Test
    void updateReview() {

        Long reviewId = 1L;

        Hotel hotel = new Hotel();
        hotel.setId(1L);

        UpdateReviewDTO dto = UpdateReviewDTO.builder()
                .title("New test title")
                .description("New test description")
                .rating(5)
                .build();

        Review review = Review.builder()
                .id(reviewId)
                .reviewTitle("Old title")
                .reviewDescription("Old description")
                .rating(3)
                .hotel(hotel)
                .build();

        when(reviewRepository.findById(reviewId))
                .thenReturn(Optional.of(review));

        when(reviewRepository.save(any(Review.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ReviewResponseDTO responseDTO = reviewService.updateReview(reviewId, dto);

        assertEquals("New test title", responseDTO.title());
        assertEquals("New test description", responseDTO.description());
        assertEquals(5, responseDTO.rating());
    }

    @Test
    void deleteReview() {

        Long reviewId = 1L;

        Hotel hotel = new Hotel();
        hotel.setId(1L);

        Review review = Review.builder()
                .id(reviewId)
                .reviewTitle("Test title")
                .reviewDescription("Test description")
                .rating(4)
                .hotel(hotel)
                .build();

        when(reviewRepository.findById(reviewId))
                .thenReturn(Optional.of(review));

        reviewService.deleteReview(reviewId);

        verify(reviewRepository).deleteById(reviewId);
    }
}