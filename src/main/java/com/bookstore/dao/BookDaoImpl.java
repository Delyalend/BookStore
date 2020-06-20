package com.bookstore.dao;

import com.bookstore.model.Author;
import com.bookstore.model.Book;
import com.bookstore.model.Theme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BookDaoImpl implements BookDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ThemeDao themeDao;

    @Autowired
    private AuthorDao authorDao;

    //language=SQL
    private final String SELECT_BOOKS_SORT_AUTHOR_AZ = "select b_db.*,a_db.name as author " +
            "from book_db b_db " +
            "left join book_author_db b_a_db on b_a_db.book_id = b_db.id " +
            "left join author_db a_db on b_a_db.author_id = a_db.id " +
            "order by a_db.name";

    //language=SQL
    private final String SELECT_BOOKS_SORT_AUTHOR_AZ_WITH_THEME = "select b_db.*, a_db.name as author " +
            "from book_db b_db " +
            "left join book_author_db b_a_db on b_a_db.book_id = b_db.id " +
            "left join author_db a_db on b_a_db.author_id = a_db.id " +
            "left join book_theme_db b_t_db on b_t_db.book_id = b_db.id " +
            "left join theme_db t on b_t_db.theme_id = t.id " +
            "where t.title = ? " +
            "order by a_db.name";

    //language=SQL
    private final String SELECT_BOOKS_SORT_AUTHOR_ZA = "select b_db.*,a_db.name as author\n" +
            "from book_db b_db " +
            "left join book_author_db b_a_db on b_a_db.book_id = b_db.id " +
            "left join author_db a_db on b_a_db.author_id = a_db.id " +
            "order by a_db.name desc";

    //language=SQL
    private final String SELECT_BOOKS_SORT_AUTHOR_ZA_WITH_THEME = "select b_db.*, a_db.name as author " +
            "from book_db b_db " +
            "left join book_author_db b_a_db on b_a_db.book_id = b_db.id " +
            "left join author_db a_db on b_a_db.author_id = a_db.id " +
            "left join book_theme_db b_t_db on b_t_db.book_id = b_db.id " +
            "left join theme_db t on b_t_db.theme_id = t.id " +
            "where t.title = ? " +
            "order by a_db.name desc";

    //language=SQL
    private final String SELECT_BOOK_BY_ID = "select b_db.*, a_db.name as author " +
            "from book_db b_db " +
            "left join book_author_db b_a_db on b_a_db.book_id = b_db.id " +
            "left join author_db a_db on b_a_db.author_id = a_db.id " +
            "where b_db.id = ?";


    //language=SQL
    private final String SELECT_BOOKS_ID_WITH_SALE_ID = "select b_db.id as bookid, s_b_db.sale_id as saleId from book_db b_db " +
            "right join sale_book_db s_b_db on s_b_db.book_id = b_db.id";

    //language=SQL
    private final String SELECT_BOOKS_ID_WITH_SALE_ID_IN_INTERVAL = "select b_db.id as bookid, s_b_db.sale_id as saleId from book_db b_db " +
            "right join sale_book_db s_b_db on s_b_db.book_id = b_db.id " +
            "right join sale_db s_db on s_db.id = s_b_db.sale_id " +
            "where datesale between ?::date and ?::date;";

    //language=SQL
    private final String SELECT_BOOKS_ID_WITH_SALE_ID_FOR_DAY = "select b_db.id as bookid, s_b_db.sale_id as saleId from book_db b_db " +
            "right join sale_book_db s_b_db on s_b_db.book_id = b_db.id " +
            "right join sale_db s_db on s_db.id = s_b_db.sale_id " +
            "where datesale = ?::date";

    //language=SQL
    private final String SELECT_COUNT_BOOKS_IN_SALE = "select count from sale_book_db " +
            "left join sale_db on sale_db.id = sale_book_db.sale_id " +
            "where sale_db.id = ?";

    //language=SQL
    private final String SELECT_BOOKS_BY_TITLE = "select b_db.*, a_db.name as author from book_db b_db " +
            "left join book_author_db b_a_db on b_a_db.book_id = b_db.id " +
            "left join author_db a_db on b_a_db.author_id = a_db.id " +
            "where LOWER(b_db.title) like ? or LOWER(b_db.title) like ? or LOWER(b_db.title) like ?";

    //language=SQL
    private final String SELECT_BOOKS_BY_AUTHOR = "select b_db.*, a_db.name as author from book_db b_db " +
            "left join book_author_db b_a_db on b_a_db.book_id = b_db.id " +
            "left join author_db a_db on b_a_db.author_id = a_db.id " +
            "where LOWER(a_db.name) like ? or LOWER(a_db.name) like ? or LOWER(a_db.name) like ?";

    //language=SQL
    private final String SELECT_BOOKS_BY_THEME = "select b_db.*, a_db.name as author from book_db b_db " +
            "left join book_author_db b_a_db on b_a_db.book_id = b_db.id " +
            "left join author_db a_db on b_a_db.author_id = a_db.id " +
            "left join book_theme_db b_t_db on b_t_db.book_id = b_db.id " +
            "left join theme_db t_db on b_t_db.theme_id = t_db.id " +
            "where LOWER(t_db.title) like ? or LOWER(t_db.title) like ? or LOWER(t_db.title) like ?";

    //language=SQL
    private final String SELECT_BOOKS_SORT_YEAR_LM = "select b_db.*,a_db.name as author " +
            "from book_db b_db " +
            "left join book_author_db b_a_db on b_a_db.book_id = b_db.id " +
            "left join author_db a_db on b_a_db.author_id = a_db.id " +
            "order by b_db.yearwriting";

    //language=SQL
    private final String SELECT_BOOKS_SORT_YEAR_ML = "select b_db.*,a_db.name as author " +
            "from book_db b_db " +
            "left join book_author_db b_a_db on b_a_db.book_id = b_db.id " +
            "left join author_db a_db on b_a_db.author_id = a_db.id " +
            "order by b_db.yearwriting desc";

    //language=SQL
    private final String SELECT_BOOKS_SORT_COUNT_LM = "select b_db.*,a_db.name as author " +
            "from book_db b_db " +
            "left join book_author_db b_a_db on b_a_db.book_id = b_db.id " +
            "left join author_db a_db on b_a_db.author_id = a_db.id " +
            "order by b_db.count ";


    //language=SQL
    private final String SELECT_BOOKS_SORT_COUNT_LM_WITH_THEME = "select b_db.*,a_db.name as author " +
            "from book_db b_db " +
            "left join book_author_db b_a_db on b_a_db.book_id = b_db.id " +
            "left join author_db a_db on b_a_db.author_id = a_db.id " +
            "left join book_theme_db b_t_db on b_t_db.book_id = b_db.id " +
            "left join theme_db t on b_t_db.theme_id = t.id " +
            "where t.title = ? " +
            "order by b_db.count ";

    //language=SQL
    private final String SELECT_BOOKS_SORT_COUNT_ML = "select b_db.*,a_db.name as author " +
            "from book_db b_db " +
            "left join book_author_db b_a_db on b_a_db.book_id = b_db.id " +
            "left join author_db a_db on b_a_db.author_id = a_db.id " +
            "order by b_db.count desc";


    //language=SQL
    private final String SELECT_BOOKS_SORT_COUNT_ML_WITH_THEME = "select b_db.*,a_db.name as author " +
            "from book_db b_db " +
            "left join book_author_db b_a_db on b_a_db.book_id = b_db.id " +
            "left join author_db a_db on b_a_db.author_id = a_db.id " +
            "left join book_theme_db b_t_db on b_t_db.book_id = b_db.id " +
            "left join theme_db t on b_t_db.theme_id = t.id " +
            "where t.title = ?" +
            "order by b_db.count desc";


    //language=SQL
    private final String SELECT_BOOKS_SORT_YEAR_LM_WITH_THEME = "select b_db.*,a_db.name as author " +
            "from book_db b_db " +
            "left join book_author_db b_a_db on b_a_db.book_id = b_db.id " +
            "left join author_db a_db on b_a_db.author_id = a_db.id " +
            "left join book_theme_db b_t_db on b_t_db.book_id = b_db.id " +
            "left join theme_db t on b_t_db.theme_id = t.id " +
            "where t.title = ? " +
            "order by b_db.yearwriting";

    //language=SQL
    private final String SELECT_BOOKS_SORT_YEAR_ML_WITH_THEME = "select b_db.*,a_db.name as author " +
            "from book_db b_db " +
            "left join book_author_db b_a_db on b_a_db.book_id = b_db.id " +
            "left join author_db a_db on b_a_db.author_id = a_db.id " +
            "left join book_theme_db b_t_db on b_t_db.book_id = b_db.id " +
            "left join theme_db t on b_t_db.theme_id = t.id " +
            "where t.title = ? " +
            "order by b_db.yearwriting desc";

    //language=SQL
    private final String SELECT_BOOKS_BY_THEME_ID = "select * from book_db b_db " +
            "left join book_theme_db b_t_db on b_t_db.book_id = b_db.id " +
            "left join theme_db t_db on b_t_db.theme_id = t_db.id " +
            "where t_db.id = ?";
    //language=SQL
    private final String SELECT_ID_UNKNOWN_AUTHOR = "select id from author_db a_db " +
            "left join book_author_db b_a_db on b_a_db.author_id = a_db.id where a_db.name = ?";
    //language=SQL
    private final String INSERT_AUTHOR_FOR_BOOK = "insert into book_author_db values(?, ?)";
    //language=SQL
    private final String DELETE_FROM_BOOK_THEME_DB = "delete from book_theme_db where book_id = ? and theme_id = ?";
    //language=SQL
    private final String UPDATE_FIELD_BOOK = "update book_db set ";
    //language=SQL
    private final String DELETE_BOOK_BY_ID = "delete from book_db where id = ?";
    //language=SQL
    private final String INSERT_INTO_BOOK_DB = "insert into book_db (title,edition,description," +
            "publishinghouse,price,count,yearwriting) values " +
            "(?,?,?,?,?,?,?) returning id";
    //language=SQL
    private final String DELETE_AUTHOR_FOR_BOOK = "delete from book_author_db where book_id = ? and author_id = ?";
    //language=SQL
    private final String SELECT_BOOKS_BY_AUTHOR_ID = "select b_db.*, a_db.name as author from book_db b_db " +
            "left join book_author_db b_a_db on b_a_db.book_id = b_db.id " +
            "left join author_db a_db on b_a_db.author_id = a_db.id " +
            "where a_db.id = ?";
    //language=SQL
    private final String SELECT_COUNT_AUTHORS_FOR_BOOK_BY_ID = "select count(*) from author_db a_db " +
            "left join book_author_db b_a_db on b_a_db.author_id = a_db.id " +
            "left join book_db b_db on b_db.id = b_a_db.book_id " +
            "where b_db.id = ?";


    private RowMapper<Integer> ROW_MAPPER_TO_COUNT_BOOKS_IN_SALE = (ResultSet resultSet, int rowNum) -> {
        Integer count = 0;

        do {
            count += resultSet.getInt("count");
        } while (resultSet.next());

        return count;
    };

    private RowMapper<Map<Long, List<Long>>> ROW_MAPPER_TO_BOOKS_ID_WITH_SALE_ID = (ResultSet resultSet, int rowNum) -> {
        Map<Long, List<Long>> booksIdWithSaleId = new HashMap<>();

        do {
            Long saleId = resultSet.getLong("saleid");
            List<Long> booksId;
            if (booksIdWithSaleId.containsKey(saleId)) {
                booksId = booksIdWithSaleId.get(saleId);

            } else {
                booksId = new ArrayList<>();
            }

            booksId.add(resultSet.getLong("bookid"));
            booksIdWithSaleId.put(saleId, booksId);

        } while (resultSet.next());

        return booksIdWithSaleId;
    };

    private RowMapper<Book> ROW_MAPPER_TO_BOOK = (ResultSet resultSet, int rawNum) -> {

        Long bookId = resultSet.getLong("id");
        List<Theme> themes = themeDao.getThemesByBookId(bookId);
        List<Author> authors = authorDao.findAuthorsByBookId(bookId);

        return Book.builder()
                .id(bookId)
                .description(resultSet.getString("description"))
                .publishinghouse(resultSet.getString("publishinghouse"))
                .themes(themes)
                .edition(resultSet.getInt("edition"))
                .count(resultSet.getInt("count"))
                .authors(authors)
                .title(resultSet.getString("title"))
                .price(new BigDecimal((resultSet.getString("price").replace("р.", "")).replace(",", ".")))
                .year(resultSet.getDate("yearwriting"))
                .build();
    };


    @Override
    public Book findBookById(Long id) {
        return jdbcTemplate.query(SELECT_BOOK_BY_ID, ROW_MAPPER_TO_BOOK, id).get(0);
    }

    @Override
    public List<Book> findBooksSortAuthorAZ() {
        return jdbcTemplate.query(SELECT_BOOKS_SORT_AUTHOR_AZ, ROW_MAPPER_TO_BOOK);
    }

    @Override
    public List<Book> findBooksSortAuthorZA() {
        return jdbcTemplate.query(SELECT_BOOKS_SORT_AUTHOR_ZA, ROW_MAPPER_TO_BOOK);
    }

    @Override
    public List<Book> findBooksSortAuthorAZWithTheme(String themeTitle) {
        return jdbcTemplate.query(SELECT_BOOKS_SORT_AUTHOR_AZ_WITH_THEME, ROW_MAPPER_TO_BOOK, themeTitle);
    }

    @Override
    public List<Book> findBooksSortAuthorZAWithTheme(String themeTitle) {
        return jdbcTemplate.query(SELECT_BOOKS_SORT_AUTHOR_ZA_WITH_THEME, ROW_MAPPER_TO_BOOK, themeTitle);
    }


    @Override
    public List<Book> findBooksSortYearLM() {
        return jdbcTemplate.query(SELECT_BOOKS_SORT_YEAR_LM, ROW_MAPPER_TO_BOOK);
    }

    @Override
    public List<Book> findBooksSortYearML() {
        return jdbcTemplate.query(SELECT_BOOKS_SORT_YEAR_ML, ROW_MAPPER_TO_BOOK);
    }


    @Override
    public List<Book> findBooksSortYearLMWithTheme(String themeTitle) {
        return jdbcTemplate.query(SELECT_BOOKS_SORT_YEAR_LM_WITH_THEME, ROW_MAPPER_TO_BOOK, themeTitle);
    }


    @Override
    public List<Book> findBooksSortYearMLWithTheme(String themeTitle) {
        return jdbcTemplate.query(SELECT_BOOKS_SORT_YEAR_ML_WITH_THEME, ROW_MAPPER_TO_BOOK, themeTitle);
    }


    @Override
    public List<Book> findBooksSortCountLM() {
        return jdbcTemplate.query(SELECT_BOOKS_SORT_COUNT_LM, ROW_MAPPER_TO_BOOK);
    }

    @Override
    public List<Book> findBooksSortCountML() {
        return jdbcTemplate.query(SELECT_BOOKS_SORT_COUNT_ML, ROW_MAPPER_TO_BOOK);
    }

    @Override
    public List<Book> findBooksSortCountLMWithTheme(String themeTitle) {
        return jdbcTemplate.query(SELECT_BOOKS_SORT_COUNT_LM_WITH_THEME, ROW_MAPPER_TO_BOOK, themeTitle);
    }


    @Override
    public List<Book> findBooksSortCountMLWithTheme(String themeTitle) {
        return jdbcTemplate.query(SELECT_BOOKS_SORT_COUNT_ML_WITH_THEME, ROW_MAPPER_TO_BOOK, themeTitle);
    }

    @Override
    public List<Book> findBooksByTitle(String title) {
        String titlePre = "%" + title;
        String titlePost = title + "%";
        String titlePrePost = "%" + title + "%";

        return jdbcTemplate.query(SELECT_BOOKS_BY_TITLE, ROW_MAPPER_TO_BOOK, titlePost, titlePre, titlePrePost);
    }

    @Override
    public List<Book> findBooksByAuthor(String name) {
        String namePre = "%" + name;
        String namePost = name + "%";
        String namePrePost = "%" + name + "%";

        return jdbcTemplate.query(SELECT_BOOKS_BY_AUTHOR, ROW_MAPPER_TO_BOOK, namePost, namePre, namePrePost);
    }

    @Override
    public List<Book> findBooksByTheme(String title) {
        String titlePre = "%" + title;
        String titlePost = title + "%";
        String titlePrePost = "%" + title + "%";

        return jdbcTemplate.query(SELECT_BOOKS_BY_THEME, ROW_MAPPER_TO_BOOK, titlePost, titlePre, titlePrePost);
    }


    @Override
    public List<Book> findBooksByThemeId(Long themeId) {
        return jdbcTemplate.query(SELECT_BOOKS_BY_THEME_ID, ROW_MAPPER_TO_BOOK, themeId);
    }


    @Override
    public Map<Long, List<Long>> findAllBooksIdWithSaleId() {
        List<Map<Long, List<Long>>> saleIdsWithBookIds = jdbcTemplate.query(SELECT_BOOKS_ID_WITH_SALE_ID, ROW_MAPPER_TO_BOOKS_ID_WITH_SALE_ID);
        if (!saleIdsWithBookIds.isEmpty()) {
            return saleIdsWithBookIds.get(0);
        }

        return null;

    }

    @Override
    public Map<Long, List<Long>> findBooksIdWithSaleIdInInterval(String from, String to) {
        List<Map<Long, List<Long>>> saleIdsBookIds = jdbcTemplate.query(SELECT_BOOKS_ID_WITH_SALE_ID_IN_INTERVAL, ROW_MAPPER_TO_BOOKS_ID_WITH_SALE_ID, from, to);
        if (!saleIdsBookIds.isEmpty()) {
            return saleIdsBookIds.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Map<Long, List<Long>> findBooksIdWithSaleIdForDay(String day) {
        List<Map<Long, List<Long>>> saleIdsBookIds = jdbcTemplate.query(SELECT_BOOKS_ID_WITH_SALE_ID_FOR_DAY, ROW_MAPPER_TO_BOOKS_ID_WITH_SALE_ID, day);
        if (!saleIdsBookIds.isEmpty()) {
            return saleIdsBookIds.get(0);
        } else {
            return null;
        }
    }


    @Override
    public int getCountBooksInSale(Long saleId) {
        return jdbcTemplate.query(SELECT_COUNT_BOOKS_IN_SALE, ROW_MAPPER_TO_COUNT_BOOKS_IN_SALE, saleId).get(0);
    }


    private RowMapper<Integer> ROW_MAPPER_TO_INT = (ResultSet resultSet, int rowNum) -> {
        return resultSet.getInt("count");
    };


    @Override
    public List<Book> findBooksByAuthorId(Long id) {
        return jdbcTemplate.query(SELECT_BOOKS_BY_AUTHOR_ID, ROW_MAPPER_TO_BOOK, id);
    }


    @Override
    public void deleteAuthorForBook(Long bookId, Long authorId) {
        jdbcTemplate.update(DELETE_AUTHOR_FOR_BOOK, bookId, authorId);
    }

    private RowMapper<Long> ROW_MAPPER_TO_LONG = (ResultSet resultSet, int rowNum) -> {
        return resultSet.getLong("id");
    };


    @Override
    public void deleteThemeForBook(Long bookId, Long themeId) {
        jdbcTemplate.update(DELETE_FROM_BOOK_THEME_DB, bookId, themeId);
    }


    @Override
    public void updateFieldOfBook(Long bookId, String fieldName, Object value) {

        jdbcTemplate.update(UPDATE_FIELD_BOOK + fieldName + " = ? where id = ?", value, bookId);

    }


    private RowMapper<Long> ROW_MAPPER_TO_ID = (ResultSet resultSet, int rowNum) -> {

        return resultSet.getLong("id");

    };

    @Override
    public Long createBook(Book book) {
        return jdbcTemplate.query(INSERT_INTO_BOOK_DB, ROW_MAPPER_TO_ID, book.getTitle(), book.getEdition(), book.getDescription(), book.getPublishinghouse(), book.getPrice(), book.getCount(), book.getYear()).get(0);
    }


    @Override
    public void deleteBookById(Long bookId) {
        jdbcTemplate.update(DELETE_BOOK_BY_ID, bookId);
    }


}
