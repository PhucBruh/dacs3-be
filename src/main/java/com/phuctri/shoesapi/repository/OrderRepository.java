package com.phuctri.shoesapi.repository;

import com.phuctri.shoesapi.entities.order.OrderInfo;
import com.phuctri.shoesapi.entities.order.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderInfo, Long> {
    List<OrderInfo> findByUserId(Long userId);

    @Query("SELECT o " +
            "FROM OrderInfo o " +
            "WHERE o.user.firstname like %:query% " +
            "or o.user.lastname like %:query% " +
            "or o.shippingAddress like %:query% " +
            "or o.description like %:query%")
    Page<OrderInfo> findAllByQuery(@Param("query") String query, Pageable pageable);

    List<OrderInfo> findAllByUserIdAndStatusNot(Long userId, OrderStatus status);

    List<OrderInfo> findAllByUserIdAndStatus(Long userId, OrderStatus status);

    Integer countByStatus(OrderStatus status);

    Integer countByStatusNotIn(List<OrderStatus> statuses);

    @Query("SELECT SUM(o.price) FROM OrderInfo o WHERE FUNCTION('YEAR', o.createdAt) = :year")
    Double findYearlyEarnings(@Param("year") int year);

    @Query("SELECT SUM(o.price) FROM OrderInfo o WHERE FUNCTION('YEAR', o.createdAt) = :year AND FUNCTION('MONTH', o.createdAt) = :month")
    Double findMonthlyEarnings(@Param("year") int year, @Param("month") int month);

    @Query("SELECT COUNT(o) FROM OrderInfo o WHERE o.status = :status AND o.createdAt BETWEEN :startDate AND :endDate")
    long countCompletedOrders(@Param("status") OrderStatus status, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT SUM(o.price) FROM OrderInfo o WHERE o.status = :status AND o.createdAt BETWEEN :startDate AND :endDate")
    Double calculateEarnings(@Param("status") OrderStatus status, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}