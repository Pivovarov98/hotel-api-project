package org.example.hotelapiproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.hotelapiproject.dto.room_dto.RoomCreateDTO;
import org.example.hotelapiproject.dto.room_dto.RoomResponseDTO;
import org.example.hotelapiproject.dto.room_dto.RoomUpdateDTO;
import org.example.hotelapiproject.service.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(RoomController.class)
@AutoConfigureMockMvc(addFilters = false)
class RoomControllerTest {

    @MockitoBean
    private RoomService roomService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findRoomByID() throws Exception {
    }

    @Test
    void createRoom() throws Exception {

        RoomCreateDTO createDTO = RoomCreateDTO.builder()
                .roomTitle("Room title")
                .roomDescription("Test desc")
                .price(BigDecimal.valueOf(150))
                .build();

        RoomResponseDTO response = RoomResponseDTO.builder()
                .hotelId(1L)
                .roomTitle(createDTO.getRoomTitle())
                .roomDescription(createDTO.getRoomDescription())
                .price(createDTO.getPrice())
                .build();

        when(roomService.createRoom(anyLong(), any()))
                .thenReturn(response);

        mockMvc.perform(post("/hotels/1/rooms/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roomTitle").value(response.getRoomTitle()))
                .andExpect(jsonPath("$.roomDescription").value(response.getRoomDescription()))
                .andExpect(jsonPath("$.price").value(response.getPrice()));
    }

    @Test
    void updateRoomByID() throws Exception {

        RoomUpdateDTO updateDTO = RoomUpdateDTO.builder()
                .price(BigDecimal.valueOf(800))
                .roomTitle("New Title")
                .roomDescription("New desc")
                .build();

        RoomResponseDTO response = RoomResponseDTO.builder()
                .hotelId(1L)
                .roomTitle(updateDTO.getRoomTitle())
                .roomDescription(updateDTO.getRoomDescription())
                .price(updateDTO.getPrice())
                .build();

        when(roomService.updateRoomByID(anyLong(), any()))
                .thenReturn(response);

        mockMvc.perform(patch("/hotels/1/rooms/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomTitle").value(response.getRoomTitle()))
                .andExpect(jsonPath("$.roomDescription").value(response.getRoomDescription()))
                .andExpect(jsonPath("$.price").value(response.getPrice()));
    }

    @Test
    void deleteByID() throws Exception {
    }
}