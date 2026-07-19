package org.example.hotelapiproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.hotelapiproject.dto.booking_dto.BookingCreateDTO;
import org.example.hotelapiproject.dto.booking_dto.BookingResponseDTO;
import org.example.hotelapiproject.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookingService bookingService;

    @Test
    void createBooking() throws Exception {

        BookingCreateDTO createDTO = BookingCreateDTO.builder()
                .roomId(1L)
                .totalPrice(BigDecimal.valueOf(250))
                .reserveFrom(LocalDate.of(2026, 7, 20))
                .reserveTo(LocalDate.of(2026, 7, 25))
                .build();

        BookingResponseDTO response = BookingResponseDTO.builder()
                .id(7L)
                .roomId(1L)
                .accountId(1L)
                .reserveFrom(LocalDate.of(2026, 7, 20))
                .reserveTo(LocalDate.of(2026, 7, 25))
                .build();

        when(bookingService.createBooking(createDTO))
                .thenReturn(response);

        mockMvc.perform(post("/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roomId").value(response.getRoomId()))
                .andExpect(jsonPath("$.accountId").value(response.getAccountId()))
                .andExpect(jsonPath("$.reserveFrom").value(response.getReserveFrom().toString()))
                .andExpect(jsonPath("$.reserveTo").value(response.getReserveTo().toString()));
    }

    @Test
    void deleteBookById() throws Exception {

        mockMvc.perform(delete("/booking/8"))
                .andExpect(status().isNoContent());

        verify(bookingService).deleteBookingByID(8L);
    }
}