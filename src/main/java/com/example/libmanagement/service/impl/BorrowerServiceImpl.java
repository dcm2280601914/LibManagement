package com.example.libmanagement.service.impl;

import com.example.libmanagement.entity.Borrower;
import com.example.libmanagement.repository.BorrowerRepository;
import com.example.libmanagement.service.BorrowerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BorrowerServiceImpl implements BorrowerService {

    private final BorrowerRepository borrowerRepository;

    public BorrowerServiceImpl(BorrowerRepository borrowerRepository) {
        this.borrowerRepository = borrowerRepository;
    }

    @Override
    public List<Borrower> findAll() {
        return borrowerRepository.findAll();
    }

    @Override
    public Borrower findById(Long id) {
        return borrowerRepository.findById(id).orElse(null);
    }

    @Override
    public Borrower save(Borrower borrower) {
        return borrowerRepository.save(borrower);
    }

    @Override
    public Borrower update(Long id, Borrower borrower) {
        Borrower existingBorrower = findById(id);
        if (existingBorrower == null) {
            return null;
        }

        existingBorrower.setFullName(borrower.getFullName());
        existingBorrower.setPhone(borrower.getPhone());
        existingBorrower.setEmail(borrower.getEmail());
        existingBorrower.setAddress(borrower.getAddress());
        existingBorrower.setIdentityNumber(borrower.getIdentityNumber());

        return borrowerRepository.save(existingBorrower);
    }

    @Override
    public void delete(Long id) {
        borrowerRepository.deleteById(id);
    }

    @Override
    public List<Borrower> searchByFullName(String keyword) {
        return borrowerRepository.findByFullNameContainingIgnoreCase(keyword);
    }

    @Override
    public Optional<Borrower> findByPhone(String phone) {
        return borrowerRepository.findByPhone(phone);
    }
}