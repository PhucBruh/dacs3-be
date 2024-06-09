package com.phuctri.shoesapi.repository;

import com.phuctri.shoesapi.entities.product.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {
}

