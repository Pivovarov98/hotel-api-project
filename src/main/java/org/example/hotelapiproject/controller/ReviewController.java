package org.example.hotelapiproject.controller;


import org.example.hotelapiproject.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotel/{hotel_id}/reviwe")
public class ReviewController {

    @Autowired
    ReviewService reviewService;
}
