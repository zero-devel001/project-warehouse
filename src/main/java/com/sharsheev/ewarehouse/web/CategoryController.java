package com.sharsheev.ewarehouse.web;

import com.sharsheev.ewarehouse.service.CategoryService;
import com.sharsheev.ewarehouse.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final ProductService productService;

    public CategoryController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("categories", this.categoryService.listAll());
        model.addAttribute("bodyContent", "categories");
        model.addAttribute("productNames", this.productService.getProductNames());
        return "master-template";
    }

    @PostMapping("/add")
    public String addNewCategory(@RequestParam String name) {
        this.categoryService.create(name);
        return "redirect:/categories";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        this.categoryService.delete(id);
        return "redirect:/categories";
    }
}