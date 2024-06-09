package com.phuctri.shoesapi.payload.response;

import com.phuctri.shoesapi.entities.product.Product;
import com.phuctri.shoesapi.entities.product.ProductStatus;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private double price;
    private double promotionPrice;
    private String mainImg;
    private double rating;
    private long totalSold;
    private ProductStatus status;

    public static ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .promotionPrice(product.getPromotionalPrice())
                .mainImg(product.getMainImg())
                .rating(product.getRating())
                .totalSold(product.getTotalSold())
                .status(product.getStatus())
                .build();
    }
}