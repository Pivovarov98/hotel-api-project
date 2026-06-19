package org.example.hotelapiproject.controller;

import org.example.hotelapiproject.dto.card.CardHotelResponseDTO;
import org.example.hotelapiproject.service.HomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class HomePageController {

    @Autowired
    HomePageService homePageService;


    @GetMapping
    ResponseEntity<List<CardHotelResponseDTO>> getAllHotels(){
        return ResponseEntity.ok(homePageService.findAllHotels());
    }
}
