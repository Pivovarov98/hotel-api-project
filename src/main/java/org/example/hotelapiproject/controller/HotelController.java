package org.example.hotelapiproject.controller;

import org.example.hotelapiproject.dto.hotel_dto.HotelCreateDTO;
import org.example.hotelapiproject.dto.hotel_dto.HotelResponseDTO;
import org.example.hotelapiproject.dto.hotel_dto.HotelUpdateDTO;
import org.example.hotelapiproject.entity.Account;
import org.example.hotelapiproject.entity.Hotel;
import org.example.hotelapiproject.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('HOTEL_OWNER')")
    public ResponseEntity<HotelResponseDTO> createHotel(@RequestBody HotelCreateDTO hotelCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.create(hotelCreateDTO));
    }

    @GetMapping("/{hotel_id}")
    public HotelResponseDTO findHotelByID(@PathVariable Long hotel_id) {
        return hotelService.findHotelByID(hotel_id);
    }

    @PatchMapping("/update/{hotel_id}")
    @PreAuthorize("hasRole('HOTEL_OWNER')")
    public ResponseEntity<HotelResponseDTO> updateHotelInfo(@PathVariable Long hotel_id,
                                                  @RequestBody HotelUpdateDTO hotelUpdateDTO,
                                                  @AuthenticationPrincipal Account account) {
        return ResponseEntity.ok(hotelService.updateHotelByID(hotel_id, hotelUpdateDTO, account));
    }

    @DeleteMapping("/{hotel_id}")
    @PreAuthorize("hasAnyRole('HOTEL_OWNER', 'ADMIN')")
    public ResponseEntity<Hotel> deleteHotelByID(@PathVariable Long hotel_id,
                                                  @AuthenticationPrincipal Account account) {
        hotelService.deleteHotelByID(hotel_id, account);
        return ResponseEntity.noContent().build();
    }
}
