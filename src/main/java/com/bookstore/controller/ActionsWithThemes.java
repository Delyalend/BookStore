package com.bookstore.controller;

import com.bookstore.dao.BookDao;
import com.bookstore.dao.ThemeDao;
import com.bookstore.dao.UserDao;
import com.bookstore.model.Book;
import com.bookstore.model.Theme;
import com.bookstore.security.AccountValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ActionsWithThemes {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ThemeDao themeDao;

    @Autowired
    private BookDao bookDao;

    @Autowired
    private AccountValidatorService accountValidatorService;

    @GetMapping("/actionsWithThemes")
    public String getActionsWithThemes(Authentication authentication, Model model) {

        accountValidatorService.doValidation(authentication);

        model.addAttribute("user", userDao.findUserByNickname(authentication.getName()));

        return "actionsWithThemes";
    }

    @PostMapping("/actionsWithThemes/delete/{id}")
    public String deleteTheme(@PathVariable Long id, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        List<Book> books = bookDao.findBooksByThemeId(id);

        for (int i = 0; i < books.size(); i++) {
            List<Theme> themes = books.get(i).getThemes();

            if (!themes.isEmpty()) {
                bookDao.deleteThemeForBook(books.get(i).getId(), id);
            }

        }

        themeDao.deleteById(id);

        return "redirect:/actionsWithThemes";

    }


    @PostMapping("/actionsWithThemes/addTheme")
    public String addTheme(@RequestParam String text, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        themeDao.addTheme(Theme.builder().themeTitle(text).build());

        return "redirect:/actionsWithThemes";
    }

    @GetMapping("/actionsWithThemes/all")
    public String showAllThemes(Authentication authentication, Model model) {

        accountValidatorService.doValidation(authentication);

        List<Theme> themes = themeDao.findAll();

        if (!themes.isEmpty()) {
            model.addAttribute("themes", themes);
        } else {
            model.addAttribute("error", "true");
        }

        model.addAttribute("user", userDao.findUserByNickname(authentication.getName()));

        return "actionsWithThemes";
    }

    @PostMapping("/actionsWithThemes/changeInformation/{id}")
    public String changeAuthorInformation(@RequestParam String text, @PathVariable Long id, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        themeDao.updateTheme(id, text);

        return "redirect:/actionsWithThemes";
    }


}
