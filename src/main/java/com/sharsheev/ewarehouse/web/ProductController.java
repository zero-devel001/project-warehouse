package com.sharsheev.ewarehouse.web;

import com.sharsheev.ewarehouse.model.Product;
import com.sharsheev.ewarehouse.service.CategoryService;
import com.sharsheev.ewarehouse.service.ProductService;
import com.sharsheev.ewarehouse.service.ManufacturerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping({"/products", "/"})
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ManufacturerService manufacturerService;

    public ProductController(ProductService productService,
                             CategoryService categoryService,
                             ManufacturerService manufacturerService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.manufacturerService = manufacturerService;
    }

    @GetMapping
    public String getProducts(@RequestParam(required = false) String search,
                              @RequestParam(required = false) Long category,
                              @RequestParam(required = false) Long manufacturer,
                              @RequestParam(required = false) Boolean availability,
                              Model model) {
        if (search != null && !search.isEmpty()) {
            model.addAttribute("products", this.productService.searchProductsByName(search));
        } else {
            model.addAttribute("products", this.productService.
                    getProductsByCategoryManufacturerAndAvailability(category, manufacturer, availability));
        }
        model.addAttribute("bodyContent", "products");
        model.addAttribute("categories", this.categoryService.listAll());
        model.addAttribute("manufacturers", this.manufacturerService.listAll());
        model.addAttribute("productNames", this.productService.getProductNames());
        return "master-template";
    }

    @GetMapping("/{id}")
    public String getProductInfo(@PathVariable Long id, Model model) {
        Product product = this.productService.findbyId(id);
        model.addAttribute("bodyContent", "productInfo");
        model.addAttribute("product", product);
        model.addAttribute("productNames", this.productService.getProductNames());
        return "master-template";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/add")
    public String addNewProductPage(Model model) {
        model.addAttribute("bodyContent", "add-product");
        model.addAttribute("categories", this.categoryService.listAll());
        model.addAttribute("manufacturers", this.manufacturerService.listAll());
        model.addAttribute("productNames", this.productService.getProductNames());
        return "master-template";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/edit/{id}")
    public String editNewProductPage(@PathVariable Long id, Model model) {
        model.addAttribute("bodyContent", "add-product");
        model.addAttribute("product", this.productService.findbyId(id));
        model.addAttribute("categories", this.categoryService.listAll());
        model.addAttribute("manufacturers", this.manufacturerService.listAll());
        model.addAttribute("productNames", this.productService.getProductNames());
        return "master-template";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public String saveProduct(@RequestParam(required = false) Long id, @RequestParam String name,
                              @RequestParam Double price, @RequestParam String description,
                              @RequestParam String imageUrl, @RequestParam(required = false) Boolean availability,
                              @RequestParam Long category, @RequestParam Long manufacturer) {
        if (id != null) {
            this.productService.update(id, name, description, imageUrl, availability, price, category, manufacturer);
        } else {
            this.productService.create(name, description, imageUrl, false, price, category, manufacturer);
        }
        return "redirect:/products";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        this.productService.delete(id);
        return "redirect:/products";
    }
}