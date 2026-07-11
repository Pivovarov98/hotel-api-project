package org.example.hotelapiproject.service;

import org.example.hotelapiproject.dto.booking_dto.BookingCreateDTO;
import org.example.hotelapiproject.dto.booking_dto.BookingResponseDTO;
import org.example.hotelapiproject.entity.Account;
import org.example.hotelapiproject.entity.Booking;
import org.example.hotelapiproject.entity.Room;
import org.example.hotelapiproject.entity.enums.BookingStatus;
import org.example.hotelapiproject.exeption.account.AccountNotFoundException;
import org.example.hotelapiproject.exeption.booking.BookNotFoundException;
import org.example.hotelapiproject.exeption.booking.InvalidBookingPeriodException;
import org.example.hotelapiproject.exeption.booking.RoomAlreadyBookedException;
import org.example.hotelapiproject.exeption.room.RoomNotFoundException;
import org.example.hotelapiproject.repository.AccountRepository;
import org.example.hotelapiproject.repository.BookingRepository;
import org.example.hotelapiproject.repository.RoomRepository;
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
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void createBooking() {

        Account account = Account.builder()
                .id(5L)
                .email("test@test.com")
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken("test@test.com", null);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        securityContext.setAuthentication(authentication);

        SecurityContextHolder.setContext(securityContext);

        when(accountRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(account));

        Room room = new Room();
        room.setId(7L);

        when(roomRepository.findById(room.getId()))
                .thenReturn(Optional.of(room));

        BookingCreateDTO dto = BookingCreateDTO.builder()
                .roomId(room.getId())
                .totalPrice(BigDecimal.valueOf(150))
                .reserveFrom(LocalDate.of(2026, 7, 10))
                .reserveTo(LocalDate.of(2026, 7, 15))
                .build();

        Booking book = Booking.builder()
                .account(account)
                .room(room)
                .reserveFrom(dto.getReserveFrom())
                .reserveTo(dto.getReserveTo())
                .totalPrice(dto.getTotalPrice())
                .status(BookingStatus.PENDING)
                .build();

        when(bookingRepository.existsByRoomIdAndReserveFromLessThanAndReserveToGreaterThan(
                room.getId(),
                dto.getReserveTo(),
                dto.getReserveFrom()
        )).thenReturn(false);

        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(book);

        BookingResponseDTO responseDTO = bookingService.createBooking(dto);

        assertEquals(7L, responseDTO.getRoomId());
        assertEquals(5L, responseDTO.getAccountId());
        assertEquals(LocalDate.of(2026, 7, 10), responseDTO.getReserveFrom());
        assertEquals(LocalDate.of(2026, 7, 15), responseDTO.getReserveTo());
    }

    @Test
    void invalidBookingPeriodCreateBooking() {

        Authentication authentication =
                new UsernamePasswordAuthenticationToken("test@test.com", null);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        securityContext.setAuthentication(authentication);

        SecurityContextHolder.setContext(securityContext);

        Room room = new Room();
        room.setId(7L);

        BookingCreateDTO dto = BookingCreateDTO.builder()
                .roomId(room.getId())
                .totalPrice(BigDecimal.valueOf(150))
                .reserveFrom(LocalDate.of(2026, 7, 20))
                .reserveTo(LocalDate.of(2026, 7, 15))
                .build();

        InvalidBookingPeriodException exception = assertThrows(
                InvalidBookingPeriodException.class,
                () -> bookingService.createBooking(dto));

        assertEquals("Invalid booking period", exception.getMessage());

        verify(bookingRepository, never())
                .save(any(Booking.class));
    }

    @Test
    void userNotFoundCreateBooking() {

        Authentication authentication =
                new UsernamePasswordAuthenticationToken("test1@test.com", null);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        securityContext.setAuthentication(authentication);

        SecurityContextHolder.setContext(securityContext);

        when(accountRepository.findByEmail("test1@test.com"))
                .thenReturn(Optional.empty());

        Room room = new Room();
        room.setId(7L);

        BookingCreateDTO dto = BookingCreateDTO.builder()
                .roomId(room.getId())
                .totalPrice(BigDecimal.valueOf(150))
                .reserveFrom(LocalDate.of(2026, 7, 10))
                .reserveTo(LocalDate.of(2026, 7, 15))
                .build();

        AccountNotFoundException exception = assertThrows(
                AccountNotFoundException.class,
                () -> bookingService.createBooking(dto));

        assertEquals("User not found: ", exception.getMessage());

        verify(bookingRepository, never())
                .save(any(Booking.class));
    }

    @Test
    void roomNotFoundCreateBooking() {

        Account account = Account.builder()
                .id(5L)
                .email("test@test.com")
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken("test@test.com", null);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        securityContext.setAuthentication(authentication);

        SecurityContextHolder.setContext(securityContext);

        when(accountRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(account));

        Room room = new Room();
        room.setId(7L);

        when(roomRepository.findById(room.getId()))
                .thenReturn(Optional.empty());

        BookingCreateDTO dto = BookingCreateDTO.builder()
                .roomId(room.getId())
                .totalPrice(BigDecimal.valueOf(150))
                .reserveFrom(LocalDate.of(2026, 7, 10))
                .reserveTo(LocalDate.of(2026, 7, 15))
                .build();

        RoomNotFoundException exception = assertThrows(
                RoomNotFoundException.class,
                () -> bookingService.createBooking(dto));

        assertEquals("Room not found", exception.getMessage());

        verify(bookingRepository, never())
                .save(any(Booking.class));
    }

    @Test
    void roomAlreadyBookedCreateBooking() {

        Account account = Account.builder()
                .id(5L)
                .email("test@test.com")
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken("test@test.com", null);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        securityContext.setAuthentication(authentication);

        SecurityContextHolder.setContext(securityContext);

        when(accountRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(account));

        Room room = new Room();
        room.setId(7L);

        when(roomRepository.findById(room.getId()))
                .thenReturn(Optional.of(room));

        BookingCreateDTO dto = BookingCreateDTO.builder()
                .roomId(room.getId())
                .totalPrice(BigDecimal.valueOf(150))
                .reserveFrom(LocalDate.of(2026, 7, 10))
                .reserveTo(LocalDate.of(2026, 7, 15))
                .build();

        when(bookingRepository.existsByRoomIdAndReserveFromLessThanAndReserveToGreaterThan(
                room.getId(),
                dto.getReserveTo(),
                dto.getReserveFrom()
        )).thenReturn(true);

        RoomAlreadyBookedException exception = assertThrows(
                RoomAlreadyBookedException.class,
                () -> bookingService.createBooking(dto));


        assertEquals("Room is already booked for these dates", exception.getMessage());

        verify(bookingRepository, never())
                .save(any(Booking.class));
    }

    @Test
    void deleteBookingByID() {

        Account account = Account.builder()
                .id(5L)
                .email("test@test.com")
                .build();

        Room room = new Room();
        room.setId(7L);

        Booking book = Booking.builder()
                .id(98L)
                .account(account)
                .room(room)
                .reserveFrom(LocalDate.of(2026, 7, 13))
                .reserveTo(LocalDate.of(2026, 7, 18))
                .totalPrice(BigDecimal.valueOf(250))
                .status(BookingStatus.PENDING)
                .build();

        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(book));

        bookingService.deleteBookingByID(book.getId());

        verify(bookingRepository).deleteById(book.getId());
    }

    @Test
    void bookNotFoundDeleteBooking() {

        Account account = Account.builder()
                .id(5L)
                .email("test@test.com")
                .build();

        Room room = new Room();
        room.setId(7L);

        Booking book = Booking.builder()
                .id(98L)
                .account(account)
                .room(room)
                .reserveFrom(LocalDate.of(2026, 7, 13))
                .reserveTo(LocalDate.of(2026, 7, 18))
                .totalPrice(BigDecimal.valueOf(250))
                .status(BookingStatus.PENDING)
                .build();

        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(
                BookNotFoundException.class,
                () -> bookingService.deleteBookingByID(book.getId()));

        assertEquals("Book not found", exception.getMessage());

        verify(bookingRepository, never())
                .deleteById(anyLong());
    }
}