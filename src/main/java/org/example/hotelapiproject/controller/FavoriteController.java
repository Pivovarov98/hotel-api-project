package org.example.hotelapiproject.controller;

import org.example.hotelapiproject.dto.card.CardHotelResponseDTO;
import org.example.hotelapiproject.dto.card.CardRoomResponseDTO;
import org.example.hotelapiproject.dto.favorite_dto.FavoriteHotelToggleResponseDTO;
import org.example.hotelapiproject.dto.favorite_dto.FavoriteRoomToggleResponseDTO;
import org.example.hotelapiproject.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    FavoriteService favoriteService;

    @PostMapping("/hotels/{hotel_id}")
    @PreAuthorize("hasRole('TRAVELER')")
    public ResponseEntity<FavoriteHotelToggleResponseDTO> favoriteHotelToggle(@PathVariable Long hotel_id) {
        boolean favorite = favoriteService.favoriteHotelToggle(hotel_id);
        return ResponseEntity.ok().body(new FavoriteHotelToggleResponseDTO(favorite));
    }

    @GetMapping("/hotels")
    @PreAuthorize("hasRole('TRAVELER')")
    public List<CardHotelResponseDTO> getFavoriteHotels() {
        return favoriteService.getAllFavoriteHotels();
    }

    @PostMapping("/rooms/{room_id}")
    @PreAuthorize("hasRole('TRAVELER')")
    public ResponseEntity<FavoriteRoomToggleResponseDTO> favoriteRoomToggle(@PathVariable Long room_id) {
        boolean favorite = favoriteService.favoriteRoomToggle(room_id);
        return ResponseEntity.ok().body(new FavoriteRoomToggleResponseDTO(favorite));
    }

    @GetMapping("/rooms")
    @PreAuthorize("hasRole('TRAVELER')")
    public List<CardRoomResponseDTO> getFavoriteRooms() {
        return favoriteService.getAllFavoriteRooms();
    }

}
