package com.example.libmanagement.service.impl;

import com.example.libmanagement.entity.Book;
import com.example.libmanagement.entity.BorrowRecord;
import com.example.libmanagement.entity.ReturnRecord;
import com.example.libmanagement.enums.BorrowStatus;
import com.example.libmanagement.repository.BookRepository;
import com.example.libmanagement.repository.BorrowRecordRepository;
import com.example.libmanagement.repository.ReturnRecordRepository;
import com.example.libmanagement.service.ReturnRecordService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReturnRecordServiceImpl implements ReturnRecordService {

    private final ReturnRecordRepository returnRecordRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;

    public ReturnRecordServiceImpl(ReturnRecordRepository returnRecordRepository,
                                   BorrowRecordRepository borrowRecordRepository,
                                   BookRepository bookRepository) {
        this.returnRecordRepository = returnRecordRepository;
        this.borrowRecordRepository = borrowRecordRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<ReturnRecord> findAll() {
        return returnRecordRepository.findAll();
    }

    @Override
    public ReturnRecord findById(Long id) {
        return returnRecordRepository.findById(id).orElse(null);
    }

    @Override
    public ReturnRecord save(ReturnRecord returnRecord) {
        BorrowRecord borrowRecord = returnRecord.getBorrowRecord();

        if (borrowRecord == null) {
            throw new RuntimeException("Phieu muon khong ton tai");
        }

        if (borrowRecord.getStatus() == BorrowStatus.RETURNED) {
            throw new RuntimeException("Sach nay da duoc tra");
        }

        Book book = borrowRecord.getBook();
        if (book != null) {
            book.setAvailableQuantity(book.getAvailableQuantity() + 1);
            bookRepository.save(book);
        }

        borrowRecord.setStatus(BorrowStatus.RETURNED);
        borrowRecordRepository.save(borrowRecord);

        return returnRecordRepository.save(returnRecord);
    }

    @Override
    public ReturnRecord update(Long id, ReturnRecord returnRecord) {
        ReturnRecord existingReturnRecord = findById(id);
        if (existingReturnRecord == null) {
            return null;
        }

        existingReturnRecord.setReturnCode(returnRecord.getReturnCode());
        existingReturnRecord.setBorrowRecord(returnRecord.getBorrowRecord());
        existingReturnRecord.setEmployee(returnRecord.getEmployee());
        existingReturnRecord.setReturnDate(returnRecord.getReturnDate());
        existingReturnRecord.setStatus(returnRecord.getStatus());
        existingReturnRecord.setFineAmount(returnRecord.getFineAmount());
        existingReturnRecord.setNote(returnRecord.getNote());

        return returnRecordRepository.save(existingReturnRecord);
    }

    @Override
    public void delete(Long id) {
        returnRecordRepository.deleteById(id);
    }

    @Override
    public Optional<ReturnRecord> findByReturnCode(String returnCode) {
        return returnRecordRepository.findByReturnCode(returnCode);
    }

    @Override
    public List<ReturnRecord> searchByBorrowerName(String keyword) {
        return returnRecordRepository.findByBorrowRecordBorrowerFullNameContainingIgnoreCase(keyword);
    }
}