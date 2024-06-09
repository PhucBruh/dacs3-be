package com.phuctri.shoesapi.repository;

import com.phuctri.shoesapi.entities.order.OrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderInfo, Long> {
    List<OrderInfo> findByUserId(Long userId);
}
