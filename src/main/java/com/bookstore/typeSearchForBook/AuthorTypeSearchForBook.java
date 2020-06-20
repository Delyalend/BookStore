package com.bookstore.typeSearchForBook;

import com.bookstore.dao.BookDao;
import com.bookstore.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorTypeSearchForBook implements TypeSearchForBook {

    @Autowired
    private BookDao bookDao;

    @Override
    public List<Book> getBooks(String text) {
        return bookDao.findBooksByAuthor(text);
    }
}
