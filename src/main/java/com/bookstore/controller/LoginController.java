package com.bookstore.controller;

import com.bookstore.dao.UserDao;
import com.bookstore.dto.LoginForm;
import com.bookstore.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    private UserDao userDao;

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String postLogin(LoginForm loginForm, Model model) {

        User userFromDb = userDao.findUserByNickname(loginForm.getNickname());

        if (userFromDb == null) {
            model.addAttribute("error", "UserNotExistsOrPasswordIncorrect");
            return "login";
        }

        User user = loginForm.toUser(new BCryptPasswordEncoder());

        if (userFromDb.getPassword().equals(user.getPassword())) {
            return "redirect:/";
        }

        model.addAttribute("error", "UserNotExistsOrPasswordIncorrect");

        return "login";
    }
}
