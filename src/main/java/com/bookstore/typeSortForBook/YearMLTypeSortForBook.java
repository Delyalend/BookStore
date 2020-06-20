package com.bookstore.typeSortForBook;

import com.bookstore.model.Book;

import java.util.List;

public class YearMLTypeSortForBook extends TypeSortAbstractForBook {
    @Override
    public List<Book> getBooks() {
        return bookDao.findBooksSortYearML();
    }
}
