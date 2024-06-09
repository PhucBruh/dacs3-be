package com.phuctri.shoesapi.payload.response;

import lombok.Builder;

@Builder
public class OrderResponse {
    private long id;
    private String shippingAddress;
    private String description;
    private double price;
    private String status;
}
