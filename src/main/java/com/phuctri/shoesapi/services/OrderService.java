package com.phuctri.shoesapi.services;

import com.phuctri.shoesapi.entities.order.OrderInfo;
import com.phuctri.shoesapi.entities.order.OrderStatus;
import com.phuctri.shoesapi.payload.response.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {
    AnalysisResponse getAnalysis();

    List<WeeklyReport> getWeeklyReports(int year, int month);

    PagedResponse<OrderResponse> getAllOrder(int page, int size);

    PagedResponse<OrderResponse> query(String query, int page, int size);

    ResponseEntity<ApiResponse> getOrderInfo(Long id);

    ResponseEntity<ApiResponse> check(Long id);

    ResponseEntity<ApiResponse> updateOrderStatus(Long id, OrderStatus orderStatus);
}
