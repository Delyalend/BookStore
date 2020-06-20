package com.bookstore.controller;

import com.bookstore.dao.ThemeDao;
import com.bookstore.dao.UserDao;
import com.bookstore.model.Theme;
import com.bookstore.security.AccountValidatorService;
import com.bookstore.service.BookService;
import com.bookstore.service.TypeSortConverterForBook;
import com.bookstore.typeSortForBook.TypeSortAbstractForBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BooksController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private TypeSortConverterForBook typeSortConverterForBook;

    @Autowired
    private ThemeDao themeDao;

    @Autowired
    private AccountValidatorService accountValidatorService;

    @Autowired
    private BookService bookService;

    @GetMapping("/booksForSeller")
    public String getBooks(Authentication authentication, Model model) {

        accountValidatorService.doValidation(authentication);

        model.addAttribute("user", userDao.findUserByNickname(authentication.getName()));

        List<Theme> themes = new ArrayList<>();
        themes.add(Theme.builder().themeId(0L).themeTitle("Все тематики").build());
        themes.addAll(themeDao.findAll());

        model.addAttribute("themes", themes);

        return "booksForSeller";
    }

    @GetMapping("/booksForSeller/show")
    public String getBooksShow() {

        return "redirect:/books";
    }


    @PostMapping("/booksForSeller")
    public String postBooks(@RequestParam String sort_selector, @RequestParam String theme_selector, Authentication authentication, Model model) {

        accountValidatorService.doValidation(authentication);

        TypeSortAbstractForBook typeSort = typeSortConverterForBook.getTypeSearch(sort_selector, theme_selector);

        if (bookService.getBooksWithoutRepeats(typeSort.getBooks()) == null) {
            model.addAttribute("error", "true");
        } else {
            model.addAttribute("books", bookService.getBooksWithoutRepeats(typeSort.getBooks()));
        }


        model.addAttribute("user", userDao.findUserByNickname(authentication.getName()));

        List<Theme> themes = new ArrayList<>();
        themes.add(Theme.builder().themeId(0L).themeTitle("Все тематики").build());
        themes.addAll(themeDao.findAll());

        model.addAttribute("themes", themes);

        return "booksForSeller";
    }

}
