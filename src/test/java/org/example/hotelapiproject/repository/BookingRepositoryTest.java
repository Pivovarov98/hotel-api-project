package org.example.hotelapiproject.repository;

import org.example.hotelapiproject.entity.Account;
import org.example.hotelapiproject.entity.Booking;
import org.example.hotelapiproject.entity.Hotel;
import org.example.hotelapiproject.entity.Room;
import org.example.hotelapiproject.entity.enums.BookingStatus;
import org.example.hotelapiproject.entity.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void existsByRoomIdAndReserveFromLessThanAndReserveToGreaterThan() {

        Account hotelOwner = mockOwner();
        entityManager.persist(hotelOwner);

        Account traveler = mockTraveler();
        entityManager.persist(traveler);

        Hotel hotel = mockHotel(hotelOwner);
        entityManager.persist(hotel);

        Room room = mockRoom(hotel);
        entityManager.persist(room);

        Booking booking = mockBooking(room, traveler);
        entityManager.persist(booking);

        entityManager.flush();

        boolean existTrue = bookingRepository
                .existsByRoomIdAndReserveFromLessThanAndReserveToGreaterThan(
                        room.getId(),
                        LocalDate.of(2026, 7, 12),
                        LocalDate.of(2026, 7, 11)
                );

        boolean existTrueOneBefore = bookingRepository
                .existsByRoomIdAndReserveFromLessThanAndReserveToGreaterThan(
                        room.getId(),
                        LocalDate.of(2026, 7, 12),
                        LocalDate.of(2026, 7, 8)
                );

        boolean existTrueOneAfter = bookingRepository
                .existsByRoomIdAndReserveFromLessThanAndReserveToGreaterThan(
                        room.getId(),
                        LocalDate.of(2026, 7, 20),
                        LocalDate.of(2026, 7, 14)
                );

        boolean existTrueAllPeriod = bookingRepository
                .existsByRoomIdAndReserveFromLessThanAndReserveToGreaterThan(
                        room.getId(),
                        LocalDate.of(2026, 7, 20),
                        LocalDate.of(2026, 7, 5)
                );

        boolean existFalse = bookingRepository
                .existsByRoomIdAndReserveFromLessThanAndReserveToGreaterThan(
                        room.getId(),
                        LocalDate.of(2026, 7, 20),
                        LocalDate.of(2026, 7, 18)
                );

        assertTrue(existTrue);
        assertTrue(existTrueOneBefore);
        assertTrue(existTrueOneAfter);
        assertTrue(existTrueAllPeriod);
        assertFalse(existFalse);
    }

    private static Account mockTraveler() {
        return Account.builder()
                .email("traveler@test.com")
                .name("Egor")
                .surname("Pivo")
                .password("test123")
                .roles(Set.of(Role.ROLE_TRAVELER))
                .build();
    }

    private static Account mockOwner() {
        return Account.builder()
                .email("owner@test.com")
                .password("test123")
                .name("Egor")
                .surname("Pivo")
                .roles(Set.of(Role.ROLE_HOTEL_OWNER))
                .build();
    }

    private static Hotel mockHotel(Account hotelOwner) {
        return Hotel.builder()
                .name("Hotel name")
                .description("Description")
                .latitude(BigDecimal.valueOf(25.8))
                .longitude(BigDecimal.valueOf(35.8))
                .account(hotelOwner)
                .build();
    }

    private static Room mockRoom(Hotel hotel) {
        return Room.builder()
                .roomTitle("Title")
                .roomDescription("Description")
                .price(BigDecimal.valueOf(200))
                .isAvailable(true)
                .hotel(hotel)
                .build();
    }

    private static Booking mockBooking(Room room, Account traveler) {
        return Booking.builder()
                .room(room)
                .account(traveler)
                .reserveFrom(LocalDate.of(2026, 7, 10))
                .reserveTo(LocalDate.of(2026, 7, 15))
                .totalPrice(room.getPrice())
                .status(BookingStatus.PENDING)
                .build();
    }
}