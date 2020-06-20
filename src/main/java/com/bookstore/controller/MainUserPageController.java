package com.bookstore.controller;

import com.bookstore.dao.UserDao;
import com.bookstore.security.AccountValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainUserPageController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AccountValidatorService accountValidatorService;

    @GetMapping("/mainUserPage")
    public String getMainUserPage(Authentication authentication, Model model) {

        accountValidatorService.doValidation(authentication);

        model.addAttribute("user", userDao.findUserByNickname(authentication.getName()));

        return "mainUserPage";
    }

}
