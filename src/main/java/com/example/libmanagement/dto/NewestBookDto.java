package com.example.libmanagement.dto;

import java.time.LocalDate;

public class NewestBookDto {

    private Long id;
    private String title;
    private String categoryName;
    private String author;
    private LocalDate importDate;

    public NewestBookDto() {
    }

    public NewestBookDto(Long id, String title, String categoryName, String author, LocalDate importDate) {
        this.id = id;
        this.title = title;
        this.categoryName = categoryName;
        this.author = author;
        this.importDate = importDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getImportDate() {
        return importDate;
    }

    public void setImportDate(LocalDate importDate) {
        this.importDate = importDate;
    }
}