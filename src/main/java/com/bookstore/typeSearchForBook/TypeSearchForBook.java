package com.bookstore.typeSearchForBook;

import com.bookstore.model.Book;

import java.util.List;

public interface TypeSearchForBook {
    List<Book> getBooks(String text);
}
