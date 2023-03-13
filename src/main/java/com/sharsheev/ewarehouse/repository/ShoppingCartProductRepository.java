package com.sharsheev.ewarehouse.repository;

import com.sharsheev.ewarehouse.model.Product;
import com.sharsheev.ewarehouse.model.ShoppingCart;
import com.sharsheev.ewarehouse.model.ShoppingCartProduct;
import com.sharsheev.ewarehouse.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartProductRepository extends JpaRepository<ShoppingCartProduct, Long> {
    Optional<ShoppingCartProduct> findByProductAndShoppingCartAndWarehouse(Product product,
                                                                           ShoppingCart shoppingCart,
                                                                           Warehouse warehouse);
}
