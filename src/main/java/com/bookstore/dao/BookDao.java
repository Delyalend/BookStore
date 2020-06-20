package com.bookstore.dao;

import com.bookstore.model.Book;

import java.util.List;
import java.util.Map;

public interface BookDao {

    Book findBookById(Long id);

    List<Book> findBooksSortAuthorAZ();
    List<Book> findBooksSortAuthorAZWithTheme(String themeTitle);
    List<Book> findBooksSortAuthorZA();
    List<Book> findBooksSortAuthorZAWithTheme(String themeTitle);

    List<Book> findBooksSortYearLM();
    List<Book> findBooksSortYearLMWithTheme(String themeTitle);
    List<Book> findBooksSortYearML();
    List<Book> findBooksSortYearMLWithTheme(String themeTitle);

    List<Book> findBooksSortCountLM();
    List<Book> findBooksSortCountLMWithTheme(String themeTitle);
    List<Book> findBooksSortCountML();
    List<Book> findBooksSortCountMLWithTheme(String themeTitle);


    List<Book> findBooksByTitle(String title);
    List<Book> findBooksByAuthor(String name);
    List<Book> findBooksByTheme(String title);

    List<Book> findBooksByThemeId(Long themeId);


    public Map<Long, List<Long>> findAllBooksIdWithSaleId();
    public Map<Long, List<Long>> findBooksIdWithSaleIdInInterval(String from, String to);
    public Map<Long, List<Long>> findBooksIdWithSaleIdForDay(String day);

    public int getCountBooksInSale(Long saleId);

    List<Book> findBooksByAuthorId(Long id);

    void deleteAuthorForBook(Long bookId, Long authorId);


    void deleteThemeForBook(Long bookId, Long themeId);

    void updateFieldOfBook(Long bookId,String fieldName, Object value);

    Long createBook(Book book);

    void deleteBookById(Long bookId);
}
