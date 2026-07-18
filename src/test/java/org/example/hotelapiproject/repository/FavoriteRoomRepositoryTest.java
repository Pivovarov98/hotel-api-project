package org.example.hotelapiproject.repository;

import org.example.hotelapiproject.entity.*;
import org.example.hotelapiproject.entity.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FavoriteRoomRepositoryTest {

    @Autowired
    private FavoriteRoomRepository favoriteRoomRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByAccountAndRoom() {

        Account owner = mockOwner();
        Account traveler = mockTraveler();
        Hotel hotel = mockHotel("Hotel name", owner);
        Room room = mockRoom("Room name", BigDecimal.valueOf(150), hotel);

        entityManager.persist(owner);
        entityManager.persist(traveler);
        entityManager.persist(hotel);
        entityManager.persist(room);

        FavoriteRoom favorite = FavoriteRoom.builder()
                .account(traveler)
                .room(room)
                .build();

        entityManager.persist(favorite);
        entityManager.flush();

        Optional<FavoriteRoom> result = favoriteRoomRepository.findByAccountAndRoom(traveler, room);

        assertTrue(result.isPresent());
        assertEquals(traveler.getId(), result.get().getAccount().getId());
        assertEquals(hotel.getId(), result.get().getRoom().getId());
    }

    @Test
    void existsByAccountAndRoom() {

        Account owner = mockOwner();
        Account traveler = mockTraveler();
        Hotel hotel = mockHotel("Hotel name", owner);
        Room room = mockRoom("Room name", BigDecimal.valueOf(150), hotel);

        entityManager.persist(owner);
        entityManager.persist(traveler);
        entityManager.persist(hotel);
        entityManager.persist(room);

        entityManager.persist(
                FavoriteRoom.builder()
                        .account(traveler)
                        .room(room)
                        .build());

        entityManager.flush();

        boolean exists = favoriteRoomRepository.existsByAccountAndRoom(traveler, room);

        assertTrue(exists);
    }

    @Test
    void existsByAccountAndRoomReturnFalse() {

        Account owner = mockOwner();
        Account traveler = mockTraveler();
        Hotel hotel = mockHotel("Hotel name", owner);
        Room room = mockRoom("Room name", BigDecimal.valueOf(150), hotel);

        entityManager.persist(owner);
        entityManager.persist(traveler);
        entityManager.persist(hotel);
        entityManager.persist(room);

        entityManager.flush();

        boolean exists = favoriteRoomRepository.existsByAccountAndRoom(traveler, room);

        assertFalse(exists);
    }

    @Test
    void findAllByAccount() {

        Account owner = mockOwner();
        Account traveler = mockTraveler();
        Hotel hotel = mockHotel("Hotel name", owner);
        Room room1 = mockRoom("Room 1", BigDecimal.valueOf(150), hotel);
        Room room2 = mockRoom("Room 2", BigDecimal.valueOf(250), hotel);

        entityManager.persist(owner);
        entityManager.persist(traveler);
        entityManager.persist(hotel);
        entityManager.persist(room1);
        entityManager.persist(room2);

        entityManager.persist(
                FavoriteRoom.builder()
                        .account(traveler)
                        .room(room1)
                        .build());

        entityManager.persist(
                FavoriteRoom.builder()
                        .account(traveler)
                        .room(room2)
                        .build());

        entityManager.flush();

        List<FavoriteRoom> favorites = favoriteRoomRepository.findAllByAccount(traveler);

        assertEquals(2, favorites.size());
    }

    @Test
    void findAllByAccountReturnEmptyList() {

        Account traveler = mockTraveler();
        entityManager.persist(traveler);

        entityManager.flush();

        List<FavoriteRoom> favorites = favoriteRoomRepository.findAllByAccount(traveler);

        assertTrue(favorites.isEmpty());
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

    private static Account mockTraveler() {
        return Account.builder()
                .email("test@test.com")
                .password("qwerty12345")
                .name("User")
                .roles(Set.of(Role.ROLE_TRAVELER))
                .build();
    }

    private static Hotel mockHotel(String name, Account owner) {
        return Hotel.builder()
                .name(name)
                .description("Description")
                .latitude(BigDecimal.valueOf(25.8))
                .longitude(BigDecimal.valueOf(35.8))
                .account(owner)
                .build();
    }

    private static Room mockRoom(String name, BigDecimal price, Hotel hotel) {
        return Room.builder()
                .roomTitle(name)
                .roomDescription("Description")
                .price(price)
                .isAvailable(true)
                .hotel(hotel)
                .build();
    }
}