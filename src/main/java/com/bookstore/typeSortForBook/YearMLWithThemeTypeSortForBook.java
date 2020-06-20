package com.bookstore.typeSortForBook;

import com.bookstore.model.Book;

import java.util.List;

public class YearMLWithThemeTypeSortForBook extends TypeSortWithThemeAbstractForBook {
    @Override
    public List<Book> getBooks() {
        return bookDao.findBooksSortYearMLWithTheme(titleTheme);
    }
}
