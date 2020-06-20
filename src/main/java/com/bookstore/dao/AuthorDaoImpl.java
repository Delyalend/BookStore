package com.bookstore.dao;

import com.bookstore.model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;

@Component
public class AuthorDaoImpl implements AuthorDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //language=SQL
    private final String DELETE_BY_ID = "delete from author_db where id = ?";

    //language=SQL
    private final String ADD_AUTHOR = "insert into author_db(name) values (?)";

    //language=SQL
    private final String SELECT_ALL = "select * from author_db order by name";

    //language=SQL
    private final String UPDATE_AUTHOR = "update author_db set name = ? where id = ?";

    //language=SQL
    private final String SELECT_AUTHORS_BY_BOOK_ID = "select * from author_db a_db " +
            "left join book_author_db b_a_db on b_a_db.author_id = a_db.id " +
            "left join book_db b_db on b_a_db.book_id = b_db.id " +
            "where b_db.id = ?";
    //language=SQL
    private final String INSERT_INTO_BOOK_AUTHOR_DB = "insert into book_author_db values(?,?)";

    //language=SQL
    private final String DELETE_AUTHORS_FOR_BOOK_BY_BOOK_ID = "delete from book_author_db where book_id = ?";

    //language=SQL
    private final String SELECT_AUTHORS_BY_NAME = "select * from author_db where LOWER(name) like ? or LOWER(name) like ? or LOWER(name) like ?";

    private RowMapper<Author> ROW_MAPPER_TO_AUTHOR = (ResultSet resultSet, int rowNum) -> {
        return Author.builder()
                .authorId(resultSet.getLong("id"))
                .fullName(resultSet.getString("name"))
                .build();
    };

    @Override
    public void addAuthor(Author author) {
        jdbcTemplate.update(ADD_AUTHOR, author.getFullName());
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }

    @Override
    public void updateAuthor(Long id, String text) {
        jdbcTemplate.update(UPDATE_AUTHOR, text, id);
    }

    @Override
    public List<Author> findAll() {
        return jdbcTemplate.query(SELECT_ALL, ROW_MAPPER_TO_AUTHOR);
    }


    @Override
    public List<Author> findAuthorsByBookId(Long id) {
        return jdbcTemplate.query(SELECT_AUTHORS_BY_BOOK_ID, ROW_MAPPER_TO_AUTHOR, id);
    }


    @Override
    public void addAuthorToBook(Long authorId, Long bookId) {
        jdbcTemplate.update(INSERT_INTO_BOOK_AUTHOR_DB, bookId, authorId);
    }


    @Override
    public List<Author> getAuthorsByFullName(String name) {
        String namePre = "%" + name;
        String namePost = name + "%";
        String namePrePost = "%" + name + "%";
        return jdbcTemplate.query(SELECT_AUTHORS_BY_NAME, ROW_MAPPER_TO_AUTHOR, namePre, namePost, namePrePost);
    }


    @Override
    public void deleteAuthorsForBook(Long bookId) {
        jdbcTemplate.update(DELETE_AUTHORS_FOR_BOOK_BY_BOOK_ID, bookId);
    }

}
