package com.phuctri.shoesapi.services;

import com.phuctri.shoesapi.entities.order.OrderInfo;
import com.phuctri.shoesapi.entities.order.OrderStatus;
import com.phuctri.shoesapi.payload.response.ApiResponse;
import com.phuctri.shoesapi.payload.response.OrderResponse;
import com.phuctri.shoesapi.payload.response.PagedResponse;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    PagedResponse<OrderResponse> getAllOrder(int page, int size);
    ResponseEntity<ApiResponse> getOrderInfo(Long id);
    ResponseEntity<ApiResponse> updateOrderStatus(Long id, OrderStatus orderStatus);
}
