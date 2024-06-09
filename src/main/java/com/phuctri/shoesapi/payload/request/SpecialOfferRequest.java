package com.phuctri.shoesapi.payload.request;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
public class SpecialOfferRequest {
    private long productId;
    private String name;
    private String description;
    private double value;
}
