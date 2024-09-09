package com.daurenassanbaev.firstpetproject.http.controllers;

import com.daurenassanbaev.firstpetproject.service.AccountService;
import com.daurenassanbaev.firstpetproject.service.TransactionService;
import com.daurenassanbaev.firstpetproject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Controller
@Slf4j
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionsController {
    private final TransactionService transactionService;
    private final UserService userService;
    private final AccountService accountService;

    @GetMapping
    public String getTransactions(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page, @RequestParam(value = "size", required = false, defaultValue = "5") Integer size, @RequestParam(value = "sort", required = false) String sort, @RequestParam(value = "sort_mode", required = false) String sortMode, @RequestParam(value = "account_name", required = false) String accountName, @RequestParam(value = "min_amount", required = false) BigDecimal minAmount, @RequestParam(value = "max_amount", required = false) BigDecimal maxAmount, @RequestParam(value = "account", required = false) String account, @RequestParam(value = "from_date", required = false) LocalDateTime fromDate, @RequestParam(value = "to_date", required = false) LocalDateTime toDate, @RequestParam(value = "type", required = false) String type, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        sort = "," + sortMode;
        Integer userId = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"))
                .getId();
        var pageRequest = PageRequest.of(page, size);
        if (type == null) {
            var list1 = transactionService.findAllByCategoryName("expense", userId, fromDate, toDate, minAmount, maxAmount, sort, accountName, pageRequest);
            var list2 = transactionService.findAllByCategoryName("income", userId, fromDate, toDate, minAmount, maxAmount, sort, accountName, pageRequest);
            if (list1.isEmpty() && list2.isEmpty()) {
                log.info("No transactions found for user {}", userDetails.getUsername());
            } else {
                log.info("Successfully retrieved transactions page by user {}", userDetails.getUsername());
            }
            model.addAttribute("expenseTransactions", list1);
            model.addAttribute("incomeTransactions", list2);
            model.addAttribute("currentPage", pageRequest.getPageNumber());
            model.addAttribute("totalPages", list1.getTotalPages());
            model.addAttribute("pageSize", size);
            model.addAttribute("accounts", accountService.findAll(userId));
            return "transactions/allTransactions";
        } else {
            if (type.equals("expense")) {
                var list1 = transactionService.findAllByCategoryName("expense", userId, fromDate, toDate, minAmount, maxAmount, sort, accountName, pageRequest);
                if (list1.isEmpty()) {
                    log.info("No expense transactions found for user {}", userDetails.getUsername());
                } else {
                    log.info("Successfully retrieved expense transactions page by user {}", userDetails.getUsername());
                }
                model.addAttribute("expenseTransactions", list1);
                model.addAttribute("currentPage", pageRequest.getPageNumber());
                model.addAttribute("totalPages", list1.getTotalPages());
                model.addAttribute("pageSize", size);
                return "transactions/allTransactions";
            } else if (type.equals("income")) {
                var list1 = transactionService.findAllByCategoryName("income", userId, fromDate, toDate, minAmount, maxAmount, sort, accountName, pageRequest);
                if (list1.isEmpty()) {
                    log.info("No income transactions found for user {}", userDetails.getUsername());
                } else {
                    log.info("Successfully retrieved income transactions page by user {}", userDetails.getUsername());
                }
                model.addAttribute("incomeTransactions", list1);
                model.addAttribute("currentPage", pageRequest.getPageNumber());
                model.addAttribute("totalPages", list1.getTotalPages());
                model.addAttribute("pageSize", size);
                return "transactions/allTransactions";
            }
        }
        return "transactions/allTransactions";
    }
}
