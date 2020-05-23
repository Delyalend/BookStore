package com.bookstore.controller;

import com.bookstore.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private UserDao userDao;

    @GetMapping("/")
    public String getHomePage(Authentication authentication) {

        if(authentication!=null) {
            if(userDao.findRoleByNickname(authentication.getName()).getTitle().equals("ROLE_USER")) {
                return "redirect:mainUserPage";
            }
            else if(userDao.findRoleByNickname(authentication.getName()).getTitle().equals("ROLE_ADMIN")){
                return "redirect:mainAdminPage";
            }
        }
        return "/login";
    }
}
