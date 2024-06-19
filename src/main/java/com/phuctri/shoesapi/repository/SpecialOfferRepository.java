package com.phuctri.shoesapi.repository;

import com.phuctri.shoesapi.entities.SpecialOffer;
import com.phuctri.shoesapi.entities.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecialOfferRepository extends JpaRepository<SpecialOffer, Long> {

    List<SpecialOffer> findByActive(Boolean active);

    @Query("SELECT sp " +
            "FROM SpecialOffer sp " +
            "WHERE sp.name like %:query% " +
            "or sp.description like %:query% " +
            "or sp.product.description like %:query%")
    Page<SpecialOffer> findAllByQuery(@Param("query") String query, Pageable pageable);

    Page<SpecialOffer> findAllByActiveTrue(Pageable pageable);

    List<SpecialOffer> findByProduct(Product product);

    List<SpecialOffer> findByActiveTrue();

    List<SpecialOffer> findByActiveTrueAndProduct(Product product);
}
