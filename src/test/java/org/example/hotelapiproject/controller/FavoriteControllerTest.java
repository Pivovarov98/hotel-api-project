package org.example.hotelapiproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.hotelapiproject.service.FavoriteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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

    @Autowired
    private ObjectMapper objectMapper;

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
    }
}