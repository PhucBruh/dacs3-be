package com.phuctri.shoesapi.repository;

import com.phuctri.shoesapi.entities.Inventory;
import com.phuctri.shoesapi.entities.product.Color;
import com.phuctri.shoesapi.entities.product.Product;
import com.phuctri.shoesapi.entities.product.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findAllByProductId(Long id);

    @Query("SELECT i " +
            "FROM Inventory i " +
            "WHERE i.product.name like %:query% " +
            "or i.product.description like %:query% " +
            "or i.color.name like %:query%")
    Page<Inventory> findAllByQuery(@Param("query") String query, Pageable pageable);

    List<Inventory> findAllByProductIdAndStockGreaterThan(Long id, Integer stock);

    List<Inventory> findByProduct(Product product);

    List<Inventory> findByColor(Color color);

    Optional<Inventory> getByColor(Color color);

    List<Inventory> findBySize(Size size);

    Optional<Inventory> findByProductIdAndColorIdAndSizeId(Long productId, Long colorId, Long sizeId);

    Optional<Inventory> findByProductAndColorAndSize(Product product, Color color, Size size);
}
