package com.phuctri.shoesapi.services.impl;

import com.phuctri.shoesapi.controller.ProductController;
import com.phuctri.shoesapi.entities.Inventory;
import com.phuctri.shoesapi.entities.order.OrderDetail;
import com.phuctri.shoesapi.entities.order.OrderInfo;
import com.phuctri.shoesapi.entities.order.OrderStatus;
import com.phuctri.shoesapi.entities.product.Product;
import com.phuctri.shoesapi.exception.ResourceNotFoundException;
import com.phuctri.shoesapi.exception.ShoesApiException;
import com.phuctri.shoesapi.payload.response.ApiResponse;
import com.phuctri.shoesapi.payload.response.DataResponse;
import com.phuctri.shoesapi.payload.response.OrderDetailResponse;
import com.phuctri.shoesapi.repository.InventoryRepository;
import com.phuctri.shoesapi.repository.OrderRepository;
import com.phuctri.shoesapi.repository.ProductRepository;
import com.phuctri.shoesapi.services.InventoryService;
import com.phuctri.shoesapi.services.OrderService;
import com.phuctri.shoesapi.util.AppConstants;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    @Override
    public ResponseEntity<ApiResponse> updateOrderStatus(Long id, OrderStatus orderStatus) {
        OrderInfo orderInfo = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "ID", id));

        if (orderStatus.equals(orderInfo.getStatus())) {
            ApiResponse response = new ApiResponse(false,
                    "current order status is " + orderStatus.toString());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        switch (orderInfo.getStatus()) {
            case COMPLETED, CANCELED, REFUNDED, FAILED -> {
                ApiResponse response = new ApiResponse(false,
                        "Cannot update the order that 'COMPLETED', 'CANCELED', 'REFUNDED' and 'FAILED', pls create new one");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }

        orderInfo.setStatus(orderStatus);

        switch (orderStatus) {
            case CANCELED, FAILED, REFUNDED -> {
                orderInfo.getOrderDetails().forEach(orderDetail -> {
                    Inventory inventory = inventoryRepository.findByProductAndColorAndSize(
                                    orderDetail.getProduct(),
                                    orderDetail.getColor(),
                                    orderDetail.getSize())
                            .orElseThrow(() -> new ShoesApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                                    "There no product '%s' with sizes '%s', color '%s' in inventory"
                                            .formatted(orderDetail.getProduct().getName(), orderDetail.getSize().getSize(), orderDetail.getColor().getName())));
                    inventory.setStock(inventory.getStock() + orderDetail.getQuantity());
                });
            }
            case COMPLETED -> {
                orderInfo.getOrderDetails().forEach(orderDetail -> {
                    orderDetail.getProduct().setTotalSold(
                            orderDetail.getProduct().getTotalSold() + orderDetail.getQuantity()
                    );
                    productRepository.save(orderDetail.getProduct());
                });
            }
            default -> {
            }
        }

        OrderInfo newOrderInfo = orderRepository.save(orderInfo);
        DataResponse response = new DataResponse(true, AppConstants.UPDATE_SUCCESSFULLY, OrderDetailResponse.toOrderResponse(newOrderInfo));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
