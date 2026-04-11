package com.example.libmanagement.dto;

import java.time.LocalDate;

public class DailyBorrowStatsDto {

    private LocalDate date;
    private long borrowCount;

    public DailyBorrowStatsDto() {
    }

    public DailyBorrowStatsDto(LocalDate date, long borrowCount) {
        this.date = date;
        this.borrowCount = borrowCount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getBorrowCount() {
        return borrowCount;
    }

    public void setBorrowCount(long borrowCount) {
        this.borrowCount = borrowCount;
    }
}