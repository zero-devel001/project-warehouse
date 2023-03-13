package com.sharsheev.ewarehouse.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @OneToMany(mappedBy = "warehouse")
    List<ProductInWarehouse> productInWarehouses;

    public Warehouse() {}

    public Warehouse(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductInWarehouse> getProductInWarehouses() {
        return productInWarehouses;
    }

    public void setProductInWarehouses(List<ProductInWarehouse> productInWarehouses) {
        this.productInWarehouses = productInWarehouses;
    }

    public Integer stockOfProduct(Long productId) {
        Integer sum = 0;
        for (ProductInWarehouse productInWarehouse : productInWarehouses) {
            if (productInWarehouse.getProduct().getId() == productId)
                sum += productInWarehouse.getQuantity();
        }
        return sum;
    }
}
