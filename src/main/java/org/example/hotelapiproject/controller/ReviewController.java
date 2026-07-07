package org.example.hotelapiproject.controller;


import org.example.hotelapiproject.dto.review_dto.CreateReviewDTO;
import org.example.hotelapiproject.dto.review_dto.ReviewResponseDTO;
import org.example.hotelapiproject.dto.review_dto.UpdateReviewDTO;
import org.example.hotelapiproject.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @PostMapping("/hotel/reviwe/new")
    public ResponseEntity<ReviewResponseDTO> createReview(@RequestBody CreateReviewDTO dto){

        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(dto));
    }

    @PatchMapping("/reviwe/{review_id}")
    public ResponseEntity<ReviewResponseDTO> updateReview(@PathVariable Long review_id,
                                                          @RequestBody UpdateReviewDTO dto){

        return ResponseEntity.ok().body(reviewService.updateReview(review_id, dto));
    }

    @DeleteMapping("/reviwe/{review_id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long review_id){

        reviewService.deleteReview(review_id);
        return ResponseEntity.noContent().build();
    }
}
