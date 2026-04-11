package com.example.libmanagement.repository;

import com.example.libmanagement.entity.BorrowRecord;
import com.example.libmanagement.enums.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    List<BorrowRecord> findByBorrowerIdOrderByBorrowDateDesc(Long borrowerId);

    List<BorrowRecord> findByBorrowerFullNameContainingIgnoreCase(String keyword);

    Optional<BorrowRecord> findByBorrowCode(String borrowCode);

    List<BorrowRecord> findByStatusOrderByBorrowDateDesc(BorrowStatus status);

    long countByStatus(BorrowStatus status);

    List<BorrowRecord> findTop5ByStatusAndDueDateBeforeOrderByDueDateAsc(BorrowStatus status, LocalDate today);

    @Query("""
        SELECT br.book.title, COUNT(br)
        FROM BorrowRecord br
        GROUP BY br.book.id, br.book.title
        ORDER BY COUNT(br) DESC
    """)
    List<Object[]> findTopBorrowedBooks();

    @Query("""
        SELECT br.borrowDate, COUNT(br)
        FROM BorrowRecord br
        WHERE br.borrowDate BETWEEN :startDate AND :endDate
        GROUP BY br.borrowDate
        ORDER BY br.borrowDate ASC
    """)
    List<Object[]> countBorrowRecordsBetweenDates(LocalDate startDate, LocalDate endDate);
}