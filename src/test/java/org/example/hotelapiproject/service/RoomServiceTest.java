package org.example.hotelapiproject.service;

import org.example.hotelapiproject.dto.room_dto.RoomCreateDTO;
import org.example.hotelapiproject.dto.room_dto.RoomResponseDTO;
import org.example.hotelapiproject.dto.room_dto.RoomUpdateDTO;
import org.example.hotelapiproject.entity.Hotel;
import org.example.hotelapiproject.entity.Room;
import org.example.hotelapiproject.repository.HotelRepository;
import org.example.hotelapiproject.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    @Test
    void createRoom() {

        RoomCreateDTO dto = RoomCreateDTO.builder()
                .price(BigDecimal.valueOf(400))
                .roomTitle("Test room title")
                .roomDescription("Test room description")
                .build();

        Hotel hotel = new Hotel();
        hotel.setId(1L);

        when(hotelRepository.getReferenceById(1L))
                .thenReturn(hotel);

        Room savedRoom = Room.builder()
                .price(dto.getPrice())
                .roomTitle(dto.getRoomTitle())
                .roomDescription(dto.getRoomDescription())
                .hotel(hotel)
                .build();

        when(roomRepository.save(any(Room.class)))
                .thenReturn(savedRoom);

        RoomResponseDTO responseDTO = roomService.createRoom(hotel.getId(), dto);

        assertEquals(BigDecimal.valueOf(400), responseDTO.getPrice());
        assertEquals("Test room title", responseDTO.getRoomTitle());
        assertEquals("Test room description", responseDTO.getRoomDescription());
    }

    @Test
    void findRoomByID() {

        Hotel hotel = new Hotel();
        hotel.setId(1L);

        Room room = Room.builder()
                .id(28L)
                .price(BigDecimal.valueOf(400))
                .roomTitle("Test room title")
                .roomDescription("Test room description")
                .hotel(hotel)
                .build();

        when(roomRepository.getReferenceById(anyLong()))
                .thenReturn(room);

        RoomResponseDTO responseDTO = roomService.findRoomByID(room.getId());

        assertEquals(BigDecimal.valueOf(400), responseDTO.getPrice());
        assertEquals("Test room title", responseDTO.getRoomTitle());
        assertEquals("Test room description", responseDTO.getRoomDescription());
    }

    @Test
    void updateRoomByID() {

        Hotel hotel = new Hotel();
        hotel.setId(1L);

        Room room = Room.builder()
                .id(30L)
                .price(BigDecimal.valueOf(400))
                .roomTitle("Test old room title")
                .roomDescription("Test old room description")
                .hotel(hotel)
                .build();

        RoomUpdateDTO dto = RoomUpdateDTO.builder()
                .price(BigDecimal.valueOf(500))
                .roomTitle("New room title")
                .roomDescription("New room description")
                .build();

        when(roomRepository.getReferenceById(anyLong()))
                .thenReturn(room);

        when(roomRepository.save(any(Room.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RoomResponseDTO responseDTO = roomService.updateRoomByID(room.getId(), dto);

        assertEquals(BigDecimal.valueOf(500), responseDTO.getPrice());
        assertEquals("New room title", responseDTO.getRoomTitle());
        assertEquals("New room description", responseDTO.getRoomDescription());
    }

    @Test
    void deleteRoomByID() {

        Hotel hotel = new Hotel();
        hotel.setId(1L);

        Room room = Room.builder()
                .id(30L)
                .price(BigDecimal.valueOf(400))
                .roomTitle("Test old room title")
                .roomDescription("Test old room description")
                .hotel(hotel)
                .build();

        when(roomRepository.existsById(anyLong()))
                .thenReturn(true);

        roomService.deleteRoomByID(room.getId());

        verify(roomRepository).deleteById(room.getId());
    }
}