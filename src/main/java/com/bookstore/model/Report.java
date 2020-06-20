package com.bookstore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    private String sellerName;
    private int countSoldBooks;
    private List<Theme> themes;
    private BigDecimal profit;
    private String dateSale;
}
