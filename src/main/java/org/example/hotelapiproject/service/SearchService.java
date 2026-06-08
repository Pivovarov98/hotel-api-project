package org.example.hotelapiproject.service;

import lombok.RequiredArgsConstructor;
import org.example.hotelapiproject.dto.card.CardHotelResponseDTO;
import org.example.hotelapiproject.dto.search_dto.HotelSearchDTO;
import org.example.hotelapiproject.entity.Account;
import org.example.hotelapiproject.entity.Hotel;
import org.example.hotelapiproject.entity.Room;
import org.example.hotelapiproject.repository.AccountRepository;
import org.example.hotelapiproject.repository.FavoriteHotelRepository;
import org.example.hotelapiproject.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FavoriteHotelRepository favoriteHotelRepository;

    public List<CardHotelResponseDTO> search(HotelSearchDTO dto) {
        List<Hotel> hotels = hotelRepository.findHotelByRoomPrise(dto.getMinPrice(), dto.getMaxPrice());

        Account account = accountRepository.findByEmail(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName()).orElseThrow(() -> new RuntimeException("User not found"));

        return hotels.stream()
                .map(hotel -> new CardHotelResponseDTO(
                        hotel.getId(),
                        hotel.getName(),
                        hotel.getDescription(),
                        hotel.getRooms()
                                .stream()
                                .map(Room::getPrice)
                                .min(BigDecimal::compareTo)
                                .orElse(BigDecimal.ZERO),
                        favoriteHotelRepository.existsByAccountAndHotel(account, hotel)
                ))
                .toList();
    }
}
