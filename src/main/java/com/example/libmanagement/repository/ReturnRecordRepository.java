package com.example.libmanagement.repository;

import com.example.libmanagement.entity.ReturnRecord;
import com.example.libmanagement.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ReturnRecordRepository extends JpaRepository<ReturnRecord, Long> {

    Optional<ReturnRecord> findByBorrowRecordId(Long borrowRecordId);

    boolean existsByBorrowRecordId(Long borrowRecordId);

    Optional<ReturnRecord> findByReturnCode(String returnCode);

    List<ReturnRecord> findByBorrowRecordBorrowerFullNameContainingIgnoreCase(String keyword);

    @Query("""
        SELECT COALESCE(SUM(rr.fineAmount), 0)
        FROM ReturnRecord rr
        WHERE rr.paymentStatus = :paymentStatus
    """)
    BigDecimal sumFineAmountByPaymentStatus(PaymentStatus paymentStatus);
}