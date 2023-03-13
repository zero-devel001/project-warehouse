package com.sharsheev.ewarehouse.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    String description;

    String imageUrl;

    Boolean availability;

    Double price;

    @ManyToOne
    Category category;

    @ManyToOne
    Manufacturer manufacturer;

    @ManyToMany(mappedBy = "products")
    List<ShoppingCart> shoppingCarts;

    @OneToMany(mappedBy = "product")
    List<ShoppingCartProduct> shoppingCartProducts;

    @OneToMany(mappedBy = "product")
    List<ProductInWarehouse> productsInWarehouse;

    public Product() {}

    public Product(String name, String description, String imageUrl, Boolean availability,
                   Double price, Category category, Manufacturer manufacturer) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.availability = availability;
        this.price = price;
        this.category = category;
        this.manufacturer = manufacturer;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public List<ShoppingCart> getShoppingCarts() {
        return shoppingCarts;
    }

    public void setShoppingCarts(List<ShoppingCart> shoppingCarts) {
        this.shoppingCarts = shoppingCarts;
    }

    public List<ShoppingCartProduct> getShoppingCartProducts() {
        return shoppingCartProducts;
    }

    public void setShoppingCartProducts(List<ShoppingCartProduct> shoppingCartProducts) {
        this.shoppingCartProducts = shoppingCartProducts;
    }

    public List<ProductInWarehouse> getProductsInWarehouse() {
        return productsInWarehouse;
    }

    public void setProductsInWarehouse(List<ProductInWarehouse> productsInWarehouse) {
        this.productsInWarehouse = productsInWarehouse;
    }

    public List<Warehouse> getWarehouses(){
        List<Warehouse> warehouseList = new ArrayList<>();
        for (ProductInWarehouse product:productsInWarehouse
        ) {
            if(!warehouseList.contains(product.getWarehouse()))
                warehouseList.add(product.getWarehouse());
        }
        return warehouseList;
    }

    public boolean checkAvailability(){
        for (ProductInWarehouse product: productsInWarehouse){
            if(product.quantity > 0)
                return true;
        }
        return false;
    }

    public int getTotalQuantity(){
        int sum = 0;
        for (ProductInWarehouse product: productsInWarehouse){
            sum+= product.getQuantity();
        }
        return sum;
    }
}