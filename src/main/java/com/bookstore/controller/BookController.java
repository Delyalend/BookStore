package com.bookstore.controller;


import com.bookstore.dao.BookDao;
import com.bookstore.dao.ThemeDao;
import com.bookstore.dao.UserDao;
import com.bookstore.model.Book;
import com.bookstore.model.Theme;
import com.bookstore.model.User;
import com.bookstore.security.AccountValidatorService;
import com.bookstore.service.BasketService;
import com.bookstore.service.TypeSearchConverterForBook;
import com.bookstore.typeSearchForBook.TypeSearchForBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BookController {

    @Autowired
    private BookDao bookDao;

    @Autowired
    private BasketService basketService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TypeSearchConverterForBook typeSearchConverterForBook;

    @Autowired
    private AccountValidatorService accountValidatorService;

    @Autowired
    private ThemeDao themeDao;

    @PostMapping("booksForSeller/findBookForSeller")
    public String findBook(@RequestParam String text, @RequestParam String className, Model model, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        TypeSearchForBook typeSearchForBook = typeSearchConverterForBook.getTypeSearch(className);

        List<Book> books = typeSearchForBook.getBooks(text.toLowerCase());

        if (!books.isEmpty()) {
            model.addAttribute("books", books);
        } else {
            model.addAttribute("error", "true");
        }

        model.addAttribute("user", userDao.findUserByNickname(authentication.getName()));


        return "searchedBookForSeller";
    }

    @GetMapping("/booksForSeller/{id}")
    public String getBook(@PathVariable Long id, Model model, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        Book book = bookDao.findBookById(id);

        model.addAttribute("book", book);

        model.addAttribute("user", userDao.findUserByNickname(authentication.getName()));

        return "bookForSeller";
    }


    @PostMapping("/booksForSeller/addToBasket/{bookId}")
    public String addBookToBasket(@PathVariable Long bookId, @RequestParam int count, Model model, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        User user = userDao.findUserByNickname(authentication.getName());

        if (bookDao.findBookById(bookId).getCount() >= count) {
            basketService.addBookToBasket(user.getUserId(), bookId, count);
        } else {
            model.addAttribute("error", "notEnoughBooks");
        }

        List<Theme> themes = new ArrayList<>();
        themes.add(Theme.builder().themeId(0L).themeTitle("Все тематики").build());
        themes.addAll(themeDao.findAll());

        model.addAttribute("themes", themes);
        model.addAttribute("user", user);

        return "booksForSeller";


    }

}
