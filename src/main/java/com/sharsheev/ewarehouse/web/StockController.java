package com.sharsheev.ewarehouse.web;

import com.sharsheev.ewarehouse.service.ProductInWarehouseService;
import com.sharsheev.ewarehouse.service.ProductService;
import com.sharsheev.ewarehouse.service.WarehouseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/stock")
public class StockController {
    private final ProductService productService;
    private final WarehouseService warehouseService;
    private final ProductInWarehouseService productInWarehouseService;

    public StockController(ProductService productService, WarehouseService warehouseService,
                           ProductInWarehouseService productInWarehouseService) {
        this.productService = productService;
        this.warehouseService = warehouseService;
        this.productInWarehouseService = productInWarehouseService;
    }

    @GetMapping
    public String getAddNewStock(Model model) {
        model.addAttribute("products", this.productService.listAll());
        model.addAttribute("bodyContent", "stock");
        model.addAttribute("warehouses", this.warehouseService.listAll());
        model.addAttribute("productNames", this.productService.getProductNames());
        return "master-template";
    }

    @PostMapping
    public String addNewStock(@RequestParam Integer quantity,
                              @RequestParam Long warehouse,
                              @RequestParam Long product) {
        this.productInWarehouseService.addProducts(quantity, this.warehouseService.findById(warehouse),
                this.productService.findbyId(product));
        return "redirect:/";
    }
}
