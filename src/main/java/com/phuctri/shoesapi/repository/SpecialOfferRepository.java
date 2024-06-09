package com.phuctri.shoesapi.repository;

import com.phuctri.shoesapi.entities.SpecialOffer;
import com.phuctri.shoesapi.entities.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecialOfferRepository extends JpaRepository<SpecialOffer, Long> {

    List<SpecialOffer> findByActive(Boolean active);

    List<SpecialOffer> findByProduct(Product product);

    List<SpecialOffer> findByActiveTrue();
}
