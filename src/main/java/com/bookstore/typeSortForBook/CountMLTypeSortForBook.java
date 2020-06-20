package com.bookstore.typeSortForBook;

import com.bookstore.model.Book;

import java.util.List;

public class CountMLTypeSortForBook extends TypeSortAbstractForBook {
    @Override
    public List<Book> getBooks() {
        return bookDao.findBooksSortCountML();
    }
}
