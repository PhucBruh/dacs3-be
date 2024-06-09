package com.phuctri.shoesapi.repository;

import com.phuctri.shoesapi.entities.Inventory;
import com.phuctri.shoesapi.entities.product.Color;
import com.phuctri.shoesapi.entities.product.Product;
import com.phuctri.shoesapi.entities.product.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findAllByProductId(Long id);

    List<Inventory> findByProduct(Product product);

    List<Inventory> findByColor(Color color);

    List<Inventory> findBySize(Size size);

    Optional<Inventory> findByProductIdAndColorIdAndSizeId(Long productId, Long colorId, Long sizeId);

    Optional<Inventory> findByProductAndColorAndSize(Product product, Color color, Size size);
}
