package org.example.hotelapiproject.service;

import org.example.hotelapiproject.dto.card.CardHotelResponseDTO;
import org.example.hotelapiproject.dto.card.CardRoomResponseDTO;
import org.example.hotelapiproject.entity.*;
import org.example.hotelapiproject.exeption.account.AccountNotFoundException;
import org.example.hotelapiproject.exeption.hotel.HotelNotFoundException;
import org.example.hotelapiproject.exeption.room.RoomNotFoundException;
import org.example.hotelapiproject.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private FavoriteHotelRepository favoriteHotelRepository;

    @Mock
    private FavoriteRoomRepository favoriteRoomRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    @Test
    void favoriteHotelToggleToAdd() {

        Account account = Account.builder()
                .id(5L)
                .email("test@test.com")
                .build();

        authenticate("test@test.com");

        when(accountRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(account));

        Hotel hotel = new Hotel();
        hotel.setId(1L);

        when(hotelRepository.findById(1L))
                .thenReturn(Optional.of(hotel));

        when(favoriteHotelRepository.findByAccountAndHotel(account, hotel))
                .thenReturn(Optional.empty());

        boolean result = favoriteService.favoriteHotelToggle(hotel.getId());

        assertTrue(result);

        verify(favoriteHotelRepository)
                .save(any(FavoriteHotel.class));
    }

    @Test
    void favoriteHotelToggleToDelete() {

        Account account = Account.builder()
                .id(5L)
                .email("test@test.com")
                .build();

        authenticate("test@test.com");

        when(accountRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(account));

        Hotel hotel = new Hotel();
        hotel.setId(1L);

        FavoriteHotel favorite = FavoriteHotel.builder()
                .id(1L)
                .account(account)
                .hotel(hotel)
                .build();

        when(hotelRepository.findById(1L))
                .thenReturn(Optional.of(hotel));

        when(favoriteHotelRepository.findByAccountAndHotel(account, hotel))
                .thenReturn(Optional.of(favorite));

        boolean result = favoriteService.favoriteHotelToggle(hotel.getId());

        assertFalse(result);

        verify(favoriteHotelRepository)
                .delete(any(FavoriteHotel.class));
    }

    @Test
    void favoriteHotelToggleUserNotFound() {

        authenticate("test@test.com");

        when(accountRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(
                AccountNotFoundException.class,
                () -> favoriteService.favoriteHotelToggle(1L));

        assertEquals("User not found", exception.getMessage());

        verify(favoriteHotelRepository, never())
                .save(any(FavoriteHotel.class));
    }

    @Test
    void favoriteHotelToggleHotelNotFound() {

        Account account = Account.builder()
                .id(5L)
                .email("test@test.com")
                .build();

        authenticate("test@test.com");

        when(accountRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(account));

        when(hotelRepository.findById(1L))
                .thenReturn(Optional.empty());

        HotelNotFoundException exception = assertThrows(
                HotelNotFoundException.class,
                () -> favoriteService.favoriteHotelToggle(1L));

        assertEquals("Hotel not found", exception.getMessage());

        verify(favoriteHotelRepository, never())
                .save(any(FavoriteHotel.class));
    }

    @Test
    void getAllFavoriteHotels() {

        Account account = Account.builder()
                .id(5L)
                .email("test@test.com")
                .favoriteHotels(new ArrayList<>())
                .build();

        authenticate("test@test.com");

        when(accountRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(account));

        FavoriteHotel favorite = FavoriteHotel.builder()
                .account(account)
                .hotel(mockHotel("Hilton"))
                .build();

        when(favoriteHotelRepository.findAllByAccount(any(Account.class)))
                .thenReturn(List.of(favorite));

        List<CardHotelResponseDTO> result =
                favoriteService.getAllFavoriteHotels();

        assertEquals(1, result.size());

        CardHotelResponseDTO dto = result.get(0);

        assertEquals(10L, dto.id());
        assertEquals("Hilton", dto.name());
        assertEquals("Description", dto.description());

        assertEquals(
                BigDecimal.valueOf(300),
                dto.minAvailablePrice());

        assertTrue(dto.favorite());
    }

    @Test
    void getAllFavoriteHotelsUserNotFound() {

        authenticate("test@test.com");

        when(accountRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(
                AccountNotFoundException.class,
                () -> favoriteService.getAllFavoriteHotels());

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void favoriteRoomToggleToAdd() {

        Account account = Account.builder()
                .id(5L)
                .email("test@test.com")
                .build();

        authenticate("test@test.com");

        when(accountRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(account));

        Room room = new Room();
        room.setId(1L);

        when(roomRepository.findById(1L))
                .thenReturn(Optional.of(room));

        when(favoriteRoomRepository.findByAccountAndRoom(account, room))
                .thenReturn(Optional.empty());

        boolean result = favoriteService.favoriteRoomToggle(room.getId());

        assertTrue(result);

        verify(favoriteRoomRepository)
                .save(any(FavoriteRoom.class));
    }

    @Test
    void favoriteRoomToggleToDelete() {

        Account account = Account.builder()
                .id(5L)
                .email("test@test.com")
                .build();

        authenticate("test@test.com");

        when(accountRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(account));

        Room room = new Room();
        room.setId(1L);

        FavoriteRoom favorite = FavoriteRoom.builder()
                .id(1L)
                .account(account)
                .room(room)
                .build();

        when(roomRepository.findById(1L))
                .thenReturn(Optional.of(room));

        when(favoriteRoomRepository.findByAccountAndRoom(account, room))
                .thenReturn(Optional.of(favorite));

        boolean result = favoriteService.favoriteRoomToggle(room.getId());

        assertFalse(result);

        verify(favoriteRoomRepository)
                .delete(any(FavoriteRoom.class));
    }

    @Test
    void favoriteRoomToggleUserNotFound() {

        authenticate("test@test.com");

        when(accountRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(
                AccountNotFoundException.class,
                () -> favoriteService.favoriteRoomToggle(1L));

        assertEquals("User not found", exception.getMessage());

        verify(favoriteRoomRepository, never())
                .save(any(FavoriteRoom.class));
    }

    @Test
    void favoriteRoomToggleRoomNotFound() {

        Account account = Account.builder()
                .id(5L)
                .email("test@test.com")
                .build();

        authenticate("test@test.com");

        when(accountRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(account));

        when(roomRepository.findById(1L))
                .thenReturn(Optional.empty());

        RoomNotFoundException exception = assertThrows(
                RoomNotFoundException.class,
                () -> favoriteService.favoriteRoomToggle(1L));

        assertEquals("Room not found", exception.getMessage());

        verify(favoriteRoomRepository, never())
                .save(any(FavoriteRoom.class));
    }

    @Test
    void getAllFavoriteRooms() {

        Account account = Account.builder()
                .id(5L)
                .email("test@test.com")
                .favoriteHotels(new ArrayList<>())
                .build();

        authenticate("test@test.com");

        when(accountRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(account));

        FavoriteRoom favorite = FavoriteRoom.builder()
                .account(account)
                .room(mockRoom(BigDecimal.valueOf(250)))
                .build();

        when(favoriteRoomRepository.findAllByAccount(any(Account.class)))
                .thenReturn(List.of(favorite));

        List<CardRoomResponseDTO> result =
                favoriteService.getAllFavoriteRooms();

        assertEquals(1, result.size());

        CardRoomResponseDTO dto = result.get(0);

        assertEquals(
                BigDecimal.valueOf(250),
                dto.price());

        assertTrue(dto.favorite());
    }

    @Test
    void getAllFavoriteRoomsUserNotFound() {

        authenticate("test@test.com");

        when(accountRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(
                AccountNotFoundException.class,
                () -> favoriteService.getAllFavoriteRooms());

        assertEquals("User not found", exception.getMessage());
    }

    private void authenticate(String email) {
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(email, null);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    private Room mockRoom(BigDecimal price) {

        return Room.builder()
                .price(price)
                .build();
    }

    private Hotel mockHotel(String name) {

        return Hotel.builder()
                .id(10L)
                .name(name)
                .description("Description")
                .rooms(List.of(mockRoom(BigDecimal.valueOf(500)),
                        mockRoom(BigDecimal.valueOf(300))))
                .build();
    }
}