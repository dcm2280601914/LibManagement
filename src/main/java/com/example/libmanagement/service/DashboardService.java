package com.example.libmanagement.service;

import com.example.libmanagement.dto.CategoryStatisticsDto;
import com.example.libmanagement.dto.DailyBorrowStatsDto;
import com.example.libmanagement.dto.DashboardSummaryDto;
import com.example.libmanagement.dto.NewestBookDto;
import com.example.libmanagement.dto.OverdueBorrowDto;
import com.example.libmanagement.dto.TopBorrowedBookDto;

import java.util.List;

public interface DashboardService {

    DashboardSummaryDto getSummary();

    List<CategoryStatisticsDto> getCategoryStatistics();

    List<TopBorrowedBookDto> getTopBorrowedBooks();

    List<DailyBorrowStatsDto> getBorrowTrendLast7Days();

    List<OverdueBorrowDto> getTopOverdueBorrows();

    List<NewestBookDto> getNewestBooks();
}