package com.phuctri.shoesapi.payload.request;

import com.phuctri.shoesapi.entities.product.ProductStatus;
import com.phuctri.shoesapi.payload.ColorPayload;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductRequest {
    private String name;
    private String description;
    private double price;
    private long brandId;
    private String mainImg;
    private List<Integer> sizes;
    private List<ColorPayload> colors;
    private List<String> imgs;
    private ProductStatus status;
}

