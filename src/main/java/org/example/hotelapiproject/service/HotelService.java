package org.example.hotelapiproject.service;

import org.example.hotelapiproject.dto.hotel_dto.HotelCreateDTO;
import org.example.hotelapiproject.dto.hotel_dto.HotelResponseDTO;
import org.example.hotelapiproject.dto.hotel_dto.HotelUpdateDTO;
import org.example.hotelapiproject.dto.room_dto.RoomShortDTO;
import org.example.hotelapiproject.entity.Account;
import org.example.hotelapiproject.entity.Hotel;
import org.example.hotelapiproject.entity.Room;
import org.example.hotelapiproject.repository.AccountRepository;
import org.example.hotelapiproject.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    AccountRepository accountRepository;

    public HotelResponseDTO create(HotelCreateDTO hotelCreateDTO) {
        Hotel hotel = new Hotel();
        Account account = accountRepository.findByEmail(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName()).orElseThrow(() -> new RuntimeException("User not found: "));

        hotel.setName(hotelCreateDTO.getName());
        hotel.setDescription(hotelCreateDTO.getDescription());
        hotel.setLatitude(hotelCreateDTO.getLatitude());
        hotel.setLongitude(hotelCreateDTO.getLongitude());
        hotel.setAccount(account);

        return responseDTO(hotelRepository.save(hotel));
    }

    public HotelResponseDTO findHotelByID(Long hotel_id) {
        return responseDTO(hotelRepository.getReferenceById(hotel_id));
    }

    public HotelResponseDTO updateHotelByID(Long hotel_id, HotelUpdateDTO hotelUpdateDTO, Account account) {
        Hotel updateHotel = hotelRepository.getReferenceById(hotel_id);

        if (Objects.nonNull(hotelUpdateDTO.getName())) {
            updateHotel.setName(hotelUpdateDTO.getName());
        }

        if (Objects.nonNull(hotelUpdateDTO.getLatitude())) {
            updateHotel.setLatitude(hotelUpdateDTO.getLatitude());
        }

        if (Objects.nonNull(hotelUpdateDTO.getLongitude())) {
            updateHotel.setLongitude(hotelUpdateDTO.getLongitude());
        }

        return responseDTO(hotelRepository.save(updateHotel));
    }

    public void deleteHotelByID(Long hotel_id, Account account) {

        if (!hotelRepository.existsById(hotel_id)) {
            throw new RuntimeException("Hotel not found");
        }

        hotelRepository.deleteById(hotel_id);
    }

    private HotelResponseDTO responseDTO(Hotel hotel){
        HotelResponseDTO response = new HotelResponseDTO();

        response.setName(hotel.getName());
        response.setDescription(hotel.getDescription());
        response.setLatitude(hotel.getLatitude());
        response.setLongitude(hotel.getLongitude());
        response.setRooms(hotel.getRooms()
                .stream()
                .map(this::roomToShortDTO)
                .toList());

        return response;
    }

    private RoomShortDTO roomToShortDTO(Room room){
        return new RoomShortDTO(room.getId(), room.getPrice(), room.getRoomTitle());
    }
}
