package com.example.libmanagement.controller;

import com.example.libmanagement.dto.CategoryStatisticsDto;
import com.example.libmanagement.dto.DailyBorrowStatsDto;
import com.example.libmanagement.dto.DashboardSummaryDto;
import com.example.libmanagement.dto.NewestBookDto;
import com.example.libmanagement.dto.OverdueBorrowDto;
import com.example.libmanagement.dto.TopBorrowedBookDto;
import com.example.libmanagement.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardApiController {

    private final DashboardService dashboardService;

    public DashboardApiController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public DashboardSummaryDto getSummary() {
        return dashboardService.getSummary();
    }

    @GetMapping("/category-stats")
    public List<CategoryStatisticsDto> getCategoryStats() {
        return dashboardService.getCategoryStatistics();
    }

    @GetMapping("/top-borrowed-books")
    public List<TopBorrowedBookDto> getTopBorrowedBooks() {
        return dashboardService.getTopBorrowedBooks();
    }

    @GetMapping("/borrow-trend")
    public List<DailyBorrowStatsDto> getBorrowTrend() {
        return dashboardService.getBorrowTrendLast7Days();
    }

    @GetMapping("/overdue-borrows")
    public List<OverdueBorrowDto> getOverdueBorrows() {
        return dashboardService.getTopOverdueBorrows();
    }

    @GetMapping("/newest-books")
    public List<NewestBookDto> getNewestBooks() {
        return dashboardService.getNewestBooks();
    }
}