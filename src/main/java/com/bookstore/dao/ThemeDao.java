package com.bookstore.dao;

import com.bookstore.model.Author;
import com.bookstore.model.Theme;

import java.util.List;

public interface ThemeDao {

    List<Theme> findAll();

    List<Theme> getThemesByBookId(Long bookId);

    void deleteById(Long themeId);

    void addTheme(Theme theme);

    void updateTheme(Long themeId,String text);

    void deleteThemeForBook(Long bookId, Long themeId);

    void addThemeToBook(Long themeId, Long bookId);

    void deleteThemesForBook(Long bookId);
}
