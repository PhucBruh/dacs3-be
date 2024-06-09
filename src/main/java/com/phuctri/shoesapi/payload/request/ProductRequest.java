package com.phuctri.shoesapi.payload.request;

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
    private List<Integer> size;
    private List<ColorPayload> colors;
    private List<String> imgs;
}

