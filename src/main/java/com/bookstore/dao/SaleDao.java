package com.bookstore.dao;

import com.bookstore.model.Book;
import com.bookstore.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface SaleDao {

    void saveReportOnSale(BigDecimal sum, User user, List<Book> booksSold);

    BigDecimal getProfitSaleBySaleId(Long saleId);


    String getDateBySaleId(Long saleId);

    void deleteSalesForBook(Long bookId);
}
