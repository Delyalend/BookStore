package com.bookstore.service;

import com.bookstore.dao.BookDao;
import com.bookstore.dao.UserDao;
import com.bookstore.model.Basket;
import com.bookstore.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BasketServiceImpl implements BasketService {

    @Autowired
    private BookDao bookDao;

    @Autowired
    private UserDao userDao;

    private static Map<Long, Basket> basketId_Basket = new HashMap<>();

    public List<Book> getBooksFromBasket(Long basketId) {

        Basket basket = basketId_Basket.get(basketId);

        if (basket == null) {
            basket = Basket.builder()
                    .basketId(basketId)
                    .booksInBasket(new ArrayList<>())
                    .resultSum(new BigDecimal("0"))
                    .build();
        }

        return basket.getBooksInBasket();
    }


    public void addBookToBasket(Long basketId, Long bookId, int count) {
        Book book = bookDao.findBookById(bookId);
        book.setCountInBasket(count);

        Basket basket = basketId_Basket.get(basketId);

        if (basket == null) {
            List<Book> books = new ArrayList<>();
            books.add(book);
            basket = Basket.builder()
                    .basketId(basketId)
                    .resultSum(new BigDecimal("0"))
                    .booksInBasket(books)
                    .build();
        } else {
            basket.getBooksInBasket().add(book);
        }


        basket.resultSum = basket.resultSum.add(new BigDecimal(count).multiply(book.getPrice()));

        basketId_Basket.put(basketId, basket);
    }


    public void deleteBookFromBasket(Long basketId, Book book) {
        Basket basket = basketId_Basket.get(basketId);
        basket.getBooksInBasket().remove(book);
    }

    @Override
    public void clearBasket(Long basketId) {
        Basket basket = basketId_Basket.get(basketId);
        if (basket != null) {
            basket.getBooksInBasket().clear();
            basket.setResultSum(new BigDecimal("0"));
        }
    }

    @Override
    public BigDecimal getResultSum(Long basketId) {

        Basket basket = basketId_Basket.get(basketId);

        if (basket == null) {
            return new BigDecimal("0");
        }

        return basketId_Basket.get(basketId).resultSum;
    }

}
