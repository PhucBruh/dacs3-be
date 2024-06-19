package com.phuctri.shoesapi.repository;

import com.phuctri.shoesapi.entities.product.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    @Query("SELECT b " +
            "FROM Brand b " +
            "WHERE b.name like %:query%")
    Page<Brand> findAllByQuery(@Param("query") String query, Pageable pageable);
}
