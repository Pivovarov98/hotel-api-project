package org.example.hotelapiproject.service;

import org.example.hotelapiproject.dto.room_dto.RoomCreateDTO;
import org.example.hotelapiproject.dto.room_dto.RoomResponseDTO;
import org.example.hotelapiproject.dto.room_dto.RoomUpdateDTO;
import org.example.hotelapiproject.entity.Hotel;
import org.example.hotelapiproject.entity.Room;
import org.example.hotelapiproject.exeption.hotel.HotelNotFoundException;
import org.example.hotelapiproject.exeption.room.RoomNotFoundException;
import org.example.hotelapiproject.repository.HotelRepository;
import org.example.hotelapiproject.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    HotelRepository hotelRepository;

    public RoomResponseDTO createRoom(Long hotel_id, RoomCreateDTO roomCreateDTO) {

        Hotel hotel = hotelRepository.findById(hotel_id)
                .orElseThrow(() -> new HotelNotFoundException("Hotel not found"));

        Room room = new Room();

        room.setRoomTitle(roomCreateDTO.getRoomTitle());
        room.setRoomDescription(roomCreateDTO.getRoomDescription());
        room.setPrice(roomCreateDTO.getPrice());
        room.setHotel(hotel);
        room.setAvailable(true);

        return responseDTO(roomRepository.save(room));
    }

    public RoomResponseDTO findRoomByID(Long room_id) {

        Room room = roomRepository.findById(room_id)
                .orElseThrow(() -> new RoomNotFoundException("Room not found"));

        return responseDTO(room);
    }

    public RoomResponseDTO updateRoomByID(Long room_id, RoomUpdateDTO roomUpdateDTO) {

        Room updateRoom = roomRepository.findById(room_id)
                .orElseThrow(() -> new RoomNotFoundException("Room not found"));

        if (Objects.nonNull(roomUpdateDTO.getRoomTitle())) {
            updateRoom.setRoomTitle(roomUpdateDTO.getRoomTitle());
        }

        if (Objects.nonNull(roomUpdateDTO.getRoomDescription())) {
            updateRoom.setRoomDescription(roomUpdateDTO.getRoomDescription());
        }

        if (Objects.nonNull(roomUpdateDTO.getPrice())) {
            updateRoom.setPrice(roomUpdateDTO.getPrice());
        }

        return responseDTO(roomRepository.save(updateRoom));
    }

    public void deleteRoomByID(Long room_id) {
        if (!roomRepository.existsById(room_id)) {
            throw new RoomNotFoundException("Room not found");
        }

        roomRepository.deleteById(room_id);
    }

    private RoomResponseDTO responseDTO(Room room){
        RoomResponseDTO response = new RoomResponseDTO();

        response.setHotelId(room.getHotel().getId());
        response.setPrice(room.getPrice());
        response.setRoomTitle(room.getRoomTitle());
        response.setRoomDescription(room.getRoomDescription());

        return response;
    }
}
