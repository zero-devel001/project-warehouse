package com.sharsheev.ewarehouse.service.impl;

import com.sharsheev.ewarehouse.model.Product;
import com.sharsheev.ewarehouse.model.ShoppingCart;
import com.sharsheev.ewarehouse.model.ShoppingCartProduct;
import com.sharsheev.ewarehouse.repository.ShoppingCartProductRepository;
import com.sharsheev.ewarehouse.repository.WarehouseRepository;
import com.sharsheev.ewarehouse.service.ShoppingCartProductService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartProductServiceImpl implements ShoppingCartProductService {
    private final ShoppingCartProductRepository shoppingCartProductRepository;
    private final WarehouseRepository warehouseRepository;

    public ShoppingCartProductServiceImpl(ShoppingCartProductRepository shoppingCartProductRepository,
                                          WarehouseRepository warehouseRepository) {
        this.shoppingCartProductRepository = shoppingCartProductRepository;
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public ShoppingCartProduct findById(Long id) {
        return this.shoppingCartProductRepository.findById(id).get();
    }

    @Override
    public ShoppingCartProduct create(Integer quantity, Product product, ShoppingCart shoppingCart, Long warehouse) {
        return this.shoppingCartProductRepository.save(new ShoppingCartProduct(quantity, product, shoppingCart,
                this.warehouseRepository.findById(warehouse).get()));
    }

    @Override
    public ShoppingCartProduct updateQuantityOfProduct(Long id, Integer quantity) {
        ShoppingCartProduct product = this.shoppingCartProductRepository.findById(id).get();
        product.setQuantity(quantity);
        return this.shoppingCartProductRepository.save(product);
    }
}
