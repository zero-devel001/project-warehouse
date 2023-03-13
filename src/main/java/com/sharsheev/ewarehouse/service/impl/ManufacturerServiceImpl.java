package com.sharsheev.ewarehouse.service.impl;

import com.sharsheev.ewarehouse.model.Manufacturer;
import com.sharsheev.ewarehouse.model.Product;
import com.sharsheev.ewarehouse.model.exceptions.InvalidManufacturerIdException;
import com.sharsheev.ewarehouse.repository.ManufacturerRepository;
import com.sharsheev.ewarehouse.repository.ProductRepository;
import com.sharsheev.ewarehouse.service.ManufacturerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {
    private final ManufacturerRepository manufacturerRepository;
    private final ProductRepository productRepository;

    public ManufacturerServiceImpl(ManufacturerRepository manufacturerRepository, ProductRepository productRepository) {
        this.manufacturerRepository = manufacturerRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<Manufacturer> listAll() {
        return this.manufacturerRepository.findAll();
    }

    @Override
    public Manufacturer findbyId(Long id) {
        return this.manufacturerRepository.findById(id).orElseThrow(InvalidManufacturerIdException::new);
    }

    @Override
    public Manufacturer create(String name) {
        return this.manufacturerRepository.save(new Manufacturer(name));
    }

    @Override
    public Manufacturer update(Long id, String name) {
        Manufacturer manufacturer = this.findbyId(id);
        manufacturer.setName(name);
        return this.manufacturerRepository.save(manufacturer);
    }

    @Override
    public Manufacturer delete(Long id) {
        Manufacturer manufacturer = this.findbyId(id);
        for (Product product :
                manufacturer.getProducts()) {
            this.productRepository.delete(product);
        }
        this.manufacturerRepository.delete(manufacturer);
        return manufacturer;
    }
}
