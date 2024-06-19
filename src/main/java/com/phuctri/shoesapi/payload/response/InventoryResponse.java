package com.phuctri.shoesapi.payload.response;

import com.phuctri.shoesapi.entities.Inventory;
import com.phuctri.shoesapi.entities.product.Product;
import com.phuctri.shoesapi.services.InventoryService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InventoryResponse {
    private long id;
    private long productId;
    private String productName;
    private String productImg;
    private Size size;
    private Color color;
    private int stock;

    @Getter
    @AllArgsConstructor
    static class Size {
        private long id;
        private int size;
    }

    @Getter
    @AllArgsConstructor
    static class Color {
        private long id;
        private String name;
        private String value;
    }

    public static InventoryResponse toInventoryResponse(Inventory inventory) {
        return InventoryResponse.builder()
                .id(inventory.getId())
                .productId(inventory.getProduct().getId())
                .productName(inventory.getProduct().getName())
                .productImg(inventory.getProduct().getMainImg())
                .size(new
                        Size(inventory.getSize().getId(),
                        inventory.getSize().getSize()))
                .color(new Color(inventory.getColor().getId(),
                        inventory.getColor().getName(),
                        inventory.getColor().getValue()))
                .stock(inventory.getStock())
                .build();
    }
}