package com.sharsheev.ewarehouse.service;

import com.sharsheev.ewarehouse.model.Manufacturer;

import java.util.List;

public interface ManufacturerService {
    List<Manufacturer> listAll();

    Manufacturer findbyId(Long id);

    Manufacturer create(String name);

    Manufacturer update(Long id, String name);

    Manufacturer delete(Long id);
}
