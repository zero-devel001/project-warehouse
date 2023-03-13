package com.sharsheev.ewarehouse.model;

import javax.persistence.*;

@Entity
public class ShoppingCartProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Integer quantity;

    @ManyToOne
    Product product;

    @ManyToOne
    ShoppingCart shoppingCart;

    @ManyToOne
    Warehouse warehouse;

    public ShoppingCartProduct() {}

    public ShoppingCartProduct(Integer quantity, Product product, ShoppingCart shoppingCart, Warehouse warehouse) {
        this.quantity = quantity;
        this.product = product;
        this.shoppingCart = shoppingCart;
        this.warehouse = warehouse;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }
}
