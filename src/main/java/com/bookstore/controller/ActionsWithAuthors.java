package com.bookstore.controller;

import com.bookstore.dao.AuthorDao;
import com.bookstore.dao.BookDao;
import com.bookstore.dao.UserDao;
import com.bookstore.model.Author;
import com.bookstore.model.Book;
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
public class ActionsWithAuthors {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthorDao authorDao;

    @Autowired
    private BookDao bookDao;

    @Autowired
    private AccountValidatorService accountValidatorService;

    @GetMapping("/actionsWithAuthors")
    public String getActionsWithAuthor(Authentication authentication, Model model) {

        model.addAttribute("user", userDao.findUserByNickname(authentication.getName()));

        return "actionsWithAuthors";
    }

    @PostMapping("/actionsWithAuthors/delete/{id}")
    public String deleteAuthor(@PathVariable Long id, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        List<Book> books = bookDao.findBooksByAuthorId(id);

        for (int i = 0; i < books.size(); i++) {
            List<Author> authors = books.get(i).getAuthors();

            if (!authors.isEmpty()) {
                bookDao.deleteAuthorForBook(books.get(i).getId(), id);
            }
        }

        authorDao.deleteById(id);

        return "redirect:/actionsWithAuthors";
    }


    @PostMapping("/actionsWithAuthors/addAuthor")
    public String addAuthor(@RequestParam String text, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        authorDao.addAuthor(Author.builder().fullName(text).build());

        return "redirect:/actionsWithAuthors";
    }

    @GetMapping("/actionsWithAuthors/all")
    public String showAllAuthors(Authentication authentication, Model model) {

        accountValidatorService.doValidation(authentication);

        List<Author> authors = authorDao.findAll();

        if (!authors.isEmpty()) {
            model.addAttribute("authors", authors);
        } else {
            model.addAttribute("error", "true");
        }

        model.addAttribute("user", userDao.findUserByNickname(authentication.getName()));

        return "actionsWithAuthors";
    }

    @PostMapping("/actionsWithAuthors/changeInformation/{id}")
    public String changeAuthorInformation(@RequestParam String text, @PathVariable Long id, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        authorDao.updateAuthor(id, text);

        return "redirect:/actionsWithAuthors";
    }

}
