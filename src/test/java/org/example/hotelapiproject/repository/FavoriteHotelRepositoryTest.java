package org.example.hotelapiproject.repository;

import org.example.hotelapiproject.entity.Account;
import org.example.hotelapiproject.entity.FavoriteHotel;
import org.example.hotelapiproject.entity.Hotel;
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
class FavoriteHotelRepositoryTest {

    @Autowired
    private FavoriteHotelRepository favoriteHotelRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByAccountAndHotel() {

        Account owner = mockOwner();
        Account traveler = mockTraveler();
        Hotel hotel = mockHotel("Hotel name", owner);

        entityManager.persist(owner);
        entityManager.persist(traveler);
        entityManager.persist(hotel);

        FavoriteHotel favorite = FavoriteHotel.builder()
                .account(traveler)
                .hotel(hotel)
                .build();

        entityManager.persist(favorite);
        entityManager.flush();

        Optional<FavoriteHotel> result = favoriteHotelRepository.findByAccountAndHotel(traveler, hotel);

        assertTrue(result.isPresent());
        assertEquals(traveler.getId(), result.get().getAccount().getId());
        assertEquals(hotel.getId(), result.get().getHotel().getId());
    }

    @Test
    void existsByAccountAndHotel() {

        Account owner = mockOwner();
        Account traveler = mockTraveler();
        Hotel hotel = mockHotel("Hotel name", owner);

        entityManager.persist(owner);
        entityManager.persist(traveler);
        entityManager.persist(hotel);

        entityManager.persist(
                FavoriteHotel.builder()
                        .account(traveler)
                        .hotel(hotel)
                        .build());

        entityManager.flush();

        boolean exists = favoriteHotelRepository.existsByAccountAndHotel(traveler, hotel);

        assertTrue(exists);
    }

    @Test
    void existsByAccountAndHotelReturnFalse() {

        Account owner = mockOwner();
        Account traveler = mockTraveler();
        Hotel hotel = mockHotel("Hotel name", owner);

        entityManager.persist(owner);
        entityManager.persist(traveler);
        entityManager.persist(hotel);

        entityManager.flush();

        boolean exists = favoriteHotelRepository.existsByAccountAndHotel(traveler, hotel);

        assertFalse(exists);
    }

    @Test
    void findAllByAccount() {

        Account owner = mockOwner();
        Account traveler = mockTraveler();
        Hotel hotel1 = mockHotel("Hotel 1", owner);
        Hotel hotel2 = mockHotel("Hotel 2", owner);

        entityManager.persist(owner);
        entityManager.persist(traveler);
        entityManager.persist(hotel1);
        entityManager.persist(hotel2);

        entityManager.persist(
                FavoriteHotel.builder()
                        .account(traveler)
                        .hotel(hotel1)
                        .build());

        entityManager.persist(
                FavoriteHotel.builder()
                        .account(traveler)
                        .hotel(hotel2)
                        .build());

        entityManager.flush();

        List<FavoriteHotel> favorites = favoriteHotelRepository.findAllByAccount(traveler);

        assertEquals(2, favorites.size());
    }

    @Test
    void findAllByAccountReturnEmptyList() {

        Account traveler = mockTraveler();
        entityManager.persist(traveler);

        entityManager.flush();

        List<FavoriteHotel> favorites = favoriteHotelRepository.findAllByAccount(traveler);

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
}