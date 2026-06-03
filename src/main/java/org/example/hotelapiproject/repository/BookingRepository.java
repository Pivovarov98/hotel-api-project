package org.example.hotelapiproject.repository;

import org.example.hotelapiproject.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByAccountId(Long accountId);

    List<Booking> findAllByRoomId(Long roomId);

    boolean existsByRoomIdAndReserveFromLessThanAndReserveToGreaterThan(Long roomId,
                                                                        LocalDate reserveTo,
                                                                        LocalDate reserveFrom);
}
