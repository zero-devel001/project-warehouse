package com.sharsheev.ewarehouse.repository;

import com.sharsheev.ewarehouse.model.Product;
import com.sharsheev.ewarehouse.model.ProductInWarehouse;
import com.sharsheev.ewarehouse.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductInWarehouseRepository extends JpaRepository<ProductInWarehouse, Long> {
    Optional<ProductInWarehouse> findByProductAndWarehouse(Product product, Warehouse warehouse);
}
