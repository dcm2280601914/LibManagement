package com.example.libmanagement.repository;

import com.example.libmanagement.entity.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BorrowerRepository extends JpaRepository<Borrower, Long> {
    List<Borrower> findByFullNameContainingIgnoreCase(String keyword);
    Optional<Borrower> findByPhone(String phone);
}