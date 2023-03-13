package com.sharsheev.ewarehouse.repository;

import com.sharsheev.ewarehouse.model.Category;
import com.sharsheev.ewarehouse.model.Manufacturer;
import com.sharsheev.ewarehouse.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select distinct name from Product ")
    List<String> getProductNames();

    List<Product> findAllByNameContains(String name);

    List<Product> findAllByCategory(Category category);

    List<Product> findAllByManufacturer(Manufacturer manufacturer);

    List<Product> findAllByCategoryAndManufacturer(Category category, Manufacturer manufacturer);

    List<Product> findAllByCategoryAndManufacturerAndAvailability(Category category, Manufacturer manufacturer, Boolean availability);

    List<Product> findAllByAvailability(Boolean availability);

    List<Product> findAllByCategoryAndAvailability(Category category, Boolean availability);

    List<Product> findAllByManufacturerAndAvailability(Manufacturer manufacturer, Boolean availability);

    List<Product> findFirst3ByOrderById();
}
