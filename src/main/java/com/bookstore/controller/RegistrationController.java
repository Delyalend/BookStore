package com.bookstore.controller;

import com.bookstore.dao.UserDao;
import com.bookstore.dto.RegistrationForm;
import com.bookstore.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class RegistrationController {

    @Autowired
    private UserDao userDao;

    @GetMapping("/registration")
    public String getRegistration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String postRegistration(RegistrationForm registrationForm, Model model) {


        if (!registrationForm.getPassword().equals(registrationForm.getConfirm())) {
            model.addAttribute("error", "PasswordsDontMatch");
            return "registration";
        }

        User user = registrationForm.toUser(new BCryptPasswordEncoder());

        if (userDao.findUserByNickname(user.getNickname()) == null) {
            userDao.createUser(user);
            userDao.updateRoleByNickname(registrationForm.getRole(), user.getNickname());
            return "redirect:login";
        }

        model.addAttribute("error", "NicknameIsAlreadyUsed");

        return "registration";
    }
}
