package com.sharsheev.ewarehouse.service;

import com.sharsheev.ewarehouse.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    List<Category> listAll();

    Category findbyId(Long id);

    Category create(String name);

    Category update(Long id, String name);

    Category delete(Long id);
}
