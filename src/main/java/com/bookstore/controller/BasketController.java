package com.bookstore.controller;

import com.bookstore.dao.SaleDao;
import com.bookstore.dao.UserDao;
import com.bookstore.dto.BasketDto;
import com.bookstore.model.Book;
import com.bookstore.model.User;
import com.bookstore.security.AccountValidatorService;
import com.bookstore.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BasketController {

    @Autowired
    private BasketService basketService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private SaleDao saleDao;

    @Autowired
    private AccountValidatorService accountValidatorService;

    @PostMapping("/clearBasket")
    public String clearBasket(Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        basketService.clearBasket(userDao.findUserByNickname(authentication.getName()).getUserId());
        return "redirect:/mainUserPage";
    }


    @GetMapping("/basket")
    public String getBasket(Model model, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        User user = userDao.findUserByNickname(authentication.getName());

        List<Book> books = basketService.getBooksFromBasket(user.getUserId());

        List<BasketDto> basketDtos = new ArrayList<>();


        for (int i = 0; i < books.size(); i++) {
            BigDecimal countBooks = new BigDecimal(books.get(i).getCountInBasket());
            BigDecimal resultPrice = countBooks.multiply(books.get(i).getPrice());

            BasketDto basketDto = BasketDto.builder()
                    .title(books.get(i).getTitle())
                    .price(books.get(i).getPrice())
                    .resultPrice(resultPrice)
                    .countInBasket(books.get(i).getCountInBasket())
                    .build();
            basketDtos.add(basketDto);
        }

        model.addAttribute("user", user);
        model.addAttribute("basketDtos", basketDtos);
        model.addAttribute("resultSum", basketService.getResultSum(user.getUserId()));
        return "basket";
    }


    @PostMapping("/basket/sell")
    public String sellBooksFromBasket(Model model, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        User user = userDao.findUserByNickname(authentication.getName());

        BigDecimal sum = basketService.getResultSum(user.getUserId());
        List<Book> books = basketService.getBooksFromBasket(user.getUserId());

        saleDao.saveReportOnSale(sum, user, books);

        basketService.clearBasket(user.getUserId());

        return "redirect:/";
    }

}
