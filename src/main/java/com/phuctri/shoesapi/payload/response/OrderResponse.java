package com.phuctri.shoesapi.payload.response;

import com.phuctri.shoesapi.entities.order.OrderInfo;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.query.Order;

@Getter
@Builder
public class OrderResponse {
    private long id;
    private String shippingAddress;
    private String description;
    private double price;
    private String status;

    public static OrderResponse toOrderResponse(OrderInfo orderInfo) {
        return OrderResponse.builder()
                .id(orderInfo.getId())
                .shippingAddress(orderInfo.getShippingAddress())
                .description(orderInfo.getDescription())
                .price(orderInfo.getPrice())
                .status(orderInfo.getStatus().toString())
                .build();
    }
}
