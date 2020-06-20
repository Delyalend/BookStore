package com.bookstore.service;

import com.bookstore.model.Book;
import java.math.BigDecimal;
import java.util.List;

public interface BasketService {

    List<Book> getBooksFromBasket(Long basketId);

    void addBookToBasket(Long BasketId, Long bookId, int count);

    void clearBasket(Long BasketId);

    BigDecimal getResultSum(Long BasketId);
}
