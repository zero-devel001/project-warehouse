package com.sharsheev.ewarehouse.web;

import com.sharsheev.ewarehouse.model.OrderStatus;
import com.sharsheev.ewarehouse.model.ShoppingCart;
import com.sharsheev.ewarehouse.model.ShoppingCartProduct;
import com.sharsheev.ewarehouse.model.User;
import com.sharsheev.ewarehouse.service.OrderService;
import com.sharsheev.ewarehouse.service.ProductService;
import com.sharsheev.ewarehouse.service.ShoppingCartService;
import com.sharsheev.ewarehouse.service.UserService;
import mk.ukim.finki.wp.ewarehouse.model.*;
import mk.ukim.finki.wp.ewarehouse.service.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrdersController {
    private final ShoppingCartService shoppingCartService;
    private final OrderService orderService;
    private final ProductService productService;
    private final UserService userService;

    public OrdersController(ShoppingCartService shoppingCartService, OrderService orderService,
                            ProductService productService, UserService userService) {
        this.shoppingCartService = shoppingCartService;
        this.orderService = orderService;
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/checkout")
    public String checkout(Model model, HttpServletRequest request) {
        User user = this.userService.getUser(request.getRemoteUser());
        ShoppingCart cart = this.shoppingCartService.getShoppingCart(request.getRemoteUser());
        List<ShoppingCartProduct> products = cart.getShoppingCartProducts();
        Double totalPrice = 0.0;
        for (ShoppingCartProduct product : products) {
            totalPrice += product.getQuantity() * product.getProduct().getPrice();
        }
        model.addAttribute("products", products);
        model.addAttribute("bodyContent", "checkout");
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("productNames", this.productService.getProductNames());
        model.addAttribute("user", user);
        return "master-template";
    }

    @PostMapping("/checkout")
    public String placeOrder(@RequestParam String firstName, @RequestParam String lastName,
                             @RequestParam String address, @RequestParam String email,
                             @RequestParam String country, @RequestParam String city,
                             @RequestParam String zipCode, @RequestParam Double totalPrice,
                             HttpServletRequest request) {
        ShoppingCart cart = this.shoppingCartService.getShoppingCart(request.getRemoteUser());
        List<ShoppingCartProduct> products = cart.getShoppingCartProducts();
        List<Long> productsList = new ArrayList<>();
        for (ShoppingCartProduct product : products) { productsList.add(product.getId()); }
        this.shoppingCartService.deleteAllProducts(cart.getId());
        this.orderService.placeOrder(firstName, lastName, email, address, country, city, zipCode, totalPrice,
                productsList, request.getRemoteUser());
        return "redirect:/";
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showOrders(Model model) {
        model.addAttribute("orders", this.orderService.listAll());
        model.addAttribute("bodyContent", "orders");
        return "master-template";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editOrder(@PathVariable Long id, Model model) {
        model.addAttribute("order", this.orderService.findById(id));
        model.addAttribute("orderStatuses", OrderStatus.values());
        model.addAttribute("bodyContent", "orderedit");
        return "master-template";
    }

    @PostMapping("/edit/{id}")
    public String changeOrderDetails(@PathVariable Long id, @RequestParam(required = false) String username,
                                     @RequestParam String firstName, @RequestParam String lastName,
                                     @RequestParam String address, @RequestParam String email,
                                     @RequestParam String city, @RequestParam String country,
                                     @RequestParam String zipcode, @RequestParam OrderStatus status) {
        this.orderService.updateOrder(id, firstName, lastName, address, email, city, country, zipcode, status);
        return "redirect:/orders";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        this.orderService.delete(id);
        return "redirect:/orders";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/myorders")
    public String showMyOrders(Model model, HttpServletRequest request) {
        model.addAttribute("orders", this.userService.listAllUserOrders(this.userService.getUser(request.
                getRemoteUser()).getUsername()));
        model.addAttribute("bodyContent", "myorders");
        return "master-template";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/myorders/{id}")
    public String showSpecificOrder(@PathVariable Long id, Model model) {
        model.addAttribute("order", this.orderService.findById(id));
        model.addAttribute("pending", OrderStatus.AWAITING);
        model.addAttribute("bodyContent", "userorder");
        return "master-template";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/myorders/{id}")
    public String userOrderDetailsChange(@PathVariable Long id, @RequestParam(required = false) String username,
                                         @RequestParam String firstName, @RequestParam String lastName,
                                         @RequestParam String address, @RequestParam String email,
                                         @RequestParam String city, @RequestParam String country,
                                         @RequestParam String zipcode, @RequestParam(required = false)
                                                 OrderStatus status) {
        this.orderService.updateOrder(id, firstName, lastName, address, email, city, country, zipcode, OrderStatus.AWAITING);
        return "redirect:/orders/myorders";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/myorders/{id}/cancel")
    public String cancelOrder(@PathVariable Long id) {
        this.orderService.cancelOrder(id);
        return "redirect:/orders/myorders";
    }

}
