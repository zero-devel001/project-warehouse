package com.sharsheev.ewarehouse.service.impl;

import com.sharsheev.ewarehouse.model.Product;
import com.sharsheev.ewarehouse.model.ShoppingCart;
import com.sharsheev.ewarehouse.model.ShoppingCartProduct;
import com.sharsheev.ewarehouse.model.User;
import com.sharsheev.ewarehouse.model.exceptions.InvalidProductIdException;
import com.sharsheev.ewarehouse.model.exceptions.InvalidShoppingCartIdException;
import com.sharsheev.ewarehouse.model.exceptions.InvalidUsernameOrPasswordException;
import com.sharsheev.ewarehouse.model.exceptions.UserNotFoundException;
import com.sharsheev.ewarehouse.repository.*;
import com.sharsheev.ewarehouse.service.ShoppingCartProductService;
import com.sharsheev.ewarehouse.service.ShoppingCartService;
import mk.ukim.finki.wp.ewarehouse.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ShoppingCartProductService shoppingCartProductService;
    private final ShoppingCartProductRepository shoppingCartProductRepository;
    private final WarehouseRepository warehouseRepository;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository, UserRepository userRepository,
                                   ProductRepository productRepository,
                                   ShoppingCartProductService shoppingCartProductService,
                                   ShoppingCartProductRepository shoppingCartProductRepository,
                                   WarehouseRepository warehouseRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.shoppingCartProductService = shoppingCartProductService;
        this.shoppingCartProductRepository = shoppingCartProductRepository;
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public ShoppingCart findById(Long id) {
        return this.shoppingCartRepository.findById(id).orElseThrow(InvalidShoppingCartIdException::new);
    }

    @Override
    public ShoppingCart deleteShoppingCart(Long id) {
        ShoppingCart shoppingCart = this.findById(id);
        User user = this.userRepository.findByUsername(shoppingCart.getUser().getUsername()).
                orElseThrow(InvalidUsernameOrPasswordException::new);
        user.setShoppingCart(null);
        this.userRepository.save(user);
        this.shoppingCartRepository.delete(shoppingCart);
        return shoppingCart;
    }

    @Override
    public ShoppingCart deleteProduct(ShoppingCartProduct product, Long id) {
        ShoppingCart cart = this.shoppingCartRepository.findById(id).orElseThrow(InvalidShoppingCartIdException::new);
        List<ShoppingCartProduct> productList = cart.getShoppingCartProducts();
        productList.remove(product);
        cart.setShoppingCartProducts(productList);
        this.shoppingCartProductRepository.delete(product);
        return this.shoppingCartRepository.save(cart);
    }

    @Override
    public ShoppingCart deleteAllProducts(Long id) {
        ShoppingCart cart = this.shoppingCartRepository.findById(id).orElseThrow(InvalidShoppingCartIdException::new);
        for (ShoppingCartProduct item : cart.getShoppingCartProducts()) {
            item.setShoppingCart(null);
        }
        cart.setShoppingCartProducts(new ArrayList<>());
        this.shoppingCartRepository.save(cart);
        return cart;
    }

    @Override
    public ShoppingCart getShoppingCart(String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException());
        return this.shoppingCartRepository
                .findByUser(user)
                .orElseGet(() -> {ShoppingCart cart = new ShoppingCart(user); user.setShoppingCart(cart);
                    cart = this.shoppingCartRepository.save(cart); this.userRepository.save(user);
                    return cart;
                });
    }

    @Override
    public List<Product> listAllProductsInShoppingCart(Long id) {
        ShoppingCart cart = this.shoppingCartRepository.findById(id).orElseThrow(InvalidShoppingCartIdException::new);
        return cart.getProducts();
    }

    @Override
    public List<ShoppingCartProduct> listAllProducts(Long id) {
        ShoppingCart cart = this.shoppingCartRepository.findById(id).orElseThrow(InvalidShoppingCartIdException::new);
        return cart.getShoppingCartProducts();
    }

    @Override
    public void addProductToShoppingCart(String username, Long id, Long fromWarehouse) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findByUser(this.userRepository.
                        findByUsername(username).orElseThrow(InvalidUsernameOrPasswordException::new)).
                orElseThrow(InvalidShoppingCartIdException::new);
        Product product = this.productRepository.findById(id).orElseThrow(InvalidProductIdException::new);
        Optional<ShoppingCartProduct> shoppingCartProductOpt = this.shoppingCartProductRepository.
                findByProductAndShoppingCartAndWarehouse(product, shoppingCart, this.warehouseRepository.
                        findById(fromWarehouse).get());
        if (shoppingCartProductOpt.isPresent()) {
            ShoppingCartProduct sci = shoppingCartProductOpt.get();
            Integer numberOfProductsInWarehouse = sci.getWarehouse().stockOfProduct(product.getId());
            if (numberOfProductsInWarehouse.equals(sci.getQuantity())) {
                return;
            }
            sci.setQuantity(sci.getQuantity() + 1);
            List<ShoppingCartProduct> list = shoppingCart.getShoppingCartProducts();
            list.add(sci);
            shoppingCart.setShoppingCartProducts(list);
            this.shoppingCartRepository.save(shoppingCart);
            return;
        }
        ShoppingCartProduct shoppingCartProduct = this.shoppingCartProductService.create(1,
                product, shoppingCart, fromWarehouse);
        List<ShoppingCartProduct> productList = shoppingCart.getShoppingCartProducts();
        productList.add(shoppingCartProduct);
        shoppingCart.setShoppingCartProducts(productList);
        this.shoppingCartRepository.save(shoppingCart);
    }
}
