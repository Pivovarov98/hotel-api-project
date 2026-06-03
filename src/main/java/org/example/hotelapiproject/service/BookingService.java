package org.example.hotelapiproject.service;

import org.example.hotelapiproject.dto.booking_dto.BookingCreateDTO;
import org.example.hotelapiproject.dto.booking_dto.BookingResponseDTO;
import org.example.hotelapiproject.entity.Account;
import org.example.hotelapiproject.entity.Booking;
import org.example.hotelapiproject.entity.Room;
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

    public BookingResponseDTO createBooking(Long room_id, BookingCreateDTO dto){
        if (!dto.getReserveFrom().isBefore(dto.getReserveTo())){
            throw  new RuntimeException("invalid booking period");
        }

        Account account = accountRepository.findByEmail(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName()).orElseThrow(() -> new RuntimeException("User not found: "));

        Room room = roomRepository.findById(room_id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        boolean isRoomBusy =
                bookingRepository.existsByRoomIdAndReserveFromLessThanAndReserveToGreaterThan(
                        room.getId(),
                        dto.getReserveTo(),
                        dto.getReserveFrom());

        if (isRoomBusy) {
            throw new RuntimeException("Room is already booked for these dates");
        }

        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setAccount(account);
        booking.setReserveFrom(dto.getReserveFrom());
        booking.setReserveTo(dto.getReserveTo());

        return responseDTO(bookingRepository.save(booking));
    }

    public void deleteBookingByID(Long book_id){
        Booking booking = bookingRepository.findById(book_id).
                orElseThrow(() -> new RuntimeException("Book not find"));

        bookingRepository.deleteById(book_id);
    }

    private BookingResponseDTO responseDTO (Booking book){
        BookingResponseDTO response = new BookingResponseDTO();

        response.setId(book.getId());
        response.setRoomId(book.getRoom().getId());
        response.setAccountId(book.getAccount().getId());
        response.setReserveFrom(book.getReserveFrom());
        response.setReserveTo(book.getReserveTo());

        return response;
    }
}
