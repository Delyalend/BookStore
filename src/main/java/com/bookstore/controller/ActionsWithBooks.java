package com.bookstore.controller;

import com.bookstore.dao.*;
import com.bookstore.dto.BookDto;
import com.bookstore.model.Author;
import com.bookstore.model.Book;
import com.bookstore.model.Theme;
import com.bookstore.security.AccountValidatorService;
import com.bookstore.service.BookService;
import com.bookstore.service.TypeSearchConverterForBook;
import com.bookstore.service.TypeSortConverterForBook;
import com.bookstore.typeSearchForBook.TypeSearchForBook;
import com.bookstore.typeSortForBook.TypeSortAbstractForBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ActionsWithBooks {

    @Autowired
    private AccountValidatorService accountValidatorService;

    @Autowired
    private TypeSearchConverterForBook typeSearchConverterForBook;

    @Autowired
    private UserDao userDao;

    @Autowired
    private BookDao bookDao;

    @Autowired
    private ThemeDao themeDao;

    @Autowired
    private TypeSortConverterForBook typeSortConverterForBook;

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorDao authorDao;

    @Autowired
    private SaleDao saleDao;

    @PostMapping("booksForAdmin/findBookForAdmin")
    public String findBook(@RequestParam String text, @RequestParam String className, Model model, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        TypeSearchForBook typeSearchForBook = typeSearchConverterForBook.getTypeSearch(className);

        List<Book> books = typeSearchForBook.getBooks(text.toLowerCase());

        model.addAttribute("user", userDao.findUserByNickname(authentication.getName()));

        if (!books.isEmpty()) {
            model.addAttribute("books", books);
        } else {
            model.addAttribute("error", "true");
        }

        return "searchedBookForAdmin";
    }


    @GetMapping("/actionsWithBooks")
    public String getMainUserPage(Authentication authentication, Model model) {

        accountValidatorService.doValidation(authentication);

        model.addAttribute("user", userDao.findUserByNickname(authentication.getName()));

        return "actionsWithBooks";
    }

    @GetMapping("/booksForAdmin/{id}")
    public String getBook(@PathVariable Long id, Authentication authentication, Model model) {

        accountValidatorService.doValidation(authentication);

        model.addAttribute("user", userDao.findUserByNickname(authentication.getName()));

        Book book = bookDao.findBookById(id);

        model.addAttribute("book", book);

        return "bookForAdmin";
    }

    @GetMapping("/booksForAdmin")
    public String getBooks(Authentication authentication, Model model) {

        accountValidatorService.doValidation(authentication);

        model.addAttribute("user", userDao.findUserByNickname(authentication.getName()));

        List<Theme> themes = new ArrayList<>();
        themes.add(Theme.builder().themeId(0L).themeTitle("Все тематики").build());
        themes.addAll(themeDao.findAll());

        model.addAttribute("themes", themes);

        return "booksForAdmin";
    }


    @PostMapping("/booksForAdmin")
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

        return "booksForAdmin";
    }


    @PostMapping("/books/{id1}/deleteAuthor/{id2}")
    public String deleteAuthorForBook(@PathVariable Long id1, @PathVariable Long id2, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        bookDao.deleteAuthorForBook(id1, id2);
        return "redirect:/booksForAdmin/{id1}";
    }

    @PostMapping("/books/{id1}/deleteTheme/{id2}")
    public String deleteThemeForBook(@PathVariable Long id1, @PathVariable Long id2) {
        themeDao.deleteThemeForBook(id1, id2);
        return "redirect:/booksForAdmin/{id1}";
    }

    @GetMapping("/actionsWithBooks/{bookId}/getThemes")
    public String getThemesForBook(@PathVariable Long bookId, Model model, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        List<Theme> themes = themeDao.findAll();

        List<Theme> themesOfBook = themeDao.getThemesByBookId(bookId);


        if (themesOfBook != null) {
            for (int i = 0; i < themesOfBook.size(); i++) {
                if (themes.contains(themesOfBook.get(i))) {
                    themes.remove(themesOfBook.get(i));
                }
            }
        }


        if (!themes.isEmpty()) {
            model.addAttribute("themes", themes);
        } else {
            model.addAttribute("error", "true");
        }

        Book book = bookDao.findBookById(bookId);

        model.addAttribute("book", book);

        model.addAttribute("user", userDao.findUserByNickname(authentication.getName()));

        return "bookForAdmin";
    }

    @PostMapping("/booksForAdmin/{id1}/addTheme")
    public String addThemeToBook(@PathVariable Long id1, @RequestParam Long theme_selector, Model model, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        themeDao.addThemeToBook(theme_selector, id1);

        return "redirect:/booksForAdmin/{id1}";
    }

    @GetMapping("/actionsWithBooks/{bookId}/getAllAuthors")
    public String getAllAuthorsForBook(@PathVariable Long bookId, Authentication authentication, Model model) {

        accountValidatorService.doValidation(authentication);

        Book book = bookDao.findBookById(bookId);
        List<Author> authorsOfBook = book.getAuthors();

        List<Author> authors = authorDao.findAll();

        if (authorsOfBook != null) {
            for (int i = 0; i < authorsOfBook.size(); i++) {
                if (authors.contains(authorsOfBook.get(i))) {
                    authors.remove(authorsOfBook.get(i));
                }
            }
        }

        if (!authors.isEmpty()) {
            model.addAttribute("authors", authors);
        } else {
            model.addAttribute("error", "true");
        }

        model.addAttribute("book", book);

        model.addAttribute("user", userDao.findUserByNickname(authentication.getName()));

        return "bookForAdmin";
    }


    @GetMapping("/actionsWithBooks/{bookId}/getAuthorsByName")
    public String getAuthorsByFullNameForBook(@PathVariable Long bookId, @RequestParam String name, Authentication authentication, Model model) {

        accountValidatorService.doValidation(authentication);

        Book book = bookDao.findBookById(bookId);
        List<Author> authorsOfBook = book.getAuthors();

        List<Author> authors = authorDao.getAuthorsByFullName(name.toLowerCase());

        if (authorsOfBook != null) {
            for (int i = 0; i < authorsOfBook.size(); i++) {
                if (authors.contains(authorsOfBook.get(i))) {
                    authors.remove(authorsOfBook.get(i));
                }
            }
        }

        if (!authors.isEmpty()) {
            model.addAttribute("authors", authors);
        } else {
            model.addAttribute("error", "true");
        }

        model.addAttribute("book", book);

        model.addAttribute("user", userDao.findUserByNickname(authentication.getName()));

        return "bookForAdmin";
    }


    @PostMapping("/actionsWithBooks/{bookId}/addAuthor")
    public String addAuthorToBook(@PathVariable Long bookId, @RequestParam Long author_selector, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        authorDao.addAuthorToBook(author_selector, bookId);

        return "redirect:/booksForAdmin/{bookId}";
    }

    @PostMapping("/actionsWithBooks/{bookId}/changeInformation/{nameField}")
    public String changeInformationForBook(@PathVariable Long bookId, @PathVariable String nameField, @RequestParam Object text, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        bookDao.updateFieldOfBook(bookId, nameField, text);

        return "redirect:/booksForAdmin/{bookId}";
    }

    @PostMapping("/actionsWithBooks/{bookId}/changeInformation/yearwriting")
    public String changeYearWritingForBook(@PathVariable Long bookId, @RequestParam Date yearwriting, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        bookDao.updateFieldOfBook(bookId, "yearwriting", yearwriting);

        return "redirect:/booksForAdmin/{bookId}";
    }

    @PostMapping("/actionsWithBooks/{bookId}/changeInformation/price")
    public String changeCostForBook(@PathVariable Long bookId, @RequestParam BigDecimal price, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        bookDao.updateFieldOfBook(bookId, "price", price);

        return "redirect:/booksForAdmin/{bookId}";
    }

    @PostMapping("/actionsWithBooks/{bookId}/changeInformation/setCount")
    public String setCountForBook(@PathVariable Long bookId, @RequestParam int count, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        bookDao.updateFieldOfBook(bookId, "count", count);
        return "redirect:/booksForAdmin/{bookId}";
    }

    @PostMapping("/actionsWithBooks/{bookId}/changeInformation/addCount")
    public String addCountForBook(@PathVariable Long bookId, @RequestParam int count, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        count += bookDao.findBookById(bookId).getCount();

        bookDao.updateFieldOfBook(bookId, "count", count);
        return "redirect:/booksForAdmin/{bookId}";
    }

    @PostMapping("/actionsWithBooks/{bookId}/changeInformation/edition")
    public String changeEditionForBook(@PathVariable Long bookId, @RequestParam int edition, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        bookDao.updateFieldOfBook(bookId, "edition", edition);
        return "redirect:/booksForAdmin/{bookId}";
    }

    @GetMapping("/actionsWithBooks/addBook")
    public String getAddBook(Model model, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        model.addAttribute("user", userDao.findUserByNickname(authentication.getName()));

        return "addingBook";
    }

    @PostMapping("/actionsWithBooks/addBook")
    public String postAddBook(BookDto bookDto, Model model, Authentication authentication) throws ParseException {

        accountValidatorService.doValidation(authentication);

        Book book = bookDto.toBook();

        Long id = bookDao.createBook(book);

        model.addAttribute("user", userDao.findUserByNickname(authentication.getName()));

        model.addAttribute("book", bookDao.findBookById(id));

        return "bookForAdmin";
    }

    @PostMapping("/actionsWithBooks/deleteBook/{id}")
    public String deleteBook(Model model, Authentication authentication, @PathVariable Long id) {

        accountValidatorService.doValidation(authentication);

        themeDao.deleteThemesForBook(id);
        authorDao.deleteAuthorsForBook(id);
        saleDao.deleteSalesForBook(id);
        bookDao.deleteBookById(id);

        return "redirect:/actionsWithBooks";
    }

}
