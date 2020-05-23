package com.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainUserPageController {

    @GetMapping("/mainUserPage")
    public String getMainUserPage() {
        return "mainUserPage";
    }

}
