package com.sharsheev.ewarehouse.service;

import com.sharsheev.ewarehouse.model.Order;
import com.sharsheev.ewarehouse.model.OrderStatus;

import java.util.List;

public interface OrderService {
    Order placeOrder(String firstName, String lastName, String email, String address, String country,
                     String city, String zipCode, Double totalPrice, List<Long> products, String username);

    void updateOrder(Long id, String firstName, String lastName, String address, String email, String city,
                     String country, String zipcode, OrderStatus status);

    Order delete(Long id);

    Order findById(Long id);

    List<Order> listAll();

    Order cancelOrder(Long id);
}
