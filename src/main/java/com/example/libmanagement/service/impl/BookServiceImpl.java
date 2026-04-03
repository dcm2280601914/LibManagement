package com.example.libmanagement.service.impl;

import com.example.libmanagement.entity.Book;
import com.example.libmanagement.entity.Category;
import com.example.libmanagement.repository.BookRepository;
import com.example.libmanagement.repository.CategoryRepository;
import com.example.libmanagement.service.BookService;
import com.example.libmanagement.specification.BookSpecification;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    public BookServiceImpl(BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public Book save(Book book) {
        if (book.getCategory() != null && book.getCategory().getId() != null) {
            Category category = categoryRepository.findById(book.getCategory().getId()).orElse(null);
            book.setCategory(category);
        } else {
            book.setCategory(null);
        }

        return bookRepository.save(book);
    }

    @Override
    public Book update(Long id, Book book) {
        Book existingBook = findById(id);
        if (existingBook == null) {
            return null;
        }

        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPublisher(book.getPublisher());
        existingBook.setPublicationYear(book.getPublicationYear());
        existingBook.setIsbn(book.getIsbn());
        existingBook.setBarcode(book.getBarcode());
        existingBook.setLocation(book.getLocation());
        existingBook.setCoverImage(book.getCoverImage());
        existingBook.setImportDate(book.getImportDate());
        existingBook.setTotalQuantity(book.getTotalQuantity());
        existingBook.setAvailableQuantity(book.getAvailableQuantity());
        existingBook.setDescription(book.getDescription());

        if (book.getCategory() != null && book.getCategory().getId() != null) {
            Category category = categoryRepository.findById(book.getCategory().getId()).orElse(null);
            existingBook.setCategory(category);
        } else {
            existingBook.setCategory(null);
        }

        return bookRepository.save(existingBook);
    }

    @Override
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Page<Book> searchBooks(String keyword,
                                  Long categoryId,
                                  String author,
                                  Integer publicationYear,
                                  int page,
                                  int size,
                                  String sortField,
                                  String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Book> spec = Specification
                .where(BookSpecification.keywordLike(keyword))
                .and(BookSpecification.hasCategory(categoryId))
                .and(BookSpecification.hasAuthor(author))
                .and(BookSpecification.hasPublicationYear(publicationYear));

        return bookRepository.findAll(spec, pageable);
    }

    @Override
    public List<String> getAllAuthors() {
        return bookRepository.findDistinctByAuthorIsNotNullOrderByAuthorAsc()
                .stream()
                .map(Book::getAuthor)
                .filter(author -> author != null && !author.trim().isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer> getAllPublicationYears() {
        return bookRepository.findDistinctByPublicationYearIsNotNullOrderByPublicationYearDesc()
                .stream()
                .map(Book::getPublicationYear)
                .distinct()
                .collect(Collectors.toList());
    }
}