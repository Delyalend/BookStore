package com.bookstore.service;

import com.bookstore.model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Override
    public List<Book> getBooksWithoutRepeats(List<Book> booksWithRepeats) {

        List<Book> books = new ArrayList<>();

        if (booksWithRepeats.isEmpty()) {
            return null;
        }
        books.add(booksWithRepeats.get(0));


        for (int i = 0; i < books.size(); i++) {
            for (int j = 0; j < booksWithRepeats.size(); j++) {
                if (!books.contains(booksWithRepeats.get(j))) {
                    books.add(booksWithRepeats.get(j));
                }
            }
        }

        return books;
    }
}
