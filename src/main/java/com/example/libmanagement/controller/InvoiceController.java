package com.example.libmanagement.controller;

import com.example.libmanagement.entity.Invoice;
import com.example.libmanagement.enums.InvoiceStatus;
import com.example.libmanagement.enums.PaymentMethod;
import com.example.libmanagement.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public String listInvoices(Model model) {
        List<Invoice> invoices = invoiceService.findAll();
        model.addAttribute("invoices", invoices);
        model.addAttribute("totalInvoices", invoices.size());
        model.addAttribute("paymentMethods", Arrays.asList(PaymentMethod.values()));
        model.addAttribute("invoiceStatuses", Arrays.asList(InvoiceStatus.values()));
        return "invoices/list";
    }

    @GetMapping("/{id}")
    public String viewInvoiceDetail(@PathVariable Long id, Model model) {
        Invoice invoice = invoiceService.findById(id);
        if (invoice == null) {
            return "redirect:/invoices";
        }

        model.addAttribute("invoice", invoice);
        model.addAttribute("paymentMethods", Arrays.asList(PaymentMethod.values()));
        return "invoices/detail";
    }

    @PostMapping("/{id}/pay")
    public String payInvoice(@PathVariable Long id,
                             @RequestParam("paymentMethod") PaymentMethod paymentMethod,
                             @RequestParam(value = "note", required = false) String note,
                             Model model) {
        try {
            invoiceService.payInvoice(id, paymentMethod, note);
            return "redirect:/invoices/" + id + "?success=paid";
        } catch (Exception e) {
            Invoice invoice = invoiceService.findById(id);
            model.addAttribute("invoice", invoice);
            model.addAttribute("paymentMethods", Arrays.asList(PaymentMethod.values()));
            model.addAttribute("errorMessage", e.getMessage());
            return "invoices/detail";
        }
    }

    @PostMapping("/{id}/cancel")
    public String cancelInvoice(@PathVariable Long id,
                                @RequestParam(value = "note", required = false) String note,
                                Model model) {
        try {
            invoiceService.cancelInvoice(id, note);
            return "redirect:/invoices/" + id + "?success=cancelled";
        } catch (Exception e) {
            Invoice invoice = invoiceService.findById(id);
            model.addAttribute("invoice", invoice);
            model.addAttribute("paymentMethods", Arrays.asList(PaymentMethod.values()));
            model.addAttribute("errorMessage", e.getMessage());
            return "invoices/detail";
        }
    }

    @GetMapping("/{id}/receipt")
    public String printReceipt(@PathVariable Long id, Model model) {
        Invoice invoice = invoiceService.findById(id);
        if (invoice == null) {
            return "redirect:/invoices";
        }

        model.addAttribute("invoice", invoice);
        return "invoices/receipt";
    }
}