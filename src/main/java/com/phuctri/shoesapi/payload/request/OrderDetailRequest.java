package com.phuctri.shoesapi.payload.request;

import lombok.Getter;

@Getter
public class OrderDetailRequest {
    private long productId;
    private long colorId;
    private long sizeId;
    private int quantity;
}
