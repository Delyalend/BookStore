package com.bookstore.dto;

import com.bookstore.model.Book;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;


@Data
public class BookDto {

    private String title;
    private String description;
    private String yearwriting;
    private String edition;
    private String publishinghouse;
    private BigDecimal price;
    private int count;

    public Book toBook() throws ParseException {

        Date yearwritingDate;
        java.sql.Date yearWritingSqlDate = new java.sql.Date(0);
    if(description==null) {
        description = "unknown";
    }
    if(!yearwriting.isEmpty()) {

        yearwritingDate  = new SimpleDateFormat("yyyy/MM/dd").parse(yearwriting.replace('-','/'));

        yearWritingSqlDate = new java.sql.Date(yearwritingDate.getTime());
    }
    if(publishinghouse==null) {
        publishinghouse="unknown";
    }
    if(edition.equals("")) {
        edition="0";
    }

        System.out.println(yearwriting + " = yearwriting");



        return Book.builder()
                .title(title)
                .description(description)
                .year(yearWritingSqlDate)
                .edition(Integer.parseInt(edition))
                .publishinghouse(publishinghouse)
                .price(price)
                .count(count)
                .build();
    }

}
