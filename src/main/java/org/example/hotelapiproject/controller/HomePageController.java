package org.example.hotelapiproject.controller;

import org.example.hotelapiproject.entity.HomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomePageController {

    @Autowired
    HomePageService homePageService;
}
