package com.example.libmanagement.service.impl;

import com.example.libmanagement.dto.CategoryStatisticsDto;
import com.example.libmanagement.dto.DailyBorrowStatsDto;
import com.example.libmanagement.dto.DashboardSummaryDto;
import com.example.libmanagement.dto.NewestBookDto;
import com.example.libmanagement.dto.OverdueBorrowDto;
import com.example.libmanagement.dto.TopBorrowedBookDto;
import com.example.libmanagement.entity.Book;
import com.example.libmanagement.entity.BorrowRecord;
import com.example.libmanagement.enums.BorrowStatus;
import com.example.libmanagement.enums.PaymentStatus;
import com.example.libmanagement.repository.BookRepository;
import com.example.libmanagement.repository.BorrowRecordRepository;
import com.example.libmanagement.repository.BorrowerRepository;
import com.example.libmanagement.repository.ReturnRecordRepository;
import com.example.libmanagement.service.DashboardService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final BookRepository bookRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final BorrowerRepository borrowerRepository;
    private final ReturnRecordRepository returnRecordRepository;

    public DashboardServiceImpl(BookRepository bookRepository,
                                BorrowRecordRepository borrowRecordRepository,
                                BorrowerRepository borrowerRepository,
                                ReturnRecordRepository returnRecordRepository) {
        this.bookRepository = bookRepository;
        this.borrowRecordRepository = borrowRecordRepository;
        this.borrowerRepository = borrowerRepository;
        this.returnRecordRepository = returnRecordRepository;
    }

    @Override
    public DashboardSummaryDto getSummary() {
        long totalBooks = bookRepository.count();
        long borrowedBooks = borrowRecordRepository.countByStatus(BorrowStatus.BORROWED);

        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);

        long newBorrowersThisMonth =
                borrowerRepository.countByCreatedDateBetween(firstDayOfMonth, today);

        BigDecimal totalPaidFines =
                returnRecordRepository.sumFineAmountByPaymentStatus(PaymentStatus.PAID);

        if (totalPaidFines == null) {
            totalPaidFines = BigDecimal.ZERO;
        }

        return new DashboardSummaryDto(
                totalBooks,
                borrowedBooks,
                newBorrowersThisMonth,
                totalPaidFines
        );
    }

    @Override
    public List<CategoryStatisticsDto> getCategoryStatistics() {
        List<Object[]> rows = bookRepository.countBooksByCategory();
        List<CategoryStatisticsDto> result = new ArrayList<>();

        for (Object[] row : rows) {
            String categoryName = row[0] != null ? row[0].toString() : "Chưa phân loại";
            long bookCount = row[1] != null ? ((Number) row[1]).longValue() : 0L;

            result.add(new CategoryStatisticsDto(
                    null,
                    categoryName,
                    null,
                    null,
                    bookCount,
                    0L
            ));
        }

        return result;
    }

    @Override
    public List<TopBorrowedBookDto> getTopBorrowedBooks() {
        List<Object[]> rows = borrowRecordRepository.findTopBorrowedBooks();
        List<TopBorrowedBookDto> result = new ArrayList<>();

        int limit = Math.min(rows.size(), 5);
        for (int i = 0; i < limit; i++) {
            Object[] row = rows.get(i);

            String bookTitle = row[0] != null ? row[0].toString() : "Không xác định";
            long borrowCount = row[1] != null ? ((Number) row[1]).longValue() : 0L;

            result.add(new TopBorrowedBookDto(bookTitle, borrowCount));
        }

        return result;
    }

    @Override
    public List<DailyBorrowStatsDto> getBorrowTrendLast7Days() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);

        List<Object[]> rows =
                borrowRecordRepository.countBorrowRecordsBetweenDates(startDate, endDate);

        Map<LocalDate, Long> countMap = new HashMap<>();
        for (Object[] row : rows) {
            LocalDate date = (LocalDate) row[0];
            long count = row[1] != null ? ((Number) row[1]).longValue() : 0L;
            countMap.put(date, count);
        }

        List<DailyBorrowStatsDto> result = new ArrayList<>();
        LocalDate cursor = startDate;

        while (!cursor.isAfter(endDate)) {
            result.add(new DailyBorrowStatsDto(
                    cursor,
                    countMap.getOrDefault(cursor, 0L)
            ));
            cursor = cursor.plusDays(1);
        }

        return result;
    }

    @Override
    public List<OverdueBorrowDto> getTopOverdueBorrows() {
        LocalDate today = LocalDate.now();

        List<BorrowRecord> overdueRecords =
                borrowRecordRepository.findTop5ByStatusAndDueDateBeforeOrderByDueDateAsc(
                        BorrowStatus.BORROWED,
                        today
                );

        List<OverdueBorrowDto> result = new ArrayList<>();

        for (BorrowRecord record : overdueRecords) {
            long overdueDays = 0L;
            if (record.getDueDate() != null && today.isAfter(record.getDueDate())) {
                overdueDays = ChronoUnit.DAYS.between(record.getDueDate(), today);
            }

            result.add(new OverdueBorrowDto(
                    record.getBorrowCode(),
                    record.getBorrower() != null ? record.getBorrower().getFullName() : "Không xác định",
                    record.getBook() != null ? record.getBook().getTitle() : "Không xác định",
                    record.getDueDate(),
                    overdueDays
            ));
        }

        return result;
    }

    @Override
    public List<NewestBookDto> getNewestBooks() {
        List<Book> books = bookRepository.findTop5ByOrderByCreatedAtDesc();
        List<NewestBookDto> result = new ArrayList<>();

        for (Book book : books) {
            result.add(new NewestBookDto(
                    book.getId(),
                    book.getTitle(),
                    book.getCategory() != null ? book.getCategory().getName() : "Chưa phân loại",
                    book.getAuthor(),
                    book.getImportDate()
            ));
        }

        return result;
    }
}