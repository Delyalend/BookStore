package com.bookstore.typeSortForBook;

import com.bookstore.dao.BookDao;
import com.bookstore.model.Book;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class TypeSortAbstractForBook {

    @Autowired
    protected BookDao bookDao;

    public abstract List<Book> getBooks();
}
