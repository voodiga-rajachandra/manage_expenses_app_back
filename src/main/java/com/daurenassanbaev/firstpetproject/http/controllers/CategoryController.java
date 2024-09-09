package com.daurenassanbaev.firstpetproject.http.controllers;

import com.daurenassanbaev.firstpetproject.dto.CategoryDto;
import com.daurenassanbaev.firstpetproject.service.CategoryService;
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

@Slf4j
@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public String getCategories(@AuthenticationPrincipal UserDetails user, Model model) {
        var list1 = categoryService.findAllIncomes();
        var list2 = categoryService.findAllExpenses();
        if (!list1.isEmpty() && !list2.isEmpty()) {
            model.addAttribute("incomeCategories", list1);
            model.addAttribute("expenseCategories", list2);
            log.info("Received categories page by user: {}", user.getUsername());
            return "category/categories";
        } else {
            log.warn("Failed to get categories page by user: {}", user.getUsername());
            return "menu/main";
        }
    }

    @GetMapping("/add")
    public String addCategoryPage(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("category", new CategoryDto());
        log.info("Received add category page by user: {}", user.getUsername());
        return "category/addCategory";
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Integer id, @AuthenticationPrincipal UserDetails user) {
        try {
            categoryService.delete(id);
            log.info("Successfully deleted category by user {}", user.getUsername());
            return "redirect:/categories";
        } catch (Exception e) {
            log.error("Failed to delete category by user {}: {}", user.getUsername(), e.getMessage());
            return "redirect:/categories?error=true";
        }
    }

    @PostMapping("/add")
    public String addCategory(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute("category") @Validated CategoryDto categoryDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/categories/add";
        }
        try {
            categoryService.save(categoryDto);
            log.info("New category created by user {}: {}", userDetails.getUsername(), categoryDto.getCategoryName());
            return "redirect:/categories";
        } catch (ExistsException e) {
            List<ObjectError> errors = new ArrayList<>();
            errors.add(new ObjectError("error", e.getMessage()));
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:/categories/add";
        }
        catch (Exception e) {
            log.error("Failed to add category by user {}: {}", userDetails.getUsername(), e.getMessage());
            return "redirect:/categories?error=true";
        }
    }
}
