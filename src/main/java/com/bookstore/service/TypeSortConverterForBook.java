package com.bookstore.service;

import com.bookstore.typeSortForBook.TypeSortAbstractForBook;

public interface TypeSortConverterForBook {
    TypeSortAbstractForBook getTypeSearch(String subjectSort, String theme);
}
