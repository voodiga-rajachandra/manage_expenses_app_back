package com.daurenassanbaev.firstpetproject.http.controllers;

import com.daurenassanbaev.firstpetproject.dto.AccountDto;
import com.daurenassanbaev.firstpetproject.dto.AccountTransferDto;
import com.daurenassanbaev.firstpetproject.dto.AccountTransferViewDto;
import com.daurenassanbaev.firstpetproject.dto.UserDto;
import com.daurenassanbaev.firstpetproject.service.AccountService;
import com.daurenassanbaev.firstpetproject.service.AccountTransferService;
import com.daurenassanbaev.firstpetproject.service.UserService;
import com.daurenassanbaev.firstpetproject.validation.group.ExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final UserService userService;
    private final AccountTransferService accountTransferService;

    @GetMapping
    public String accounts(@AuthenticationPrincipal UserDetails user, Model model) {
        Optional<UserDto> user1 = userService.findByUsername(user.getUsername());
        if(user1.isPresent()) {
            List<AccountDto> list = accountService.findAll(getCurrentUserId(user));
            if (!list.isEmpty()) {
                model.addAttribute("accounts", list);
                log.info("Received accounts page by user {}", user.getUsername());
                return "accounts/account";
            } else {
                log.warn("Failed to find user {} accounts. User doesn't have any accounts", user.getUsername());
                model.addAttribute("accounts", list);
                return "accounts/account";
            }
        } else {
            log.warn("Failed to receive accounts page by user {}", user.getUsername());
            return "menu/main";
        }
    }

    @GetMapping("/add")
    public String addAccountPage(Model model, @AuthenticationPrincipal UserDetails user) {
        model.addAttribute("account", new AccountDto());
        log.info("Received add account page with user {}", user.getUsername());
        return "accounts/addAccount";
    }

    @PostMapping("/add")
    public String addAccount(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute("account") @Validated AccountDto account, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/accounts/add";
        }
        try {
            accountService.create(account, userDetails.getUsername());
            log.info("Account added successfully by user {}", userDetails.getUsername());
            return "redirect:/accounts";
        } catch (ExistsException e) {
            List<ObjectError> errors = new ArrayList<>();
            errors.add(new ObjectError("error", e.getMessage()));
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:/accounts/add";
        } catch (Exception e) {
            log.warn("Failed to add account by user {}: {}", userDetails.getUsername(), e.getMessage());
            throw e;
        }
    }

    public Integer getCurrentUserId(UserDetails userDetails) {
        Optional<UserDto> user = userService.findByUsername(userDetails.getUsername());
        if (user.isPresent()) {
            return user.get().getId();
        }
        return -1;
    }

    @GetMapping("/transfer")
    public String createTransferPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Integer userId = getCurrentUserId(userDetails);
        List<AccountDto> list = accountService.findByUserId(userId);
        if (!list.isEmpty()) {
            model.addAttribute("accountTransfer", new AccountTransferDto());
            model.addAttribute("accounts", list);
            return "accounts/createTransfer";
        } else {
            log.warn("Failed to find account transfer page by user {}. User doesn't have any accounts", userDetails.getUsername());
            return "accounts/addAccount";
        }
    }

    @PostMapping("/transfer")
    public String createTransfer(@ModelAttribute @Validated AccountTransferDto accountTransferDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/transfer";
        }
        try {
            accountTransferService.save(accountTransferDto, userDetails.getUsername());
            log.info("Transfer successfully created by user {}", userDetails.getUsername());
            return "redirect:/accounts";
        } catch (Exception e) {
            log.error("Failed to save account transfer by user {}: {}", userDetails.getUsername(), e.getMessage());
            return "redirect:/accounts/transfer";
        }
    }

    @GetMapping("/history")
    public String historyPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<AccountTransferViewDto> list = accountTransferService.findAll(getCurrentUserId(userDetails));
        if (!list.isEmpty()) {
            model.addAttribute("accountTransfers", list);
            log.info("Received history page by user {}", userDetails.getUsername());
            return "accounts/history";
        } else {
            log.warn("Failed to receive history page by user {}", userDetails.getUsername());
            return "accounts/history";
        }
    }
}
