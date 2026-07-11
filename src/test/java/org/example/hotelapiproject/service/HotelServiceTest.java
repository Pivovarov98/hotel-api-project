package org.example.hotelapiproject.service;

import org.example.hotelapiproject.dto.hotel_dto.HotelCreateDTO;
import org.example.hotelapiproject.dto.hotel_dto.HotelResponseDTO;
import org.example.hotelapiproject.dto.hotel_dto.HotelUpdateDTO;
import org.example.hotelapiproject.entity.Account;
import org.example.hotelapiproject.entity.Hotel;
import org.example.hotelapiproject.exeption.account.AccountNotFoundException;
import org.example.hotelapiproject.exeption.hotel.HotelNotFoundException;
import org.example.hotelapiproject.repository.AccountRepository;
import org.example.hotelapiproject.repository.HotelRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    private HotelService hotelService;

    @Test
    void create() {

        Account account = Account.builder()
                .id(3L)
                .email("test@test.com")
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken("test@test.com", null);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        securityContext.setAuthentication(authentication);

        SecurityContextHolder.setContext(securityContext);

        when(accountRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(account));

        HotelCreateDTO dto = HotelCreateDTO.builder()
                .name("Test hotel name")
                .description("Test hotel description")
                .latitude(BigDecimal.valueOf(25.8))
                .longitude(BigDecimal.valueOf(85.6))
                .build();

        Hotel hotel = Hotel.builder()
                .id(1L)
                .name(dto.getName())
                .description(dto.getDescription())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .rooms(new ArrayList<>())
                .reviews(new ArrayList<>())
                .build();

        when(hotelRepository.save(any(Hotel.class)))
                .thenReturn(hotel);

        HotelResponseDTO responseDTO = hotelService.create(dto);

        assertEquals("Test hotel name", responseDTO.getName());
        assertEquals("Test hotel description", responseDTO.getDescription());
        assertEquals(BigDecimal.valueOf(25.8), responseDTO.getLatitude());
        assertEquals(BigDecimal.valueOf(85.6), responseDTO.getLongitude());
    }

    @Test
    void userNotFoundCreateHotel() {

        Authentication authentication =
                new UsernamePasswordAuthenticationToken("test@test.com", null);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        securityContext.setAuthentication(authentication);

        SecurityContextHolder.setContext(securityContext);

        when(accountRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.empty());

        HotelCreateDTO dto = HotelCreateDTO.builder()
                .name("Test hotel name")
                .description("Test hotel description")
                .latitude(BigDecimal.valueOf(25.8))
                .longitude(BigDecimal.valueOf(85.6))
                .build();

        AccountNotFoundException exception = assertThrows(
                AccountNotFoundException.class,
                () -> hotelService.create(dto));

        assertEquals("User not found: ", exception.getMessage());

        verify(hotelRepository, never())
                .save(any(Hotel.class));
    }

    @Test
    void findHotelByID() {

        Hotel hotel = Hotel.builder()
                .id(1L)
                .name("Test find hotel name")
                .description("Test find hotel description")
                .latitude(BigDecimal.valueOf(25.8))
                .longitude(BigDecimal.valueOf(85.6))
                .rooms(new ArrayList<>())
                .reviews(new ArrayList<>())
                .build();

        when(hotelRepository.findById(anyLong()))
                .thenReturn(Optional.of(hotel));

        HotelResponseDTO responseDTO = hotelService.findHotelByID(1L);

        assertEquals("Test find hotel name", responseDTO.getName());
        assertEquals("Test find hotel description", responseDTO.getDescription());
        assertEquals(BigDecimal.valueOf(25.8), responseDTO.getLatitude());
        assertEquals(BigDecimal.valueOf(85.6), responseDTO.getLongitude());
    }

    @Test
    void hotelNotFoundFindHotel() {

        when(hotelRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        HotelNotFoundException exception = assertThrows(
                HotelNotFoundException.class,
                () -> hotelService.findHotelByID(28L));

        assertEquals("Hotel not found", exception.getMessage());
    }

    @Test
    void updateHotelByID() {

        Hotel hotel = Hotel.builder()
                .id(1L)
                .name("Test hotel name")
                .description("Test hotel description")
                .latitude(BigDecimal.valueOf(25.8))
                .longitude(BigDecimal.valueOf(85.6))
                .rooms(new ArrayList<>())
                .reviews(new ArrayList<>())
                .build();

        HotelUpdateDTO dto = HotelUpdateDTO.builder()
                .name("New test hotel name")
                .description("New test hotel description")
                .latitude(BigDecimal.valueOf(258.8))
                .longitude(BigDecimal.valueOf(186.6))
                .build();

        when(hotelRepository.findById(anyLong()))
                .thenReturn(Optional.of(hotel));

        when(hotelRepository.save(any(Hotel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        HotelResponseDTO responseDTO = hotelService.updateHotelByID(1L, dto);

        assertEquals("New test hotel name", responseDTO.getName());
        assertEquals("New test hotel description", responseDTO.getDescription());
        assertEquals(BigDecimal.valueOf(258.8), responseDTO.getLatitude());
        assertEquals(BigDecimal.valueOf(186.6), responseDTO.getLongitude());
    }

    @Test
    void hotelNotFoundUpdateHotel() {

        when(hotelRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        HotelUpdateDTO dto = HotelUpdateDTO.builder().build();

        HotelNotFoundException exception = assertThrows(
                HotelNotFoundException.class,
                () -> hotelService.updateHotelByID(28L, dto));

        assertEquals("Hotel not found", exception.getMessage());

        verify(hotelRepository, never())
                .save(any(Hotel.class));
    }

    @Test
    void deleteHotelByID() {

        Hotel hotel = Hotel.builder()
                .id(1L)
                .name("Test hotel name")
                .description("Test hotel description")
                .latitude(BigDecimal.valueOf(25.8))
                .longitude(BigDecimal.valueOf(85.6))
                .rooms(new ArrayList<>())
                .reviews(new ArrayList<>())
                .build();

        when(hotelRepository.findById(anyLong()))
                .thenReturn(Optional.of(hotel));

        hotelService.deleteHotelByID(hotel.getId());

        verify(hotelRepository).deleteById(hotel.getId());
    }

    @Test
    void hotelNotFoundDeleteHotel() {

        when(hotelRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        HotelNotFoundException exception = assertThrows(
                HotelNotFoundException.class,
                () -> hotelService.deleteHotelByID(28L));

        assertEquals("Hotel not found", exception.getMessage());

        verify(hotelRepository, never())
                .deleteById(anyLong());
    }
}