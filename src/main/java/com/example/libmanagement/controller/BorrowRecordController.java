package com.example.libmanagement.controller;

import com.example.libmanagement.entity.BorrowRecord;
import com.example.libmanagement.enums.BorrowStatus;
import com.example.libmanagement.service.BookService;
import com.example.libmanagement.service.BorrowRecordService;
import com.example.libmanagement.service.BorrowerService;
import com.example.libmanagement.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/borrow-records")
public class BorrowRecordController {

    private final BorrowRecordService borrowRecordService;
    private final BorrowerService borrowerService;
    private final EmployeeService employeeService;
    private final BookService bookService;

    public BorrowRecordController(BorrowRecordService borrowRecordService,
                                  BorrowerService borrowerService,
                                  EmployeeService employeeService,
                                  BookService bookService) {
        this.borrowRecordService = borrowRecordService;
        this.borrowerService = borrowerService;
        this.employeeService = employeeService;
        this.bookService = bookService;
    }

    @GetMapping
    public String listBorrowRecords(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<BorrowRecord> borrowRecords;
        if (keyword != null && !keyword.trim().isEmpty()) {
            borrowRecords = borrowRecordService.searchByBorrowerName(keyword);
        } else {
            borrowRecords = borrowRecordService.findAll();
        }
        model.addAttribute("borrowRecords", borrowRecords);
        model.addAttribute("keyword", keyword);
        return "borrow-records/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("borrowRecord", new BorrowRecord());
        model.addAttribute("borrowers", borrowerService.findAll());
        model.addAttribute("employees", employeeService.findAll());
        model.addAttribute("books", bookService.findAll());
        model.addAttribute("statuses", Arrays.asList(BorrowStatus.values()));
        return "borrow-records/add";
    }

    @PostMapping("/save")
    public String saveBorrowRecord(@ModelAttribute("borrowRecord") BorrowRecord borrowRecord) {
        borrowRecordService.save(borrowRecord);
        return "redirect:/borrow-records";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        BorrowRecord borrowRecord = borrowRecordService.findById(id);
        if (borrowRecord == null) {
            return "redirect:/borrow-records";
        }
        model.addAttribute("borrowRecord", borrowRecord);
        model.addAttribute("borrowers", borrowerService.findAll());
        model.addAttribute("employees", employeeService.findAll());
        model.addAttribute("books", bookService.findAll());
        model.addAttribute("statuses", Arrays.asList(BorrowStatus.values()));
        return "borrow-records/edit";
    }

    @PostMapping("/update/{id}")
    public String updateBorrowRecord(@PathVariable Long id, @ModelAttribute("borrowRecord") BorrowRecord borrowRecord) {
        borrowRecordService.update(id, borrowRecord);
        return "redirect:/borrow-records";
    }

    @GetMapping("/detail/{id}")
    public String viewDetail(@PathVariable Long id, Model model) {
        BorrowRecord borrowRecord = borrowRecordService.findById(id);
        if (borrowRecord == null) {
            return "redirect:/borrow-records";
        }
        model.addAttribute("borrowRecord", borrowRecord);
        return "borrow-records/detail";
    }

    @GetMapping("/delete/{id}")
    public String deleteBorrowRecord(@PathVariable Long id) {
        borrowRecordService.delete(id);
        return "redirect:/borrow-records";
    }
}
