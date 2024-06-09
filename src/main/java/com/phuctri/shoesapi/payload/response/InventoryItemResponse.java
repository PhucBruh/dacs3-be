package com.phuctri.shoesapi.payload.response;

import com.phuctri.shoesapi.entities.Inventory;
import com.phuctri.shoesapi.payload.ColorPayload;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InventoryItemResponse {
    private long productId;
    private String productName;
    private String productImg;
    private int size;
    private ColorPayload color;
    private int stock;

    public static InventoryItemResponse toInventoryItemResponse(Inventory inventory) {
        return InventoryItemResponse.builder()
                .productId(inventory.getProduct().getId())
                .productName(inventory.getColor().getName())
                .productImg(inventory.getProduct().getMainImg())
                .size(inventory.getSize().getSize())
                .color(ColorPayload.builder()
                        .name(inventory.getColor().getName())
                        .value(inventory.getColor().getValue())
                        .build())
                .stock(inventory.getStock())
                .build();
    }
}