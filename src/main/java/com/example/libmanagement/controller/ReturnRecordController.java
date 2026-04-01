package com.example.libmanagement.controller;

import com.example.libmanagement.entity.ReturnRecord;
import com.example.libmanagement.enums.ReturnStatus;
import com.example.libmanagement.service.BorrowRecordService;
import com.example.libmanagement.service.EmployeeService;
import com.example.libmanagement.service.ReturnRecordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/return-records")
public class ReturnRecordController {

    private final ReturnRecordService returnRecordService;
    private final BorrowRecordService borrowRecordService;
    private final EmployeeService employeeService;

    public ReturnRecordController(ReturnRecordService returnRecordService,
                                  BorrowRecordService borrowRecordService,
                                  EmployeeService employeeService) {
        this.returnRecordService = returnRecordService;
        this.borrowRecordService = borrowRecordService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public String listReturnRecords(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<ReturnRecord> returnRecords;
        if (keyword != null && !keyword.trim().isEmpty()) {
            returnRecords = returnRecordService.searchByBorrowerName(keyword);
        } else {
            returnRecords = returnRecordService.findAll();
        }
        model.addAttribute("returnRecords", returnRecords);
        model.addAttribute("keyword", keyword);
        return "return-records/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("returnRecord", new ReturnRecord());
        model.addAttribute("borrowRecords", borrowRecordService.findAll());
        model.addAttribute("employees", employeeService.findAll());
        model.addAttribute("statuses", Arrays.asList(ReturnStatus.values()));
        return "return-records/add";
    }

    @PostMapping("/save")
    public String saveReturnRecord(@ModelAttribute("returnRecord") ReturnRecord returnRecord) {
        returnRecordService.save(returnRecord);
        return "redirect:/return-records";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        ReturnRecord returnRecord = returnRecordService.findById(id);
        if (returnRecord == null) {
            return "redirect:/return-records";
        }
        model.addAttribute("returnRecord", returnRecord);
        model.addAttribute("borrowRecords", borrowRecordService.findAll());
        model.addAttribute("employees", employeeService.findAll());
        model.addAttribute("statuses", Arrays.asList(ReturnStatus.values()));
        return "return-records/edit";
    }

    @PostMapping("/update/{id}")
    public String updateReturnRecord(@PathVariable Long id, @ModelAttribute("returnRecord") ReturnRecord returnRecord) {
        returnRecordService.update(id, returnRecord);
        return "redirect:/return-records";
    }

    @GetMapping("/detail/{id}")
    public String viewDetail(@PathVariable Long id, Model model) {
        ReturnRecord returnRecord = returnRecordService.findById(id);
        if (returnRecord == null) {
            return "redirect:/return-records";
        }
        model.addAttribute("returnRecord", returnRecord);
        return "return-records/detail";
    }

    @GetMapping("/delete/{id}")
    public String deleteReturnRecord(@PathVariable Long id) {
        returnRecordService.delete(id);
        return "redirect:/return-records";
    }
}
