package com.phuctri.shoesapi.payload.request;

import lombok.Getter;

@Getter
public class InventoryRequest {
    private long productId;
    private long colorId;
    private long sizeId;
}
