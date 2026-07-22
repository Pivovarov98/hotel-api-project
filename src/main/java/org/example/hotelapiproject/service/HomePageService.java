package org.example.hotelapiproject.service;

import org.example.hotelapiproject.dto.card.CardHotelResponseDTO;
import org.example.hotelapiproject.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomePageService {

    @Autowired
    private HotelRepository hotelRepository;

    public List<CardHotelResponseDTO> getAllHotels() {

    }
}
