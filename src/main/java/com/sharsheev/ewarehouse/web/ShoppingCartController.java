package com.sharsheev.ewarehouse.web;

import com.sharsheev.ewarehouse.model.ShoppingCart;
import com.sharsheev.ewarehouse.model.ShoppingCartProduct;
import com.sharsheev.ewarehouse.service.ShoppingCartService;
import com.sharsheev.ewarehouse.service.ProductService;
import com.sharsheev.ewarehouse.service.ShoppingCartProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/shopping-cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final ProductService productService;
    private final ShoppingCartProductService shoppingCartProductService;

    public ShoppingCartController(ShoppingCartService shoppingCartService,
                                  ProductService productService,
                                  ShoppingCartProductService shoppingCartProductService) {
        this.shoppingCartService = shoppingCartService;
        this.productService = productService;
        this.shoppingCartProductService = shoppingCartProductService;
    }

    @GetMapping
    public String showShoppingCarts(HttpServletRequest req, Model model) {
        String username = req.getRemoteUser();
        ShoppingCart shoppingCart = this.shoppingCartService.getShoppingCart(username);
        model.addAttribute("products", this.shoppingCartService.listAllProducts(shoppingCart.getId()));
        model.addAttribute("cart", shoppingCart);
        model.addAttribute("bodyContent", "shopping-cart");
        model.addAttribute("productNames", this.productService.getProductNames());
        return "master-template";
    }

    @PostMapping("/add-product/{id}")
    public String addToShoppingCart(@PathVariable Long id, @RequestParam Long fromWarehouse, HttpServletRequest req) {
        try {
            String username = req.getRemoteUser();
            this.shoppingCartService.getShoppingCart(username);
            this.shoppingCartService.addProductToShoppingCart(username, id, fromWarehouse);
            return "redirect:/shopping-cart";
        } catch (RuntimeException exception) {
            return "redirect:/shopping-cart?error=" + exception.getMessage();
        }
    }

    @DeleteMapping("/deleteitem/{id}/{productid}")
    public String deleteItem(@PathVariable Long id, @PathVariable Long productid) {
        ShoppingCartProduct product = this.shoppingCartProductService.findById(productid);
        this.shoppingCartService.deleteProduct(product, id);
        return "redirect:/shopping-cart";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCart(@PathVariable Long id) {
        this.shoppingCartService.deleteAllProducts(id);
        return "redirect:/shopping-cart";
    }

    @PostMapping("/changequantity/{id}")
    public String changeQuantityOfProduct(@PathVariable Long id, @RequestParam Integer quantity) {
        this.shoppingCartProductService.updateQuantityOfProduct(id, quantity);
        return "redirect:/shopping-cart";
    }
}