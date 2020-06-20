package com.bookstore.controller;

import com.bookstore.dao.UserDao;
import com.bookstore.model.User;
import com.bookstore.security.AccountValidatorService;
import com.bookstore.service.TypeSearchConverterForUser;
import com.bookstore.service.TypeSortConverterForUser;
import com.bookstore.typeSearchForUser.TypeSearchAbstractForUser;
import com.bookstore.typeSortForUser.TypeSortForUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ActionsWithAccountsController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private TypeSearchConverterForUser typeSearchConverterForUser;


    @Autowired
    private TypeSortConverterForUser typeSortConverterForUser;

    @Autowired
    private AccountValidatorService accountValidatorService;

    @GetMapping("/actionsWithAccounts")
    public String getActionsWithSeller(Model model, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        model.addAttribute("user", userDao.findUserByNickname(authentication.getName()));

        return "actionsWithAccounts";
    }

    @PostMapping("/actionsWithAccounts")
    public String postActionsWithSeller(@RequestParam String text, @RequestParam String className1, @RequestParam String className2, Model model, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        TypeSearchAbstractForUser typeSearch = typeSearchConverterForUser.getTypeSearch(className1, className2);

        List<User> users = typeSearch.getUsers(text.toLowerCase());

        if (users.isEmpty()) {
            model.addAttribute("error", "accountsNotFound");
        } else {
            model.addAttribute("users", users);
        }

        model.addAttribute("user", userDao.findUserByNickname(authentication.getName()));

        return "actionsWithAccounts";
    }

    @PostMapping("/actionsWithAccounts/accounts")
    public String postAccounts(@RequestParam String selector, Model model, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        TypeSortForUser typeSortForUser = typeSortConverterForUser.getTypeSort(selector);

        List<User> users = typeSortForUser.getUsers();

        if (users.isEmpty()) {
            model.addAttribute("error", "accountsNotFound");
        } else {
            model.addAttribute("users", users);
        }

        model.addAttribute("user", userDao.findUserByNickname(authentication.getName()));

        return "accounts";
    }

    @GetMapping("/actionsWithAccounts/accounts")
    public String getAccounts(Model model, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        model.addAttribute("user", userDao.findUserByNickname(authentication.getName()));

        return "accounts";
    }


}
