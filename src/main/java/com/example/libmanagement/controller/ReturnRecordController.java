package com.example.libmanagement.controller;

import com.example.libmanagement.entity.BorrowRecord;
import com.example.libmanagement.entity.Employee;
import com.example.libmanagement.entity.ReturnRecord;
import com.example.libmanagement.enums.BookCondition;
import com.example.libmanagement.enums.BorrowStatus;
import com.example.libmanagement.enums.FineReason;
import com.example.libmanagement.enums.PaymentStatus;
import com.example.libmanagement.enums.ReturnStatus;
import com.example.libmanagement.repository.BorrowRecordRepository;
import com.example.libmanagement.repository.EmployeeRepository;
import com.example.libmanagement.service.ReturnRecordService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/return-records")
public class ReturnRecordController {

    private final ReturnRecordService returnRecordService;
    private final BorrowRecordRepository borrowRecordRepository;
    private final EmployeeRepository employeeRepository;

    public ReturnRecordController(ReturnRecordService returnRecordService,
                                  BorrowRecordRepository borrowRecordRepository,
                                  EmployeeRepository employeeRepository) {
        this.returnRecordService = returnRecordService;
        this.borrowRecordRepository = borrowRecordRepository;
        this.employeeRepository = employeeRepository;
    }

    private Employee getCurrentEmployee() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getName() == null) {
            throw new RuntimeException("Không xác định được người đăng nhập.");
        }

        String username = auth.getName();

        return employeeRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên: " + username));
    }

    @GetMapping
    public String list(@RequestParam(value = "keyword", required = false) String keyword,
                       Model model) {
        model.addAttribute("keyword", keyword);
        model.addAttribute("returnRecords", returnRecordService.searchByBorrowerName(keyword));
        return "return-records/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        ReturnRecord returnRecord = new ReturnRecord();
        returnRecord.setBorrowRecord(new BorrowRecord());
        returnRecord.setBookCondition(BookCondition.INTACT);

        model.addAttribute("returnRecord", returnRecord);
        model.addAttribute("borrowRecords",
                borrowRecordRepository.findByStatusOrderByBorrowDateDesc(BorrowStatus.BORROWED));
        model.addAttribute("bookConditions", BookCondition.values());

        return "return-records/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("returnRecord") ReturnRecord returnRecord,
                      Model model) {
        try {
            // 🔥 FIX CHÍNH
            Employee employee = getCurrentEmployee();
            returnRecord.setEmployee(employee);

            returnRecordService.save(returnRecord);
            return "redirect:/return-records";

        } catch (Exception e) {
            if (returnRecord.getBorrowRecord() == null) {
                returnRecord.setBorrowRecord(new BorrowRecord());
            }

            model.addAttribute("returnRecord", returnRecord);
            model.addAttribute("borrowRecords",
                    borrowRecordRepository.findByStatusOrderByBorrowDateDesc(BorrowStatus.BORROWED));
            model.addAttribute("bookConditions", BookCondition.values());
            model.addAttribute("errorMessage", e.getMessage());

            return "return-records/add";
        }
    }

    @PostMapping("/quick-confirm")
    public String quickConfirm(@RequestParam("borrowCode") String borrowCode,
                               @RequestParam(value = "bookCondition", defaultValue = "INTACT") BookCondition bookCondition,
                               RedirectAttributes redirectAttributes) {
        try {
            BorrowRecord borrowRecord = borrowRecordRepository.findByBorrowCode(borrowCode.trim())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu mượn"));

            ReturnRecord returnRecord = new ReturnRecord();
            returnRecord.setBorrowRecord(borrowRecord);
            returnRecord.setReturnDate(LocalDate.now());
            returnRecord.setBookCondition(bookCondition);

            // 🔥 FIX CHÍNH
            returnRecord.setEmployee(getCurrentEmployee());

            ReturnRecord saved = returnRecordService.save(returnRecord);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Xác nhận trả nhanh thành công: " + saved.getReturnCode());

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/return-records";
    }
}