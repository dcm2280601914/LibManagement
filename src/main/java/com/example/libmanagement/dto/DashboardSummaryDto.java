package com.example.libmanagement.dto;

import java.math.BigDecimal;

public class DashboardSummaryDto {

    private long totalBooks;
    private long borrowedBooks;
    private long newBorrowersThisMonth;
    private BigDecimal totalPaidFines;

    public DashboardSummaryDto() {
    }

    public DashboardSummaryDto(long totalBooks, long borrowedBooks, long newBorrowersThisMonth, BigDecimal totalPaidFines) {
        this.totalBooks = totalBooks;
        this.borrowedBooks = borrowedBooks;
        this.newBorrowersThisMonth = newBorrowersThisMonth;
        this.totalPaidFines = totalPaidFines;
    }

    public long getTotalBooks() {
        return totalBooks;
    }

    public void setTotalBooks(long totalBooks) {
        this.totalBooks = totalBooks;
    }

    public long getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(long borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    public long getNewBorrowersThisMonth() {
        return newBorrowersThisMonth;
    }

    public void setNewBorrowersThisMonth(long newBorrowersThisMonth) {
        this.newBorrowersThisMonth = newBorrowersThisMonth;
    }

    public BigDecimal getTotalPaidFines() {
        return totalPaidFines;
    }

    public void setTotalPaidFines(BigDecimal totalPaidFines) {
        this.totalPaidFines = totalPaidFines;
    }
}