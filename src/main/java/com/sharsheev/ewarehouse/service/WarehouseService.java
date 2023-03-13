package com.sharsheev.ewarehouse.service;

import com.sharsheev.ewarehouse.model.Warehouse;

import java.util.List;

public interface WarehouseService {
    List<Warehouse> listAll();

    Warehouse findById(Long id);

    Warehouse create(String name);

    Warehouse update(Long id, String name);

    Warehouse delete(Long id);
}
