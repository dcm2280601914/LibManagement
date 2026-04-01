package com.example.libmanagement.controller;

import com.example.libmanagement.entity.Borrower;
import com.example.libmanagement.service.BorrowerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/borrowers")
public class BorrowerController {

    private final BorrowerService borrowerService;

    public BorrowerController(BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    @GetMapping
    public String listBorrowers(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Borrower> borrowers;
        if (keyword != null && !keyword.trim().isEmpty()) {
            borrowers = borrowerService.searchByFullName(keyword);
        } else {
            borrowers = borrowerService.findAll();
        }
        model.addAttribute("borrowers", borrowers);
        model.addAttribute("keyword", keyword);
        return "borrowers/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("borrower", new Borrower());
        return "borrowers/add";
    }

    @PostMapping("/save")
    public String saveBorrower(@ModelAttribute("borrower") Borrower borrower) {
        borrowerService.save(borrower);
        return "redirect:/borrowers";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Borrower borrower = borrowerService.findById(id);
        if (borrower == null) {
            return "redirect:/borrowers";
        }
        model.addAttribute("borrower", borrower);
        return "borrowers/edit";
    }

    @PostMapping("/update/{id}")
    public String updateBorrower(@PathVariable Long id, @ModelAttribute("borrower") Borrower borrower) {
        borrowerService.update(id, borrower);
        return "redirect:/borrowers";
    }

    @GetMapping("/detail/{id}")
    public String viewDetail(@PathVariable Long id, Model model) {
        Borrower borrower = borrowerService.findById(id);
        if (borrower == null) {
            return "redirect:/borrowers";
        }
        model.addAttribute("borrower", borrower);
        return "borrowers/detail";
    }

    @GetMapping("/delete/{id}")
    public String deleteBorrower(@PathVariable Long id) {
        borrowerService.delete(id);
        return "redirect:/borrowers";
    }
}
