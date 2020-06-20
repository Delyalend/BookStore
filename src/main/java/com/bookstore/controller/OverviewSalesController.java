package com.bookstore.controller;

import com.bookstore.dao.BookDao;
import com.bookstore.dao.UserDao;
import com.bookstore.model.Report;
import com.bookstore.model.User;
import com.bookstore.security.AccountValidatorService;
import com.bookstore.service.ReportConstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class OverviewSalesController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private BookDao bookDao;

    @Autowired
    private ReportConstructorService reportConstructorService;

    @Autowired
    private AccountValidatorService accountValidatorService;

    @GetMapping("/overviewSales")
    public String overviewSales(Authentication authentication, Model model) {

        accountValidatorService.doValidation(authentication);

        User user = userDao.findUserByNickname(authentication.getName());

        model.addAttribute("user", user);

        return "overviewSales";
    }

    @GetMapping("/overviewSales/day")
    public String getReportsForDay(@RequestParam String day, Model model, Authentication authentication) {

        accountValidatorService.doValidation(authentication);


        Map<Long, List<Long>> saleIdsBookIds = bookDao.findBooksIdWithSaleIdForDay(day);

        if (saleIdsBookIds != null) {

            List<Report> reports = reportConstructorService.getReports(saleIdsBookIds);

            model.addAttribute("reports", reports);
        } else {
            model.addAttribute("error", "reportsNotFound");
        }

        model.addAttribute("user", userDao.findUserByNickname(authentication.getName()));

        return "report";
    }

    @GetMapping("/overviewSales/interval")
    public String getReportsInInterval(@RequestParam String from, @RequestParam String to, Model model, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        Map<Long, List<Long>> saleIdsBookIds = bookDao.findBooksIdWithSaleIdInInterval(from, to);

        if (saleIdsBookIds != null) {
            List<Report> reports = reportConstructorService.getReports(saleIdsBookIds);
            model.addAttribute("reports", reports);
        } else {
            model.addAttribute("error", "reportsNotFound");
        }

        model.addAttribute("user", userDao.findUserByNickname(authentication.getName()));

        return "report";
    }


    @GetMapping("/overviewSales/all")
    public String getReportsForAllTime(Model model, Authentication authentication) {

        accountValidatorService.doValidation(authentication);

        Map<Long, List<Long>> saleIdsBookIds = bookDao.findAllBooksIdWithSaleId();

        if (saleIdsBookIds != null) {
            List<Report> reports = reportConstructorService.getReports(saleIdsBookIds);
            model.addAttribute("reports", reports);
        } else {
            model.addAttribute("error", "reportsNotFound");
        }

        model.addAttribute("user", userDao.findUserByNickname(authentication.getName()));

        return "report";
    }

}
