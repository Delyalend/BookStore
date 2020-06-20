package com.bookstore.service;

import com.bookstore.dao.BookDao;
import com.bookstore.dao.SaleDao;
import com.bookstore.dao.ThemeDao;
import com.bookstore.dao.UserDao;
import com.bookstore.model.Report;
import com.bookstore.model.Theme;
import com.bookstore.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ReportConstructorServiceImpl implements ReportConstructorService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ThemeDao themeDao;

    @Autowired
    private BookDao bookDao;

    @Autowired
    private SaleDao saleDao;

    @Override
    public List<Report> getReports(Map<Long, List<Long>> booksIdWithSaleId) {

        List<Report> reports = new ArrayList<>();

        List<Long> saleIds = new ArrayList<Long>(booksIdWithSaleId.keySet());

        for (int i = 0; i < saleIds.size(); i++) {

            User seller = userDao.findUserBySaleId(saleIds.get(i));

            int countBooks = bookDao.getCountBooksInSale(saleIds.get(i));

            List<Long> bookIds = booksIdWithSaleId.get(saleIds.get(i));//id-шники всех книг в данной продаже

            List<Theme> allThemes = new ArrayList<>();


            for (int j = 0; j < bookIds.size(); j++) {
                List<Theme> themeList = themeDao.getThemesByBookId(bookIds.get(j));
                if (themeList == null) {
                    continue;
                }
                for (int k = 0; k < themeList.size(); k++) {
                    if (!allThemes.contains(themeList.get(k))) {
                        allThemes.add(themeList.get(k));
                    }
                }
            }

            BigDecimal profit = saleDao.getProfitSaleBySaleId(saleIds.get(i));

            String date = saleDao.getDateBySaleId(saleIds.get(i));

            Report report = Report.builder()
                    .themes(allThemes)
                    .countSoldBooks(countBooks)
                    .dateSale(date)
                    .sellerName(seller.getLastname() + " " + seller.getFirstname() + " " + seller.getMiddlename())
                    .profit(profit)
                    .build();

            reports.add(report);
        }

        return reports;
    }
}
