package org.example.hotelapiproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.hotelapiproject.dto.hotel_dto.HotelCreateDTO;
import org.example.hotelapiproject.dto.hotel_dto.HotelResponseDTO;
import org.example.hotelapiproject.service.HotelService;
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

@WebMvcTest(HotelController.class)
@AutoConfigureMockMvc(addFilters = false)
class HotelControllerTest {

    @MockitoBean
    private HotelService hotelService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createHotel() throws Exception {

        HotelCreateDTO createDTO = HotelCreateDTO.builder()
                .name("Star Hotel")
                .description("Test desc")
                .latitude(BigDecimal.valueOf(158.74))
                .longitude(BigDecimal.valueOf(845.98))
                .build();

        HotelResponseDTO response = HotelResponseDTO.builder()
                .name("Star Hotel")
                .description("Test desc")
                .latitude(BigDecimal.valueOf(158.74))
                .longitude(BigDecimal.valueOf(845.98))
                .build();

        when(hotelService.create(any()))
                .thenReturn(response);

        mockMvc.perform(post("/hotels/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andExpect(jsonPath("$.description").value(response.getDescription()))
                .andExpect(jsonPath("$.latitude").value(response.getLatitude()))
                .andExpect(jsonPath("$.longitude").value(response.getLongitude()));
    }

    @Test
    void findHotelByID() {
    }

    @Test
    void updateHotelInfo() {
    }

    @Test
    void deleteHotelByID() throws Exception {

        mockMvc.perform(delete("/hotels/9"))
                .andExpect(status().isNoContent());

        verify(hotelService).deleteHotelByID(9L);
    }
}