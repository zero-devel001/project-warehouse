package com.sharsheev.ewarehouse.service;

import com.sharsheev.ewarehouse.model.Product;
import com.sharsheev.ewarehouse.model.ProductInWarehouse;
import com.sharsheev.ewarehouse.model.Warehouse;

import java.util.Optional;

public interface ProductInWarehouseService {
    Optional<ProductInWarehouse> findById(Long id);

    boolean lowerQuantity(Long id, Integer q);

    void addProducts(Integer quantity, Warehouse warehouse, Product product);
}
