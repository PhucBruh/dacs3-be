package com.phuctri.shoesapi.controller;

import com.phuctri.shoesapi.entities.order.OrderStatus;
import com.phuctri.shoesapi.entities.product.Brand;
import com.phuctri.shoesapi.payload.response.ApiResponse;
import com.phuctri.shoesapi.payload.response.OrderResponse;
import com.phuctri.shoesapi.payload.response.PagedResponse;
import com.phuctri.shoesapi.repository.OrderRepository;
import com.phuctri.shoesapi.services.OrderService;
import com.phuctri.shoesapi.util.AppConstants;
import com.phuctri.shoesapi.util.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PagedResponse<OrderResponse> getOrder(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

        AppUtils.validatePageNumberAndSize(page, size);
        return orderService.getAllOrder(page, size);
    }

    @GetMapping("/q")
    @PreAuthorize("hasRole('ADMIN')")
    public PagedResponse<OrderResponse> query(
            @RequestParam(name = "query", required = false, defaultValue = "") String query,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size
    ) {
        AppUtils.validatePageNumberAndSize(page, size);
        return orderService.query(query, page, size);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long id) {
        return orderService.getOrderInfo(id);
    }

    @GetMapping("/check/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> check(
            @PathVariable Long id
    ) {
        return orderService.check(id);
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
