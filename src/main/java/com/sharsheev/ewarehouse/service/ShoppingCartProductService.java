package com.sharsheev.ewarehouse.service;

import com.sharsheev.ewarehouse.model.Product;
import com.sharsheev.ewarehouse.model.ShoppingCart;
import com.sharsheev.ewarehouse.model.ShoppingCartProduct;

public interface ShoppingCartProductService {
    ShoppingCartProduct findById(Long id);

    ShoppingCartProduct create(Integer quantity, Product product, ShoppingCart shoppingCart, Long warehouse);

    ShoppingCartProduct updateQuantityOfProduct(Long id, Integer quantity);
}
