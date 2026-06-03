package org.example.hotelapiproject.controller;

import org.example.hotelapiproject.entity.Account;
import org.example.hotelapiproject.entity.FavoriteHotel;
import org.example.hotelapiproject.entity.Hotel;
import org.example.hotelapiproject.entity.Room;
import org.example.hotelapiproject.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    FavoriteService favoriteService;

    @PostMapping("/hotels/{hotel_id}")
    public ResponseEntity<FavoriteHotel> addFavoriteHotel(@PathVariable Long hotel_id,
                                                          @AuthenticationPrincipal Account account) {
        return ResponseEntity.ok().body(favoriteService.addFavoriteHotel(hotel_id, account));
    }

//    @GetMapping("/hotels")
//    public Set<Hotel> getFavoriteHotels() {
//        return favoriteService.getFavoriteHotels();
//    }
//
//    @DeleteMapping("/hotels/{hotel_id}")
//    public ResponseEntity<Account> deleteFavoriteHotel(@PathVariable Long hotel_id) {
//        favoriteService.deleteFavoriteHotel();
//        return ResponseEntity.noContent().build();
//    }
//
//    @PostMapping("/rooms/{room_id}")
//    public ResponseEntity<Account> addFavoriteRoom(@PathVariable Long room_id) {
//        return ResponseEntity.ok().body(favoriteService.addFavoriteRoom);
//    }
//
//    @GetMapping("/rooms")
//    public Set<Room> getFavoriteRooms() {
//        return favoriteService.getFavoriteRooms();
//    }
//
//    @DeleteMapping("/rooms/{room_id}")
//    public ResponseEntity<Account> deleteFavoriteRoom() {
//        favoriteService.deleteFavoriteRoom();
//        return ResponseEntity.noContent().build(favoriteService.deleteFavoriteRoom());
//    }
}
