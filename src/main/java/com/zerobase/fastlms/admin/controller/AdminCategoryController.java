package com.zerobase.fastlms.admin.controller;

import com.zerobase.fastlms.admin.dto.CategoryDto;
import com.zerobase.fastlms.admin.model.CategoryInput;
import com.zerobase.fastlms.admin.repository.CategoryRepository;
import com.zerobase.fastlms.admin.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;

    @GetMapping("/admin/category/list")
    public String list(Model model) {
        List<CategoryDto> categoryDtoList = categoryService.getList();
        model.addAttribute("list", categoryDtoList);

        return "admin/category/list";
    }

    @PostMapping("/admin/category/add")
    public String add(Model model, CategoryInput categoryInput) {
        boolean result = categoryService.add(categoryInput.getCategoryName());

        return "redirect:/admin/category/list";
    }

    @PostMapping("/admin/category/delete")
    public String delete(Model model, CategoryInput categoryInput) {
        boolean result = categoryService.delete(categoryInput.getId());

        return "redirect:/admin/category/list";
    }

    @PostMapping("/admin/category/update")
    public String update(Model model, CategoryInput categoryInput) {
        boolean result = categoryService.update(categoryInput);

        return "redirect:/admin/category/list";
    }
}
