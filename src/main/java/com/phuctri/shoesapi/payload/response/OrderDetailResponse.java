package com.phuctri.shoesapi.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.phuctri.shoesapi.entities.order.OrderInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Builder
@Getter
public class OrderDetailResponse implements Serializable {
    private long id;
    private User user;
    private String shippingAddress;
    private String description;
    private List<Detail> details;
    private double price;
    private String status;

    public static OrderDetailResponse toOrderResponse(OrderInfo order) {
        return OrderDetailResponse.builder()
                .id(order.getId())
                .user(User.builder()
                        .name(order.getUser().getFirstname() + " " + order.getUser().getLastname())
                        .phone(order.getUser().getPhone())
                        .build())
                .shippingAddress(order.getShippingAddress())
                .description(order.getDescription())
                .details(
                        order.getOrderDetails().stream()
                                .map(orderDetail -> Detail.builder()
                                        .productId(orderDetail.getProduct().getId())
                                        .productName(orderDetail.getProduct().getName())
                                        .productImg(orderDetail.getProduct().getMainImg())
                                        .size(orderDetail.getSize().getSize())
                                        .color(orderDetail.getColor().getName())
                                        .colorValue(orderDetail.getColor().getValue())
                                        .quantity(orderDetail.getQuantity())
                                        .price(orderDetail.getPrice())
                                        .build())
                                .toList()
                )
                .price(order.getPrice())
                .status(order.getStatus().toString())
                .build();
    }

    @Builder
    @Getter
    static class Detail implements Serializable {
        private long productId;
        private String productName;
        private String productImg;
        private int size;
        private String color;
        private String colorValue;
        private int quantity;
        private double price;
    }

    @Builder
    @Getter
    static class User implements Serializable {
        private String name;
        private String phone;
    }
}
