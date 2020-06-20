package com.bookstore.service;

import com.bookstore.model.Report;

import java.util.List;
import java.util.Map;

public interface ReportConstructorService {
    List<Report> getReports(Map<Long, List<Long>> booksIdWithSaleId);
}
