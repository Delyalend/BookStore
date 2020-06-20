package com.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasketDto {
    private String title;
    private int countInBasket;
    private BigDecimal price;
    private BigDecimal resultPrice;
}
