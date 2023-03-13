package com.sharsheev.ewarehouse.service.impl;

import com.sharsheev.ewarehouse.model.Product;
import com.sharsheev.ewarehouse.model.ProductInWarehouse;
import com.sharsheev.ewarehouse.model.Warehouse;
import com.sharsheev.ewarehouse.repository.ProductInWarehouseRepository;
import com.sharsheev.ewarehouse.service.ProductService;
import com.sharsheev.ewarehouse.service.ProductInWarehouseService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductInWarehouseServiceImpl implements ProductInWarehouseService {
    private final ProductInWarehouseRepository productInWarehouseRepository;
    private final ProductService productService;

    public ProductInWarehouseServiceImpl(ProductInWarehouseRepository productInWarehouseRepository,
                                         ProductService productService) {
        this.productInWarehouseRepository = productInWarehouseRepository;
        this.productService = productService;
    }

    public Optional<ProductInWarehouse> findById(Long id) {
        return this.productInWarehouseRepository.findById(id);
    }

    @Override
    public void addProducts(Integer quantity, Warehouse warehouse, Product product) {
        Optional<ProductInWarehouse> productInWarehouse = this.productInWarehouseRepository.
                findByProductAndWarehouse(product, warehouse);
        this.productService.setAvailability(product.getId(), true);
        if (productInWarehouse.isPresent()) {
            ProductInWarehouse piw = productInWarehouse.get();
            piw.setQuantity(piw.getQuantity() + quantity);
            this.productInWarehouseRepository.save(piw);
        } else
            this.productInWarehouseRepository.save
                    (new ProductInWarehouse(product, warehouse, quantity));
    }

    @Override
    public boolean lowerQuantity(Long id, Integer q) {
        Optional<ProductInWarehouse> productInWarehouse = this.productInWarehouseRepository.findById(id);
        if (!productInWarehouse.isPresent()) {
            return false;
        }
        ProductInWarehouse product = productInWarehouse.get();
        int quantity = product.getQuantity();
        if (quantity < q) return false;
        quantity -= q;
        if (quantity == 0) {
            this.productService.setAvailability(product.getProduct().getId(), false);
            this.productInWarehouseRepository.deleteById(product.getId());
            return false;
        } else {
            product.setQuantity(quantity);
            this.productInWarehouseRepository.save(product);
        }
        return true;
    }
}
