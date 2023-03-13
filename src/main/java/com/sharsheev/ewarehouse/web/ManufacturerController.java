package com.sharsheev.ewarehouse.web;

import com.sharsheev.ewarehouse.service.ProductService;
import com.sharsheev.ewarehouse.service.ManufacturerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/manufacturers")
public class ManufacturerController {
    private final ManufacturerService manufacturerService;
    private final ProductService productService;

    public ManufacturerController(ManufacturerService manufacturerService, ProductService productService) {
        this.manufacturerService = manufacturerService;
        this.productService = productService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("manufacturers", this.manufacturerService.listAll());
        model.addAttribute("bodyContent", "manufacturers");
        model.addAttribute("productNames", this.productService.getProductNames());
        return "master-template";
    }

    @PostMapping("/add")
    public String addNewManufacturer(@RequestParam String name) {
        this.manufacturerService.create(name);
        return "redirect:/manufacturers";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteManufacturer(@PathVariable Long id) {
        this.manufacturerService.delete(id);
        return "redirect:/manufacturers";
    }
}