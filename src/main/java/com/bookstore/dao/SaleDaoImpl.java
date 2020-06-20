package com.bookstore.dao;

import com.bookstore.model.Book;
import com.bookstore.model.User;
import com.fasterxml.jackson.core.JsonToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.List;

@Component
public class SaleDaoImpl implements SaleDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    //language=SQL
    private final String INSERT_INTO_SALE_DB = "insert into sale_db(sum,datesale,user_id) values (?,?,?) returning id";

    //language=SQL
    private final String INSERT_INTO_SALE_BOOK_DB = "insert into sale_book_db values (?, ?, ?, ?)";

    //language=SQL
    private final String UPDATE_BOOK_DB_COUNT = "update book_db set count = count - ? where id = ?";

    private RowMapper<Long> ROW_MAPPER_TO_ID = (ResultSet resultSet, int rawNum) -> {
        return resultSet.getLong("id");
    };
    //language=SQL
    private final String SELECT_PROFIT_FROM_SALE_DB_BY_SALE_ID = "select sum from sale_db where id = ?";

    //language=SQL
    private final String SELECT_DATE_BY_SALE_ID = "select datesale from sale_db where id = ?";

    //language=SQL
    private final String DELETE_SALES_FOR_BOOK_BY_BOOK_ID = "delete from sale_book_db where book_id = ?";

    private RowMapper<String> ROW_MAPPER_TO_STRING = (ResultSet resultSet, int rowNum) -> {
        return resultSet.getString("datesale");
    };

    private RowMapper<BigDecimal> ROW_MAPPER_TO_BIG_DECIMAL = (ResultSet resultSet, int rowNum) ->
    {
        String str = (resultSet.getString("sum").replace("р.", "")).replace(",", ".").replace(" ", "");

        return new BigDecimal(str);
    };

    @Override
    public void saveReportOnSale(BigDecimal sum, User user, List<Book> booksSold) {
        java.util.Date now = new java.util.Date();
        Long saleId = jdbcTemplate.query(INSERT_INTO_SALE_DB, ROW_MAPPER_TO_ID, sum, new Date(now.getTime()), user.getUserId()).get(0);


        //вычитание из книг на складе необходимого количества
        for (int i = 0; i < booksSold.size(); i++) {

            //Заполнение sale_book_db
            jdbcTemplate.update(INSERT_INTO_SALE_BOOK_DB, saleId, booksSold.get(i).getId(), booksSold.get(i).getCountInBasket(), booksSold.get(i).getPrice());

            jdbcTemplate.update(UPDATE_BOOK_DB_COUNT, booksSold.get(i).getCountInBasket(), booksSold.get(i).getId());


        }
    }


    @Override
    public BigDecimal getProfitSaleBySaleId(Long saleId) {
        return jdbcTemplate.query(SELECT_PROFIT_FROM_SALE_DB_BY_SALE_ID, ROW_MAPPER_TO_BIG_DECIMAL, saleId).get(0);
    }

    @Override
    public String getDateBySaleId(Long saleId) {
        return jdbcTemplate.query(SELECT_DATE_BY_SALE_ID, ROW_MAPPER_TO_STRING, saleId).get(0);
    }



    @Override
    public void deleteSalesForBook(Long bookId) {
        jdbcTemplate.update(DELETE_SALES_FOR_BOOK_BY_BOOK_ID, bookId);
    }


}
