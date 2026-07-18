package org.example.hotelapiproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.hotelapiproject.dto.review_dto.CreateReviewDTO;
import org.example.hotelapiproject.dto.review_dto.ReviewResponseDTO;
import org.example.hotelapiproject.dto.review_dto.UpdateReviewDTO;
import org.example.hotelapiproject.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReviewControllerTest {

    @MockitoBean
    private ReviewService reviewService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createReview() throws Exception {

        CreateReviewDTO dto = CreateReviewDTO.builder()
                .title("Title")
                .description("Description")
                .rating(5)
                .hotelId(1L)
                .build();

        ReviewResponseDTO response =
                new ReviewResponseDTO(
                        "Test title",
                        "Test description",
                        5,
                        1L);

        when(reviewService.createReview(any()))
                .thenReturn(response);

        mockMvc.perform(post("/hotel/review/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test title"))
                .andExpect(jsonPath("$.description").value("Test description"))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.hotelId").value(1));
    }

    @Test
    void updateReview() throws Exception {

        UpdateReviewDTO dto = UpdateReviewDTO.builder()
                .title("New title")
                .description("New description")
                .rating(4)
                .build();

        ReviewResponseDTO response = new ReviewResponseDTO(
                "New title",
                "New description",
                4,
                1L);

        when(reviewService.updateReview(eq(10L), any()))
                .thenReturn(response);

        mockMvc.perform(patch("/review/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New title"));

        verify(reviewService).updateReview(eq(10L), any());
    }

    @Test
    void deleteReview() throws Exception {

        mockMvc.perform(delete("/review/10"))
                .andExpect(status().isNoContent());

        verify(reviewService).deleteReview(10L);
    }
}