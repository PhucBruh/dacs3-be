package com.phuctri.shoesapi.payload.response;

import com.phuctri.shoesapi.entities.product.Product;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductPriceResponse {
    private double price;
    private double promotionPrice;

    public static ProductPriceResponse toProductPriceResponse(Product product) {
        return ProductPriceResponse.builder()
                .price(product.getPrice())
                .promotionPrice(product.getPromotionalPrice())
                .build();
    }
}
