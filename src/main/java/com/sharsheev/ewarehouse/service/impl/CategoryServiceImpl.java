package com.sharsheev.ewarehouse.service.impl;

import com.sharsheev.ewarehouse.model.Category;
import com.sharsheev.ewarehouse.model.Product;
import com.sharsheev.ewarehouse.model.exceptions.InvalidCategoryIdException;
import com.sharsheev.ewarehouse.repository.CategoryRepository;
import com.sharsheev.ewarehouse.repository.ProductRepository;
import com.sharsheev.ewarehouse.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<Category> listAll() {
        return this.categoryRepository.findAll();
    }

    @Override
    public Category findbyId(Long id) {
        return this.categoryRepository.findById(id).orElseThrow(InvalidCategoryIdException::new);
    }

    @Override
    public Category create(String name) {
        return this.categoryRepository.save(new Category(name));
    }

    @Override
    public Category update(Long id, String name) {
        Category category = this.findbyId(id);
        category.setName(name);
        return this.categoryRepository.save(category);
    }

    @Override
    public Category delete(Long id) {
        Category category = this.findbyId(id);
        for (Product product : category.getProducts()) {
            this.productRepository.delete(product);
        }
        this.categoryRepository.delete(category);
        return category;
    }
}
