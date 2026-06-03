package org.example.hotelapiproject.controller;

import org.example.hotelapiproject.dto.room_dto.RoomCreateDTO;
import org.example.hotelapiproject.dto.room_dto.RoomUpdateDTO;
import org.example.hotelapiproject.entity.Room;
import org.example.hotelapiproject.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotels/{hotel_id}/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/{room_id}")
    public Room findRoomByID(@PathVariable Long room_id) {
        return roomService.findRoomByID(room_id);
    }

    @PostMapping("/create")
    public ResponseEntity<Room> createRoom(@PathVariable Long hotel_id,
                                           @RequestBody RoomCreateDTO roomCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.createRoom(hotel_id, roomCreateDTO));
    }

    @PatchMapping("/{room_id}")
    public ResponseEntity<Room> updateRoomByID(@PathVariable Long room_id,
                                                @RequestBody RoomUpdateDTO roomUpdateDTO) {
        return ResponseEntity.ok(roomService.updateRoomByID(room_id, roomUpdateDTO));
    }

    @DeleteMapping("/{room_id}")
    public ResponseEntity<Room> deleteByID(@PathVariable Long room_id) {
        roomService.deleteRoomByID(room_id);
        return ResponseEntity.noContent().build();
    }
}
