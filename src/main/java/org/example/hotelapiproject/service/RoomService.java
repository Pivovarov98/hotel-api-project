package org.example.hotelapiproject.service;

import org.example.hotelapiproject.dto.room_dto.RoomCreateDTO;
import org.example.hotelapiproject.dto.room_dto.RoomUpdateDTO;
import org.example.hotelapiproject.entity.Room;
import org.example.hotelapiproject.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public Room createRoom(RoomCreateDTO roomCreateDTO) {

        Room room = new Room();

        room.setRoomTitle(roomCreateDTO.getRoomTitle());
        room.setRoomDescription(roomCreateDTO.getRoomDescription());
        room.setPrice(roomCreateDTO.getPrice());
        room.setAvailable(true);

        return roomRepository.save(room);
    }

    public Room findRoomByID(Long room_id) {
        return roomRepository.getReferenceById(room_id);
    }

    public Room updateRoomByID(Long room_id, RoomUpdateDTO roomUpdateDTO) {
        Room updateRoom = roomRepository.getReferenceById(room_id);

        if (Objects.nonNull(roomUpdateDTO.getRoomTitle())) {
            updateRoom.setRoomTitle(roomUpdateDTO.getRoomTitle());
        }

        if (Objects.nonNull(roomUpdateDTO.getRoomDescription())) {
            updateRoom.setRoomDescription(roomUpdateDTO.getRoomDescription());
        }

        if (Objects.nonNull(roomUpdateDTO.getPrice())) {
            updateRoom.setPrice(roomUpdateDTO.getPrice());
        }

        return roomRepository.save(updateRoom);
    }

    public void deleteRoomByID(Long room_id) {
        if (!roomRepository.existsById(room_id)) {
            throw new RuntimeException("Room not found");
        }

        roomRepository.deleteById(room_id);
    }
}
