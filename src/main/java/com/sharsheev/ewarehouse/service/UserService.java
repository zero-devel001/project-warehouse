package com.sharsheev.ewarehouse.service;

import com.sharsheev.ewarehouse.model.Order;
import com.sharsheev.ewarehouse.model.User;

import java.util.List;

public interface UserService {
    User register(String username, String password, String repeatPassword, String role);

    User login(String username, String password);

    User updateUser(String username, String password, String firstName,
                    String lastName,
                    String address,
                    String email);

    User adminUserUpdate(String username, String firstName,
                         String lastName,
                         String address,
                         String email);

    User getUser(String username);

    List<User> getAllUsers();

    User findUserById(String username);

    void deleteUser(String username);

    List<Order> listAllUserOrders(String username);
}
