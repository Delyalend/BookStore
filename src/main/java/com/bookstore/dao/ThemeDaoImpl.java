package com.bookstore.dao;

import com.bookstore.model.Theme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Component
public class ThemeDaoImpl implements ThemeDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    //language=SQL
    private String SELECT_ALL = "select * from theme_db";

    //language=SQL
    private String SELECT_THEMES_BY_BOOK_ID = "select t_db.id, t_db.title from theme_db t_db " +
            "left join book_theme_db b_t_db on b_t_db.theme_id = t_db.id " +
            "left join book_db b_db on b_t_db.book_id = b_db.id " +
            "where b_db.id = ?";

    //language=SQL
    private final String DELETE_THEME_BY_ID = "delete from theme_db where id = ?";

    //language=SQL
    private final String INSERT_INTO_THEME_DB = "insert into theme_db(title) values(?)";

    //language=SQL
    private final String UPDATE_THEME_BY_ID = "update theme_db set title = ? where id = ?";

    //language=SQL
    private final String DELETE_THEME_FOR_BOOK_BY_BOOK_ID = "delete from book_theme_db where book_id = ? and theme_id = ?";

    //language=SQL
    private final String INSERT_INTO_BOOK_THEME_DB = "insert into book_theme_db values(?, ?)";

    //language=SQL
    private final String DELETE_THEMES_FOR_BOOK_BY_BOOK_ID = "delete from book_theme_db where book_id = ?";

    private RowMapper<Theme> ROW_MAPPER_TO_LIST = (ResultSet resultSet, int rowNum) -> {
        return Theme.builder()
                .themeId(resultSet.getLong("id"))
                .themeTitle(resultSet.getString("title"))
                .build();
    };

    private RowMapper<List<Theme>> ROW_MAPPER_TO_LIST_THEME = (ResultSet resultSet, int rowNum) -> {
        List<Theme> themes = new ArrayList<>();
        do {
            Theme theme = Theme.builder()
                    .themeId(resultSet.getLong("id"))
                    .themeTitle(resultSet.getString("title"))
                    .build();
            themes.add(theme);
        } while (resultSet.next());

        return themes;
    };

    @Override
    public List<Theme> findAll() {
        return jdbcTemplate.query(SELECT_ALL, ROW_MAPPER_TO_LIST);
    }

    @Override
    public List<Theme> getThemesByBookId(Long bookId) {
        List<List<Theme>> themes = jdbcTemplate.query(SELECT_THEMES_BY_BOOK_ID, ROW_MAPPER_TO_LIST_THEME, bookId);
        if (themes.isEmpty()) {
            return null;
        }
        return themes.get(0);
    }


    @Override
    public void deleteById(Long themeId) {
        jdbcTemplate.update(DELETE_THEME_BY_ID, themeId);
    }


    @Override
    public void addTheme(Theme theme) {
        jdbcTemplate.update(INSERT_INTO_THEME_DB, theme.getThemeTitle());
    }


    @Override
    public void updateTheme(Long themeId, String text) {
        jdbcTemplate.update(UPDATE_THEME_BY_ID, text, themeId);
    }


    @Override
    public void deleteThemeForBook(Long bookId, Long themeId) {
        jdbcTemplate.update(DELETE_THEME_FOR_BOOK_BY_BOOK_ID, bookId, themeId);
    }


    @Override
    public void addThemeToBook(Long themeId, Long bookId) {
        jdbcTemplate.update(INSERT_INTO_BOOK_THEME_DB, bookId, themeId);
    }


    @Override
    public void deleteThemesForBook(Long bookId) {
        jdbcTemplate.update(DELETE_THEMES_FOR_BOOK_BY_BOOK_ID, bookId);
    }


}
