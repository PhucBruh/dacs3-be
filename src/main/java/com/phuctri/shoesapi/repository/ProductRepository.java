package com.phuctri.shoesapi.repository;

import com.phuctri.shoesapi.entities.product.Brand;
import com.phuctri.shoesapi.entities.product.Product;
import com.phuctri.shoesapi.entities.product.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.WeakHashMap;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByBrandId(Long brandId);

    List<Product> findByBrand(Brand brand);

    @Query("SELECT p " +
            "FROM Product p " +
            "WHERE p.brand.id = :id and p.status = 'ACTIVE'"
    )
    Page<Product> findAllByBrandId(@Param("id") Long brandId, Pageable pageable);

    Page<Product> findByStatusNot(ProductStatus status, Pageable pageable);

    Page<Product> findByStatus(ProductStatus status, Pageable pageable);

    @Query("SELECT p " +
            "FROM Product p " +
            "WHERE " +
            "(:query is null or " +
            ":query = '' or " +
            "p.name like %:query% or " +
            "p.description like %:query% or " +
            "p.brand.name like %:query%) " +
            "AND " +
            "p.price > :minPrice AND p.price<:maxPrice " +
            "AND " +
            "(:saleStatus = 'NORMAL' or " +
            "(:saleStatus = 'ACTIVE' and p.promotionalPrice>0.0) or " +
            "(:saleStatus = 'INACTIVE' and p.promotionalPrice=0.0)) " +
            "AND " +
            "p.status = 'ACTIVE'"
    )
    Page<Product> search(
            @Param("query") String query,
            @Param("saleStatus") String saleStatus,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable
    );

    @Query("SELECT p " +
            "FROM Product  p " +
            "WHERE p.name like %:query% or p.description like %:query%"
    )
    Page<Product> searchByQuery(@Param("query") String query, Pageable pageable);

    @Query("SELECT p " +
            "FROM Product  p " +
            "WHERE p.name like %:query% or p.description like %:query% and p.status = 'ACTIVE'"
    )
    Page<Product> searchByQueryActive(@Param("query") String query, Pageable pageable);
}
