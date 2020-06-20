package com.bookstore.service;

import com.bookstore.model.Book;

import java.util.List;

public interface BookService {

    List<Book> getBooksWithoutRepeats(List<Book> books);


}
