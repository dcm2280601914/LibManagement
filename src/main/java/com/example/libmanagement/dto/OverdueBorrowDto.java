package com.example.libmanagement.dto;

import java.time.LocalDate;

public class OverdueBorrowDto {

    private String borrowCode;
    private String borrowerName;
    private String bookTitle;
    private LocalDate dueDate;
    private long overdueDays;

    public OverdueBorrowDto() {
    }

    public OverdueBorrowDto(String borrowCode, String borrowerName, String bookTitle, LocalDate dueDate, long overdueDays) {
        this.borrowCode = borrowCode;
        this.borrowerName = borrowerName;
        this.bookTitle = bookTitle;
        this.dueDate = dueDate;
        this.overdueDays = overdueDays;
    }

    public String getBorrowCode() {
        return borrowCode;
    }

    public void setBorrowCode(String borrowCode) {
        this.borrowCode = borrowCode;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public long getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(long overdueDays) {
        this.overdueDays = overdueDays;
    }
}