package org.example.hotelapiproject.controller;

import org.example.hotelapiproject.dto.card.CardHotelResponseDTO;
import org.example.hotelapiproject.dto.card.CardRoomResponseDTO;
import org.example.hotelapiproject.service.FavoriteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(FavoriteController.class)
@AutoConfigureMockMvc(addFilters = false)
class FavoriteControllerTest {

    @MockitoBean
    private FavoriteService favoriteService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void favoriteHotelToggleAddFavorite() throws Exception{

        when(favoriteService.favoriteHotelToggle(anyLong()))
                .thenReturn(true);

        mockMvc.perform(post("/favorite/hotels/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.favorite").value(true));

        verify(favoriteService).favoriteHotelToggle(5L);
    }

    @Test
    void favoriteHotelToggleRemoveFavorite() throws Exception{

        when(favoriteService.favoriteHotelToggle(anyLong()))
                .thenReturn(false);

        mockMvc.perform(post("/favorite/hotels/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.favorite").value(false));

        verify(favoriteService).favoriteHotelToggle(5L);
    }

    @Test
    void getFavoriteHotels() throws Exception{

        List<CardHotelResponseDTO> response = List.of(
                new CardHotelResponseDTO(
                        1L,
                        "Hilton",
                        "Luxury hotel",
                        BigDecimal.valueOf(250),
                        true
                ),
                new CardHotelResponseDTO(
                        2L,
                        "Marriott",
                        "Nice hotel",
                        BigDecimal.valueOf(180),
                        true
                )
        );

        when(favoriteService.getAllFavoriteHotels())
                .thenReturn(response);

        mockMvc.perform(get("/favorite/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))

                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Hilton"))
                .andExpect(jsonPath("$[0].description").value("Luxury hotel"))
                .andExpect(jsonPath("$[0].minAvailablePrice").value(250))
                .andExpect(jsonPath("$[0].favorite").value(true))

                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Marriott"))
                .andExpect(jsonPath("$[1].description").value("Nice hotel"))
                .andExpect(jsonPath("$[1].minAvailablePrice").value(180))
                .andExpect(jsonPath("$[1].favorite").value(true));

        verify(favoriteService).getAllFavoriteHotels();
    }

    @Test
    void getFavoriteHotelsEmptyList() throws Exception{

        when(favoriteService.getAllFavoriteHotels())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/favorite/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(favoriteService).getAllFavoriteHotels();
    }

    @Test
    void favoriteRoomToggleAddFavorite() throws Exception{

        when(favoriteService.favoriteRoomToggle(anyLong()))
                .thenReturn(true);

        mockMvc.perform(post("/favorite/rooms/24"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.favorite").value(true));

        verify(favoriteService).favoriteRoomToggle(24L);
    }

    @Test
    void favoriteRoomToggleRemoveFavorite() throws Exception{

        when(favoriteService.favoriteRoomToggle(anyLong()))
                .thenReturn(false);

        mockMvc.perform(post("/favorite/rooms/24"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.favorite").value(false));

        verify(favoriteService).favoriteRoomToggle(24L);
    }

    @Test
    void getFavoriteRooms() throws Exception{

        List<CardRoomResponseDTO> response = List.of(
                new CardRoomResponseDTO(
                        4L,
                        "Premium room",
                        "Test description",
                        BigDecimal.valueOf(350),
                        true
                ),
                new CardRoomResponseDTO(
                        5L,
                        "Common room",
                        "Test desc 2",
                        BigDecimal.valueOf(100),
                        true
                )
        );

        when(favoriteService.getAllFavoriteRooms())
                .thenReturn(response);

        mockMvc.perform(get("/favorite/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))

                .andExpect(jsonPath("$[0].id").value(4))
                .andExpect(jsonPath("$[0].title").value("Premium room"))
                .andExpect(jsonPath("$[0].description").value("Test description"))
                .andExpect(jsonPath("$[0].price").value(350))
                .andExpect(jsonPath("$[0].favorite").value(true))

                .andExpect(jsonPath("$[1].id").value(5))
                .andExpect(jsonPath("$[1].title").value("Common room"))
                .andExpect(jsonPath("$[1].description").value("Test desc 2"))
                .andExpect(jsonPath("$[1].price").value(100))
                .andExpect(jsonPath("$[1].favorite").value(true));

        verify(favoriteService).getAllFavoriteRooms();
    }

    @Test
    void getFavoriteRoomsEmptyList() throws Exception{

        when(favoriteService.getAllFavoriteRooms())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/favorite/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(favoriteService).getAllFavoriteRooms();
    }
}