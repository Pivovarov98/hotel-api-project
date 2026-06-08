package org.example.hotelapiproject.service;

import jakarta.transaction.Transactional;
import org.example.hotelapiproject.dto.card.CardHotelResponseDTO;
import org.example.hotelapiproject.dto.card.CardRoomResponseDTO;
import org.example.hotelapiproject.entity.*;
import org.example.hotelapiproject.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class FavoriteService {

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    FavoriteHotelRepository favoriteHotelRepository;

    @Autowired
    FavoriteRoomRepository favoriteRoomRepository;

    @Transactional
    public boolean favoriteHotelToggle(Long hotel_id) {
        Account account = accountRepository.findByEmail(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName()).orElseThrow(() -> new RuntimeException("User not found"));

        Hotel hotel = hotelRepository.findById(hotel_id)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        Optional<FavoriteHotel> favorite = favoriteHotelRepository.findByAccountAndHotel(account, hotel);

        if (favorite.isPresent()) {
            favoriteHotelRepository.delete(favorite.get());
            return false;
        }

        FavoriteHotel newFavorite = new FavoriteHotel();
        newFavorite.setHotel(hotel);
        newFavorite.setAccount(account);

        favoriteHotelRepository.save(newFavorite);

        return true;
    }

    public List<CardHotelResponseDTO> getAllFavoriteHotels() {
        Account account = accountRepository.findByEmail(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName()).orElseThrow(() -> new RuntimeException("User not found"));

        return favoriteHotelRepository.findAllByAccount(account)
                .stream()
                .map(favoriteHotel -> new CardHotelResponseDTO(
                        favoriteHotel.getHotel().getId(),
                        favoriteHotel.getHotel().getName(),
                        favoriteHotel.getHotel().getDescription(),
                        favoriteHotel.getHotel().getRooms()
                                .stream()
                                .map(Room::getPrice)
                                .min(BigDecimal::compareTo)
                                .orElse(BigDecimal.ZERO),
                        true
                ))
                .toList();
    }

    @Transactional
    public boolean favoriteRoomToggle(Long room_id) {
        Account account = accountRepository.findByEmail(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName()).orElseThrow(() -> new RuntimeException("User not found"));

        Room room = roomRepository.findById(room_id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        Optional<FavoriteRoom> favorite = favoriteRoomRepository.findByAccountAndRoom(account, room);

        if (favorite.isPresent()) {
            favoriteRoomRepository.delete(favorite.get());
            return false;
        }

        FavoriteRoom newFavorite = new FavoriteRoom();
        newFavorite.setRoom(room);
        newFavorite.setAccount(account);

        favoriteRoomRepository.save(newFavorite);

        return true;
    }

    public List<CardRoomResponseDTO> getAllFavoriteRooms() {
        Account account = accountRepository.findByEmail(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName()).orElseThrow(() -> new RuntimeException("User not found"));

        return favoriteRoomRepository.findAllByAccount(account)
                .stream()
                .map(favoriteRoom -> new CardRoomResponseDTO(
                        favoriteRoom.getRoom().getId(),
                        favoriteRoom.getRoom().getRoomTitle(),
                        favoriteRoom.getRoom().getRoomDescription(),
                        favoriteRoom.getRoom().getPrice(),
                        true
                ))
                .toList();
    }
}
