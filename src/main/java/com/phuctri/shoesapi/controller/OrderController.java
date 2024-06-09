package com.phuctri.shoesapi.controller;

import com.phuctri.shoesapi.entities.order.OrderStatus;
import com.phuctri.shoesapi.payload.response.ApiResponse;
import com.phuctri.shoesapi.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getOrder() {
        return null;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long id) {
        return null;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam(name = "status") OrderStatus status
    ) {
        return orderService.updateOrderStatus(id, status);
    }
}
