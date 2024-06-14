package com.phuctri.shoesapi.repository;

import com.phuctri.shoesapi.entities.order.OrderInfo;
import com.phuctri.shoesapi.entities.order.OrderStatus;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderInfo, Long> {
    List<OrderInfo> findByUserId(Long userId);

    List<OrderInfo> findAllByUserIdAndStatusNot(Long userId, OrderStatus status);

    List<OrderInfo> findAllByUserIdAndStatus(Long userId, OrderStatus status);
}
