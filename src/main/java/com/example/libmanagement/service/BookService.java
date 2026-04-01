package com.example.libmanagement.service;

import com.example.libmanagement.entity.Book;

import java.util.List;

public interface BookService {
    List<Book> findAll();
    Book findById(Long id);
    Book save(Book book);
    Book update(Long id, Book book);
    void delete(Long id);
    List<Book> searchByTitle(String keyword);
}
