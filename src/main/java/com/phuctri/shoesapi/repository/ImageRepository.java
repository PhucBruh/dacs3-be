package com.phuctri.shoesapi.repository;

import com.phuctri.shoesapi.entities.product.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
