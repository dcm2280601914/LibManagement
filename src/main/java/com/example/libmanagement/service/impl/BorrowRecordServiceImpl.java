package com.example.libmanagement.service.impl;

import com.example.libmanagement.entity.Book;
import com.example.libmanagement.entity.BorrowRecord;
import com.example.libmanagement.repository.BookRepository;
import com.example.libmanagement.repository.BorrowRecordRepository;
import com.example.libmanagement.service.BorrowRecordService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BorrowRecordServiceImpl implements BorrowRecordService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;

    public BorrowRecordServiceImpl(BorrowRecordRepository borrowRecordRepository, BookRepository bookRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<BorrowRecord> findAll() {
        return borrowRecordRepository.findAll();
    }

    @Override
    public BorrowRecord findById(Long id) {
        return borrowRecordRepository.findById(id).orElse(null);
    }

    @Override
    public BorrowRecord save(BorrowRecord borrowRecord) {
        Book book = borrowRecord.getBook();

        if (book == null) {
            throw new RuntimeException("Sach khong ton tai");
        }

        if (book.getAvailableQuantity() == null || book.getAvailableQuantity() <= 0) {
            throw new RuntimeException("Sach da het trong kho");
        }

        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        bookRepository.save(book);

        return borrowRecordRepository.save(borrowRecord);
    }

    @Override
    public BorrowRecord update(Long id, BorrowRecord borrowRecord) {
        BorrowRecord existingBorrowRecord = findById(id);
        if (existingBorrowRecord == null) {
            return null;
        }

        existingBorrowRecord.setBorrowCode(borrowRecord.getBorrowCode());
        existingBorrowRecord.setBorrower(borrowRecord.getBorrower());
        existingBorrowRecord.setEmployee(borrowRecord.getEmployee());
        existingBorrowRecord.setBook(borrowRecord.getBook());
        existingBorrowRecord.setBorrowDate(borrowRecord.getBorrowDate());
        existingBorrowRecord.setDueDate(borrowRecord.getDueDate());
        existingBorrowRecord.setStatus(borrowRecord.getStatus());
        existingBorrowRecord.setNote(borrowRecord.getNote());

        return borrowRecordRepository.save(existingBorrowRecord);
    }

    @Override
    public void delete(Long id) {
        borrowRecordRepository.deleteById(id);
    }

    @Override
    public Optional<BorrowRecord> findByBorrowCode(String borrowCode) {
        return borrowRecordRepository.findByBorrowCode(borrowCode);
    }

    @Override
    public List<BorrowRecord> searchByBorrowerName(String keyword) {
        return borrowRecordRepository.findByBorrowerFullNameContainingIgnoreCase(keyword);
    }
}