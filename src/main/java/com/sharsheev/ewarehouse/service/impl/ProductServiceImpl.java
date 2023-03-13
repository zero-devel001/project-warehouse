package com.sharsheev.ewarehouse.service.impl;

import com.sharsheev.ewarehouse.model.Category;
import com.sharsheev.ewarehouse.model.Manufacturer;
import com.sharsheev.ewarehouse.model.Product;
import com.sharsheev.ewarehouse.model.exceptions.InvalidCategoryIdException;
import com.sharsheev.ewarehouse.model.exceptions.InvalidManufacturerIdException;
import com.sharsheev.ewarehouse.model.exceptions.InvalidProductIdException;
import com.sharsheev.ewarehouse.repository.CategoryRepository;
import com.sharsheev.ewarehouse.repository.ManufacturerRepository;
import com.sharsheev.ewarehouse.repository.ProductRepository;
import com.sharsheev.ewarehouse.service.ProductService;
import mk.ukim.finki.wp.ewarehouse.model.*;
import mk.ukim.finki.wp.ewarehouse.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ManufacturerRepository manufacturerRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              ManufacturerRepository manufacturerRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.manufacturerRepository = manufacturerRepository;
    }

    @Override
    public List<Product> listAll() {
        return this.productRepository.findAll();
    }

    @Override
    public Product findbyId(Long id) {
        return this.productRepository.findById(id).orElseThrow(InvalidProductIdException::new);
    }


    @Override
    public Product create(String name, String description, String imageUrl, Boolean availability, Double price,
                          Long categoryId, Long manufacturerId) {
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(InvalidCategoryIdException::new);
        Manufacturer manufacturer = this.manufacturerRepository.findById(manufacturerId).orElseThrow
                (InvalidManufacturerIdException::new);
        Product product = new Product(name, description, imageUrl, availability, price, category, manufacturer);
        this.productRepository.save(product);
        return product;
    }

    @Override
    public Product update(Long id, String name, String description, String imageUrl, Boolean availability,
                          Double price, Long categoryId, Long manufacturerId) {
        Product product = this.findbyId(id);
        product.setName(name);
        product.setDescription(description);
        product.setImageUrl(imageUrl);
        product.setAvailability(availability);
        product.setPrice(price);
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(InvalidCategoryIdException::new);
        product.setCategory(category);
        Manufacturer manufacturer = this.manufacturerRepository.findById(manufacturerId).orElseThrow(InvalidManufacturerIdException::new);
        product.setManufacturer(manufacturer);
        return this.productRepository.save(product);

    }

    @Override
    public Product delete(Long id) {
        Product product = this.findbyId(id);
        this.productRepository.delete(product);
        return product;
    }

    @Override
    public Product toggleAvailability(Long id) {
        Product product = this.productRepository.findById(id).orElseThrow(InvalidProductIdException::new);
        product.setAvailability(!product.getAvailability());
        this.productRepository.save(product);
        return product;
    }

    @Override
    public List<String> getProductNames() {
        return this.productRepository.getProductNames();
    }

    @Override
    public List<Product> searchProductsByName(String search) {
        return this.productRepository.findAllByNameContains(search);
    }

    @Override
    public List<Product> getProductsByCategoryManufacturerAndAvailability(Long categoryId, Long manufacturerId,
                                                                          Boolean availability) {
        if (categoryId != null && manufacturerId == null && availability != null && availability == false) {
            return this.productRepository.findAllByCategory(this.categoryRepository.findById(categoryId).orElseThrow
                    (InvalidCategoryIdException::new));
        } else if (categoryId == null && manufacturerId != null && availability != null && availability == false) {
            return this.productRepository.findAllByManufacturer(this.manufacturerRepository.findById(manufacturerId).
                    orElseThrow(InvalidManufacturerIdException::new));
        } else if (categoryId != null && manufacturerId != null && availability != null && availability == false) {
            return this.productRepository.findAllByCategoryAndManufacturer(this.categoryRepository.findById(categoryId).
                            orElseThrow(InvalidCategoryIdException::new),
                    this.manufacturerRepository.findById(manufacturerId).orElseThrow
                            (InvalidManufacturerIdException::new));
        } else if (categoryId != null && manufacturerId != null && availability != null && availability == true) {
            return this.productRepository.findAllByCategoryAndManufacturerAndAvailability(this.categoryRepository.
                            findById(categoryId).orElseThrow(InvalidCategoryIdException::new)
                    , this.manufacturerRepository.findById(manufacturerId).orElseThrow
                            (InvalidManufacturerIdException::new), true);
        } else if (categoryId == null && manufacturerId == null && availability != null && availability == true) {
            return this.productRepository.findAllByAvailability(true);
        } else if (categoryId != null && manufacturerId == null && availability != null && availability == true) {
            return this.productRepository.findAllByCategoryAndAvailability(this.categoryRepository.
                    findById(categoryId).orElseThrow(InvalidCategoryIdException::new), true);
        } else if (categoryId == null && manufacturerId != null && availability != null && availability == true) {
            return this.productRepository.findAllByManufacturerAndAvailability(this.manufacturerRepository.
                    findById(manufacturerId).orElseThrow(InvalidManufacturerIdException::new), true);
        }
        return this.productRepository.findAll();
    }

    @Override
    public Product setAvailability(Long id, Boolean availability) {
        Product product = this.productRepository.findById(id).orElseThrow(InvalidProductIdException::new);
        product.setAvailability(availability);
        return this.productRepository.save(product);
    }

    @Override
    public List<Product> findFirstThree() {
        return this.productRepository.findFirst3ByOrderById();
    }
}