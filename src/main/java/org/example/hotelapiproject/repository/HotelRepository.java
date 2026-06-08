package org.example.hotelapiproject.repository;

import org.example.hotelapiproject.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    @Query("""
            select distinct h
            from Hotel h
            join h.rooms r
            where r.price between :minPrice and :maxPrice
            """)
    List<Hotel> findHotelByRoomPrise(
            BigDecimal minPrice,
            BigDecimal maxPrice
    );
}
