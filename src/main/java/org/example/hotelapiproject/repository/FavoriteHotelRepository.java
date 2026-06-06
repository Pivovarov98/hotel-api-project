package org.example.hotelapiproject.repository;

import org.example.hotelapiproject.entity.Account;
import org.example.hotelapiproject.entity.FavoriteHotel;
import org.example.hotelapiproject.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteHotelRepository extends JpaRepository<FavoriteHotel, Long> {

    Optional<FavoriteHotel> findByAccountAndHotel(Account account, Hotel hotel);

    boolean existsByAccountAndHotel(Account account, Hotel hotel);

    List<FavoriteHotel> findAllByAccount(Account account);
}
