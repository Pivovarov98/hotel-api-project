package org.example.hotelapiproject.service;

import org.example.hotelapiproject.entity.Account;
import org.example.hotelapiproject.entity.FavoriteHotel;
import org.example.hotelapiproject.repository.FavoriteHotelRepository;
import org.example.hotelapiproject.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    FavoriteHotelRepository favoriteHotelRepository;

    public FavoriteHotel addFavoriteHotel(Long hotel_id, Account account) {
        FavoriteHotel newFavorite = new FavoriteHotel();

        newFavorite.setHotel(hotelRepository.getReferenceById(hotel_id));
        newFavorite.setAccount(account);

        return favoriteHotelRepository.save(newFavorite);
    }
}
