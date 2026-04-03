package com.example.libmanagement.service.impl;

import com.example.libmanagement.entity.Book;
import com.example.libmanagement.entity.BorrowRecord;
import com.example.libmanagement.entity.Borrower;
import com.example.libmanagement.entity.Employee;
import com.example.libmanagement.enums.BorrowStatus;
import com.example.libmanagement.repository.BookRepository;
import com.example.libmanagement.repository.BorrowRecordRepository;
import com.example.libmanagement.repository.BorrowerRepository;
import com.example.libmanagement.repository.EmployeeRepository;
import com.example.libmanagement.service.BorrowRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BorrowRecordServiceImpl implements BorrowRecordService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BorrowerRepository borrowerRepository;
    private final EmployeeRepository employeeRepository;
    private final BookRepository bookRepository;

    public BorrowRecordServiceImpl(BorrowRecordRepository borrowRecordRepository,
                                   BorrowerRepository borrowerRepository,
                                   EmployeeRepository employeeRepository,
                                   BookRepository bookRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.borrowerRepository = borrowerRepository;
        this.employeeRepository = employeeRepository;
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
        if (borrowRecord.getBorrower() == null || borrowRecord.getBorrower().getId() == null) {
            throw new RuntimeException("Người mượn không hợp lệ");
        }
        if (borrowRecord.getEmployee() == null || borrowRecord.getEmployee().getId() == null) {
            throw new RuntimeException("Nhân viên không hợp lệ");
        }
        if (borrowRecord.getBook() == null || borrowRecord.getBook().getId() == null) {
            throw new RuntimeException("Sách không hợp lệ");
        }

        Borrower borrower = borrowerRepository.findById(borrowRecord.getBorrower().getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người mượn"));

        Employee employee = employeeRepository.findById(borrowRecord.getEmployee().getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

        Book book = bookRepository.findById(borrowRecord.getBook().getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách"));

        if (!borrower.isAllowedToBorrow()) {
            throw new RuntimeException("Người mượn hiện không được phép mượn sách");
        }

        if (book.getAvailableQuantity() == null || book.getAvailableQuantity() <= 0) {
            throw new RuntimeException("Sách đã hết trong kho");
        }

        borrowRecord.setBorrower(borrower);
        borrowRecord.setEmployee(employee);
        borrowRecord.setBook(book);

        if (borrowRecord.getBorrowDate() == null) {
            borrowRecord.setBorrowDate(LocalDate.now());
        }

        if (borrowRecord.getDueDate() == null) {
            borrowRecord.setDueDate(borrowRecord.getBorrowDate().plusDays(7));
        }

        if (borrowRecord.getDueDate().isBefore(borrowRecord.getBorrowDate())) {
            throw new RuntimeException("Hạn trả phải sau hoặc bằng ngày mượn");
        }

        // Phiếu mượn mới luôn là BORROWED
        borrowRecord.setStatus(BorrowStatus.BORROWED);

        if (!StringUtils.hasText(borrowRecord.getBorrowCode())) {
            borrowRecord.setBorrowCode("BR-" + System.currentTimeMillis());
        } else {
            borrowRecord.setBorrowCode(borrowRecord.getBorrowCode().trim());
        }

        if (borrowRecordRepository.findByBorrowCode(borrowRecord.getBorrowCode()).isPresent()) {
            throw new RuntimeException("Mã phiếu mượn đã tồn tại");
        }

        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        borrower.setCurrentBorrowedCount(
                (borrower.getCurrentBorrowedCount() == null ? 0 : borrower.getCurrentBorrowedCount()) + 1
        );

        bookRepository.save(book);
        borrowerRepository.save(borrower);

        return borrowRecordRepository.save(borrowRecord);
    }

    @Override
    public BorrowRecord update(Long id, BorrowRecord borrowRecord) {
        BorrowRecord existing = findById(id);
        if (existing == null) {
            return null;
        }

        if (borrowRecord.getBorrower() == null || borrowRecord.getBorrower().getId() == null) {
            throw new RuntimeException("Người mượn không hợp lệ");
        }
        if (borrowRecord.getEmployee() == null || borrowRecord.getEmployee().getId() == null) {
            throw new RuntimeException("Nhân viên không hợp lệ");
        }
        if (borrowRecord.getBook() == null || borrowRecord.getBook().getId() == null) {
            throw new RuntimeException("Sách không hợp lệ");
        }

        Borrower borrower = borrowerRepository.findById(borrowRecord.getBorrower().getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người mượn"));

        Employee employee = employeeRepository.findById(borrowRecord.getEmployee().getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

        Book book = bookRepository.findById(borrowRecord.getBook().getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách"));

        if (borrowRecord.getDueDate() != null && borrowRecord.getBorrowDate() != null
                && borrowRecord.getDueDate().isBefore(borrowRecord.getBorrowDate())) {
            throw new RuntimeException("Hạn trả phải sau hoặc bằng ngày mượn");
        }

        if (StringUtils.hasText(borrowRecord.getBorrowCode())) {
            Optional<BorrowRecord> sameCode = borrowRecordRepository.findByBorrowCode(borrowRecord.getBorrowCode().trim());
            if (sameCode.isPresent() && !sameCode.get().getId().equals(id)) {
                throw new RuntimeException("Mã phiếu mượn đã tồn tại");
            }
            existing.setBorrowCode(borrowRecord.getBorrowCode().trim());
        }

        existing.setBorrower(borrower);
        existing.setEmployee(employee);
        existing.setBook(book);
        existing.setBorrowDate(borrowRecord.getBorrowDate());
        existing.setDueDate(borrowRecord.getDueDate());
        existing.setStatus(borrowRecord.getStatus());
        existing.setNote(borrowRecord.getNote());

        return borrowRecordRepository.save(existing);
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