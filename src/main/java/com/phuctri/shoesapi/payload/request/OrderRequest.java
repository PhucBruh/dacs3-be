package com.phuctri.shoesapi.payload.request;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequest {
    private String phone;
    private String shippingAddress;
    private String description;
    private List<OrderDetailRequest> detail;
}
