package com.daurenassanbaev.firstpetproject.http.controllers;

import com.daurenassanbaev.firstpetproject.dto.RegularPaymentDto;
import com.daurenassanbaev.firstpetproject.dto.UserDto;
import com.daurenassanbaev.firstpetproject.service.AccountService;
import com.daurenassanbaev.firstpetproject.service.CategoryService;
import com.daurenassanbaev.firstpetproject.service.RegularPaymentsService;
import com.daurenassanbaev.firstpetproject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
@RequestMapping("/payments")
@RequiredArgsConstructor
public class RegularPaymentsController {
    private final RegularPaymentsService regularPaymentsService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final AccountService accountService;

    @GetMapping("/regular")
    public String regularPaymentsPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Integer userId = getCurrentUserId(userDetails);
        List<RegularPaymentDto> list = regularPaymentsService.findAllByUserId(userId);
        log.info("Successfully retrieved regular payments page by user {}", userDetails.getUsername());
        model.addAttribute("regularPayments", list);
        return "regularPayments/regularPayments";
    }

    @GetMapping("/regular/new")
    public String addRegularPayment(@RequestParam("type") String type, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        var list = accountService.findAll(getCurrentUserId(userDetails));
        if (list.isEmpty()) {
            log.warn("Failed to receive add regular payment page by user {}. User doesn't have any accounts", userDetails.getUsername());
            return "redirect:/payments/regular";
        } else if (type == null) {
            log.warn("User {} didn't specify the type", userDetails.getUsername());
            return "redirect:/payments/regular";
        } else {
            model.addAttribute("regularPayment", new RegularPaymentDto());
            model.addAttribute("type", type);
            model.addAttribute("accounts", list);
            if (type.equals("expense")) {
                model.addAttribute("categories", categoryService.findAllExpenses());
            } else if (type.equals("income")) {
                model.addAttribute("categories", categoryService.findAllIncomes());
            }
            log.info("Successfully retrieved add regular payment page by user {}", userDetails.getUsername());
            return "regularPayments/addRegularPayment";
        }
    }

    @PostMapping("/regular/new")
    public String addRegularPayment(@AuthenticationPrincipal UserDetails userDetails,
                                    @ModelAttribute @Validated RegularPaymentDto regularPaymentDto,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes,
                                    @RequestParam("type") String type) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/payments/regular/new?type="+type;
        }
        if (type == null) {
            log.info("Failed to add regular payment by user {}", userDetails.getUsername());
            return "redirect:/payments/regular";
        } else {
            regularPaymentDto.setType(type);
            try {
                regularPaymentsService.save(regularPaymentDto);
                log.info("Successfully added regular payment by user {}", userDetails.getUsername());
                return "redirect:/payments/regular";
            } catch (Exception e) {
                log.error("Failed to add regular payment by user {}", userDetails.getUsername(), e);
                throw e;
            }
        }
    }

    private Integer getCurrentUserId(UserDetails userDetails) {
        Optional<UserDto> user = userService.findByUsername(userDetails.getUsername());
        return user.map(UserDto::getId).orElse(-1);
    }
}
