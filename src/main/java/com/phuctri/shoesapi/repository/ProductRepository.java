package com.phuctri.shoesapi.repository;

import com.phuctri.shoesapi.entities.product.Brand;
import com.phuctri.shoesapi.entities.product.Product;
import com.phuctri.shoesapi.entities.product.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByBrandId(Long brandId);

    List<Product> findByBrand(Brand brand);

    Page<Product> findByStatusNot(ProductStatus status, Pageable pageable);

    Page<Product> findByStatus(ProductStatus status, Pageable pageable);

    @Query("SELECT p " +
            "FROM Product p " +
            "WHERE " +
            "(:query is null or " +
            ":query = '' or " +
            "p.name like :query or " +
            "p.description like :query or " +
            "p.brand.name like :query) " +
            "AND " +
            "(:saleStatus = 'DEFAULT')" +
            "ORDER BY " +
            "CASE " +
            "WHEN :orderBy = 'PRICE' THEN p.price " +
            "WHEN :orderBy = 'RATING' THEN p.rating " +
            "WHEN :orderBy = 'SOLD' THEN p.totalSold " +
            "END " +
            "ASC"
    )
    List<Product> search(
            @Param("query") String query,
            @Param("saleStatus") String saleStatus,
            @Param("orderBy") String orderBy
    );
}
