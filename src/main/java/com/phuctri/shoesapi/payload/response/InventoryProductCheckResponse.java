package com.phuctri.shoesapi.payload.response;

import com.phuctri.shoesapi.entities.product.ProductStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InventoryProductCheckResponse {
    private int stock;
    private ProductStatus status;
}
