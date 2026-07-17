package org.example.hotelapiproject.repository;

import org.example.hotelapiproject.entity.Account;
import org.example.hotelapiproject.entity.Hotel;
import org.example.hotelapiproject.entity.Room;
import org.example.hotelapiproject.entity.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class HotelRepositoryTest {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findHotelByRoomPrise() {

        Account owner = mockOwner();
        entityManager.persist(owner);

        Hotel hotel1 = mockHotel("Hotel One", owner);
        Hotel hotel2 = mockHotel("Hotel Two", owner);

        entityManager.persist(hotel1);
        entityManager.persist(hotel2);

        Room room1 = mockRoom("Cheap room", BigDecimal.valueOf(150), hotel1);
        Room room2 = mockRoom("Luxury room", BigDecimal.valueOf(500), hotel2);

        entityManager.persist(room1);
        entityManager.persist(room2);

        entityManager.flush();

        List<Hotel> result =
                hotelRepository.findHotelByRoomPrise(
                        BigDecimal.valueOf(100),
                        BigDecimal.valueOf(200));

        assertEquals(1, result.size());
        assertEquals("Hotel One", result.getFirst().getName());
    }

    @Test
    void findHotelByRoomPriceEmptyList() {

        Account owner = mockOwner();
        entityManager.persist(owner);

        Hotel hotel = mockHotel("Hotel", owner);
        entityManager.persist(hotel);

        Room room = mockRoom("Room", BigDecimal.valueOf(700), hotel);
        entityManager.persist(room);

        entityManager.flush();

        List<Hotel> result =
                hotelRepository.findHotelByRoomPrise(
                        BigDecimal.valueOf(100),
                        BigDecimal.valueOf(200));

        assertTrue(result.isEmpty());
    }

    @Test
    void findHotelByRoomPricedReturnHotelOnlyOnce() {

        Account owner = mockOwner();
        entityManager.persist(owner);

        Hotel hotel = mockHotel("Hotel", owner);
        entityManager.persist(hotel);

        entityManager.persist(mockRoom("Room 1", BigDecimal.valueOf(150), hotel));
        entityManager.persist(mockRoom("Room 2", BigDecimal.valueOf(180), hotel));

        entityManager.flush();

        List<Hotel> result =
                hotelRepository.findHotelByRoomPrise(
                        BigDecimal.valueOf(100),
                        BigDecimal.valueOf(200));

        assertEquals(1, result.size());
    }

    private static Account mockOwner() {
        return Account.builder()
                .email("owner@test.com")
                .password("12345")
                .name("Owner")
                .roles(Set.of(Role.ROLE_HOTEL_OWNER))
                .build();
    }

    private static Hotel mockHotel(String name, Account hotelOwner) {
        return Hotel.builder()
                .name(name)
                .description("Desc")
                .latitude(BigDecimal.ONE)
                .longitude(BigDecimal.ONE)
                .account(hotelOwner)
                .build();
    }

    private static Room mockRoom(String title, BigDecimal price, Hotel hotel) {
        return Room.builder()
                .roomTitle(title)
                .roomDescription("Desc")
                .price(price)
                .isAvailable(true)
                .hotel(hotel)
                .build();
    }
}