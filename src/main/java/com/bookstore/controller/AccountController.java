package com.bookstore.controller;


import com.bookstore.dao.UserDao;
import com.bookstore.model.User;
import com.bookstore.security.AccountValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController {

    @Autowired
    private AccountValidatorService accountValidatorService;

    @Autowired
    private UserDao userDao;

    @GetMapping("/accounts/{id}")
    public String getAccount(@PathVariable Long id, Authentication authentication, Model model) {

        accountValidatorService.doValidation(authentication);
        User account = userDao.findUserById(id);

        model.addAttribute("account", account);
        model.addAttribute(userDao.findUserByNickname(authentication.getName()));

        return "account";
    }

    @PostMapping("/accounts/delete/{id}")
    public String deleteAccount(@PathVariable Long id) {


        userDao.deleteUserById(id);

        return "redirect:/actionsWithAccounts";
    }

    @PostMapping("/accounts/ban/{id}")
    public String banAccount(@PathVariable Long id, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        userDao.banUserById(id);

        return "redirect:/accounts/{id}";
    }

    @PostMapping("/accounts/unban/{id}")
    public String unbanAccount(@PathVariable Long id, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        userDao.unbanUserById(id);

        return "redirect:/accounts/{id}";
    }

    @PostMapping("/accounts/firstname/{id}")
    public String updateFirstName(@PathVariable Long id, @RequestParam String firstname, Model model, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        userDao.updateUserColumnById("firstname", firstname, id);

        return "redirect:/accounts/{id}";
    }

    @PostMapping("/accounts/lastname/{id}")
    public String updateLastName(@PathVariable Long id, @RequestParam String lastname, Model model, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        userDao.updateUserColumnById("lastname", lastname, id);

        return "redirect:/accounts/{id}";
    }

    @PostMapping("/accounts/middlename/{id}")
    public String updateMiddleName(@PathVariable Long id, @RequestParam String middlename, Model model, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        userDao.updateUserColumnById("middlename", middlename, id);

        return "redirect:/accounts/{id}";
    }

    @PostMapping("/accounts/gender/{id}")
    public String updateGender(@PathVariable Long id, @RequestParam String gender, Model model, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        userDao.updateUserColumnById("gender", gender, id);

        return "redirect:/accounts/{id}";
    }

}
