package com.example.libmanagement.controller;

import com.example.libmanagement.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final DashboardService dashboardService;

    public HomeController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("summary", dashboardService.getSummary());
        model.addAttribute("overdueBorrows", dashboardService.getTopOverdueBorrows());
        model.addAttribute("newestBooks", dashboardService.getNewestBooks());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}