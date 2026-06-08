package org.example.hotelapiproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.hotelapiproject.dto.card.CardHotelResponseDTO;
import org.example.hotelapiproject.dto.search_dto.HotelSearchDTO;
import org.example.hotelapiproject.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/hotels/search")
@RequiredArgsConstructor
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping
    public ResponseEntity<List<CardHotelResponseDTO>> searchHotels(@RequestParam BigDecimal minPrice,
                                                                   @RequestParam BigDecimal maxPrice) {
        HotelSearchDTO dto = new HotelSearchDTO();
        dto.setMinPrice(minPrice);
        dto.setMaxPrice(maxPrice);

        return ResponseEntity.ok(searchService.search(dto));
    }
}
