package org.example.hotelapiproject.controller;

import org.example.hotelapiproject.dto.hotel_dto.HotelCreateDTO;
import org.example.hotelapiproject.dto.hotel_dto.HotelUpdateDTO;
import org.example.hotelapiproject.entity.Account;
import org.example.hotelapiproject.entity.Hotel;
import org.example.hotelapiproject.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @PostMapping("/create")
    private ResponseEntity<Hotel> createHotel(@RequestBody HotelCreateDTO hotelCreateDTO,
                                              @AuthenticationPrincipal Account account) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.create(hotelCreateDTO, account));
    }

    @GetMapping("/{hotel_id}")
    private Hotel findHotelByID(@PathVariable Long hotel_id) {
        return hotelService.findHotelByID(hotel_id);
    }

    @PatchMapping("/update/{hotel_id}")
    private ResponseEntity<Hotel> updateHotelInfo(@PathVariable Long hotel_id,
                                                  @RequestBody HotelUpdateDTO hotelUpdateDTO) {
        return ResponseEntity.ok(hotelService.updateHotelByID(hotel_id, hotelUpdateDTO));
    }

    @DeleteMapping("/{hotel_id}")
    private ResponseEntity<Hotel> deleteHotelByID(@PathVariable Long hotel_id) {
        hotelService.deleteHotelByID(hotel_id);
        return ResponseEntity.noContent().build();
    }
}
