package com.bookstore.dao;

import com.bookstore.model.Author;

import java.util.List;

public interface AuthorDao {

    void addAuthor(Author author);

    void deleteById(Long id);

    void updateAuthor(Long id, String text);

    List<Author> findAll();

    List<Author> findAuthorsByBookId(Long id);

    void addAuthorToBook(Long authorId, Long bookId);

    List<Author> getAuthorsByFullName(String fullName);

    void deleteAuthorsForBook(Long bookId);
}
