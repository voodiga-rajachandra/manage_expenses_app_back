package com.daurenassanbaev.firstpetproject.http.controllers;

import com.daurenassanbaev.firstpetproject.dto.UserDto;
import com.daurenassanbaev.firstpetproject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;
    @GetMapping
    public String registration(@AuthenticationPrincipal UserDetails user, Model model, @ModelAttribute UserDto userDto) {
        if (user != null) {
            log.warn("Authenticated user {} attempted to access registration page", user.getUsername());
            return "menu/main";
        } else {
            model.addAttribute("user", userDto);
            log.info("Received registration page");
            return "user/registration";
        }

    }
    @PostMapping
    public String create(@ModelAttribute @Validated UserDto userDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/registration";
        }
        try {
            userService.create(userDto);
            log.info("New user successfully created by user: {}", userDto.getUsername());
            return "redirect:/login";
        } catch (Exception e) {
            log.error("Failed to create new user {}: {}", userDto.getUsername(), e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Failed to create user. Please try again.");
            return "redirect:/registration";
        }

    }
}
