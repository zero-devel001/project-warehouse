package com.sharsheev.ewarehouse.service.impl;

import com.sharsheev.ewarehouse.model.ProductInWarehouse;
import com.sharsheev.ewarehouse.model.Warehouse;
import com.sharsheev.ewarehouse.repository.ProductInWarehouseRepository;
import com.sharsheev.ewarehouse.repository.WarehouseRepository;
import com.sharsheev.ewarehouse.service.WarehouseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final ProductInWarehouseRepository productInWarehouseRepository;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository,
                                ProductInWarehouseRepository productInWarehouseRepository) {
        this.warehouseRepository = warehouseRepository;
        this.productInWarehouseRepository = productInWarehouseRepository;
    }

    @Override
    public List<Warehouse> listAll() {
        return this.warehouseRepository.findAll();
    }

    @Override
    public Warehouse findById(Long id) {
        return this.warehouseRepository.findById(id).get();
    }

    @Override
    public Warehouse create(String name) {
        return this.warehouseRepository.save(new Warehouse(name));
    }

    @Override
    public Warehouse update(Long id, String name) {
        Warehouse warehouse = this.warehouseRepository.findById(id).get();
        warehouse.setName(name);
        return this.warehouseRepository.save(warehouse);
    }

    @Override
    public Warehouse delete(Long id) {
        Warehouse warehouse = this.warehouseRepository.findById(id).get();
        for (ProductInWarehouse product : warehouse.getProductInWarehouses()) {
            this.productInWarehouseRepository.delete(product);
        }
        this.warehouseRepository.deleteById(id);
        return warehouse;
    }
}
