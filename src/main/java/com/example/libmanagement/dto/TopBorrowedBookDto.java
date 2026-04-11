package com.example.libmanagement.dto;

public class TopBorrowedBookDto {

    private String bookTitle;
    private long borrowCount;

    public TopBorrowedBookDto() {
    }

    public TopBorrowedBookDto(String bookTitle, long borrowCount) {
        this.bookTitle = bookTitle;
        this.borrowCount = borrowCount;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public long getBorrowCount() {
        return borrowCount;
    }

    public void setBorrowCount(long borrowCount) {
        this.borrowCount = borrowCount;
    }
}