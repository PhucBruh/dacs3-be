package com.phuctri.shoesapi.services;

import com.phuctri.shoesapi.entities.order.OrderStatus;
import com.phuctri.shoesapi.payload.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    ResponseEntity<ApiResponse> updateOrderStatus(Long id, OrderStatus orderStatus);
}
