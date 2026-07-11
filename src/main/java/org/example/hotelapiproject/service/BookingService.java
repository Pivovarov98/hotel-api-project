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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    RoomRepository roomRepository;

    public BookingResponseDTO createBooking(BookingCreateDTO dto){
        if (!dto.getReserveFrom().isBefore(dto.getReserveTo())){
            throw  new InvalidBookingPeriodException("Invalid booking period");
        }

        Account account = accountRepository.findByEmail(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName()).orElseThrow(() -> new AccountNotFoundException("User not found: "));

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RoomNotFoundException("Room not found"));

        boolean isRoomBusy =
                bookingRepository.existsByRoomIdAndReserveFromLessThanAndReserveToGreaterThan(
                        room.getId(),
                        dto.getReserveTo(),
                        dto.getReserveFrom());

        if (isRoomBusy) {
            throw new RoomAlreadyBookedException("Room is already booked for these dates");
        }

        Booking booking = Booking.builder()
                .account(account)
                .room(room)
                .reserveFrom(dto.getReserveFrom())
                .reserveTo(dto.getReserveTo())
                .totalPrice(dto.getTotalPrice())
                .status(BookingStatus.PENDING)
                .build();

        return responseDTO(bookingRepository.save(booking));
    }

    public void deleteBookingByID(Long book_id){
        Booking booking = bookingRepository.findById(book_id).
                orElseThrow(() -> new BookNotFoundException("Book not found"));

        bookingRepository.deleteById(book_id);
    }

    private BookingResponseDTO responseDTO (Booking book){
        return BookingResponseDTO.builder()
                .id(book.getId())
                .roomId(book.getRoom().getId())
                .accountId(book.getAccount().getId())
                .reserveFrom(book.getReserveFrom())
                .reserveTo(book.getReserveTo())
                .build();
    }
}
