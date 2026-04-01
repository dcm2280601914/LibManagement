package com.example.libmanagement.repository;

import com.example.libmanagement.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    Optional<BorrowRecord> findByBorrowCode(String borrowCode);
    List<BorrowRecord> findByBorrowerFullNameContainingIgnoreCase(String keyword);
}