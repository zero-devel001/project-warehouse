package com.sharsheev.ewarehouse.web;

import com.sharsheev.ewarehouse.service.ProductService;
import com.sharsheev.ewarehouse.service.WarehouseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/warehouses")
public class WarehouseController {
    private final WarehouseService warehouseService;
    private final ProductService productService;

    public WarehouseController(WarehouseService warehouseService, ProductService productService) {
        this.warehouseService = warehouseService;
        this.productService = productService;
    }

    @GetMapping
    public String getWarehouses(Model model) {
        model.addAttribute("bodyContent", "warehouses");
        model.addAttribute("warehouses", this.warehouseService.listAll());
        model.addAttribute("productNames", this.productService.getProductNames());
        return "master-template";
    }

    @PostMapping("/add")
    public String addNewWarehouse(@RequestParam String name) {
        this.warehouseService.create(name);
        return "redirect:/warehouses";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteManufacturer(@PathVariable Long id) {
        this.warehouseService.delete(id);
        return "redirect:/warehouses";
    }
}