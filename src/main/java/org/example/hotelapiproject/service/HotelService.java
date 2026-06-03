package org.example.hotelapiproject.service;

import org.example.hotelapiproject.dto.hotel_dto.HotelCreateDTO;
import org.example.hotelapiproject.dto.hotel_dto.HotelUpdateDTO;
import org.example.hotelapiproject.entity.Account;
import org.example.hotelapiproject.entity.Hotel;
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

    public Hotel create(HotelCreateDTO hotelCreateDTO) {
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

        return hotelRepository.save(hotel);
    }

    public Hotel findHotelByID(Long hotel_id) {
        return hotelRepository.getReferenceById(hotel_id);
    }

    public Hotel updateHotelByID(Long hotel_id, HotelUpdateDTO hotelUpdateDTO, Account account) {
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

        return hotelRepository.save(updateHotel);
    }

    public void deleteHotelByID(Long hotel_id, Account account) {

        if (!hotelRepository.existsById(hotel_id)) {
            throw new RuntimeException("Hotel not found");
        }

        hotelRepository.deleteById(hotel_id);
    }
}
