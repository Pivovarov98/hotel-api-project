package org.example.hotelapiproject.repository;

import org.example.hotelapiproject.entity.Account;
import org.example.hotelapiproject.entity.FavoriteHotel;
import org.example.hotelapiproject.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteHotelRepository extends JpaRepository<FavoriteHotel, Long> {

    boolean existsByAccountAndHotel(
            Account account,
            Hotel hotel
    );

    void deleteByAccountAndHotel(
            Account account,
            Hotel hotel
    );

    List<FavoriteHotel> findAllByAccount(Account account);
}
