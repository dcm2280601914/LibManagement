package com.example.libmanagement.service;

import com.example.libmanagement.entity.Borrower;

import java.util.List;
import java.util.Optional;

public interface BorrowerService {
    List<Borrower> findAll();
    Borrower findById(Long id);
    Borrower save(Borrower borrower);
    Borrower update(Long id, Borrower borrower);
    void delete(Long id);
    List<Borrower> searchByFullName(String keyword);
    Optional<Borrower> findByPhone(String phone);
}