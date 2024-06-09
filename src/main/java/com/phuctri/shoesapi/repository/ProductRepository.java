package com.phuctri.shoesapi.repository;

import com.phuctri.shoesapi.entities.product.Brand;
import com.phuctri.shoesapi.entities.product.Product;
import com.phuctri.shoesapi.entities.product.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByBrandId(Long brandId);

    List<Product> findByBrand(Brand brand);

    Page<Product> findByStatusNot(ProductStatus status, Pageable pageable);
}
