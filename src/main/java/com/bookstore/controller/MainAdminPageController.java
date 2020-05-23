package com.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainAdminPageController {


    @GetMapping("/mainAdminPage")
    public String getMainAdminPage() {
        return "mainAdminPage";
    }

}