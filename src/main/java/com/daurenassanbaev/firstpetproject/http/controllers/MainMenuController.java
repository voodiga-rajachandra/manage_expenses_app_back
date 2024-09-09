package com.daurenassanbaev.firstpetproject.http.controllers;

import com.daurenassanbaev.firstpetproject.database.entity.User;
import com.daurenassanbaev.firstpetproject.dto.AccountDto;
import com.daurenassanbaev.firstpetproject.dto.CategoryDto;
import com.daurenassanbaev.firstpetproject.dto.TransactionDto;
import com.daurenassanbaev.firstpetproject.dto.UserDto;
import com.daurenassanbaev.firstpetproject.service.AccountService;
import com.daurenassanbaev.firstpetproject.service.CategoryService;
import com.daurenassanbaev.firstpetproject.service.TransactionService;
import com.daurenassanbaev.firstpetproject.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MainMenuController {
    private final UserService userService;
    private final AccountService accountService;
    private final CategoryService categoryService;
    private final TransactionService transactionService;
    @GetMapping
    public String mainMenu(@AuthenticationPrincipal UserDetails user, Model model, @ModelAttribute AccountDto accountDto) {
        String username = user.getUsername();
        Optional<UserDto> userOptional = userService.findByUsername(username);

        return userOptional.map(user1 -> {
            log.info("User found: {}", userOptional.get().getUsername());
            List<AccountDto> list = user1.getAccounts();
            if (list.isEmpty()) {
                log.info("Received welcome page by user {}", user.getUsername());
                model.addAttribute("account", accountDto);
                model.addAttribute("username", user1.getUsername());
                return "menu/welcomePage";
            } else {
                log.info("Received main page by user {}", user.getUsername());
                AccountDto accountDto1 = accountService.findByOwnerMain(user1.getId()).get();
                model.addAttribute("account", accountDto1);
                return "menu/main";
            }
        }).orElseGet(() -> {
            log.info("User not found: {}", username);
            model.addAttribute("account", accountDto);
            return "user/login";
        });

    }
    @GetMapping("/income")
    public String addIncome(@AuthenticationPrincipal UserDetails user, Model model) {
        // get user
        Optional<UserDto> userDto = userService.findByUsername(user.getUsername());
        if (userDto.isPresent()) {
            Integer userId = userDto.get().getId();
            model.addAttribute("transaction", new TransactionDto());
            model.addAttribute("categories", categoryService.findAllIncomes());
            model.addAttribute("accounts", accountService.findByUserId(userId));
            log.info("Received add income page by user {}", user.getUsername());
            return "menu/addIncome";
        } else {
            log.warn("Failed to receiving income page. User not found : {}", user.getUsername());
            return "menu/main";
        }
    }
    @PostMapping("/income")
    public String addIncome(@ModelAttribute @Validated TransactionDto transaction, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/menu/income";
        }
        transactionService.saveIncome(transaction);
        log.info("New income transaction saved: {}", transaction);
        return "redirect:/menu";
    }

    @GetMapping("/expense")
    public String addExpense(@AuthenticationPrincipal UserDetails user, Model model) {
        Optional<UserDto> userDto = userService.findByUsername(user.getUsername());
        if (userDto.isPresent()) {
            Integer userId = userDto.get().getId();
            model.addAttribute("transaction", new TransactionDto());
            model.addAttribute("categories", categoryService.findAllExpenses());
            model.addAttribute("accounts", accountService.findByUserId(userId));
            log.info("Received add expense page by user {}", user.getUsername());
            return "menu/addExpense";
        } else {
            log.warn("Failed to receiving expense page. User not found : {}", user.getUsername());
            return "redirect:/menu";
        }
        // add atributes

    }
    @PostMapping("/expense")
    public String addExpense(@ModelAttribute @Validated TransactionDto transaction, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        System.out.println(transaction.getImage());
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/menu/expense";
        }
        transactionService.saveExpense(transaction);
        log.info("New expense transaction saved: {}", transaction);
        return "redirect:/menu";
    }
    @PostMapping("/welcome")
    public String createAccount(@ModelAttribute @Validated AccountDto accountDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, @RequestParam String username) {
        if (bindingResult.hasErrors() && accountDto.getAccountName() != null) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/welcome";
        }
        accountDto.setAccountName("Main");
        accountService.createMain(accountDto, username);
        log.info("New account created: '{}' create for user '{}'", accountDto.getAccountName(), username);
        return "redirect:/menu";
    }

    @PostMapping("/deleteUser")
    public String deleteProfile(@AuthenticationPrincipal UserDetails user, Model model) {
        Integer id= userService.findByUsername(user.getUsername()).get().getId();
        userService.delete(id);
        return "user/login";
    }
}
