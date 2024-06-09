package com.phuctri.shoesapi.payload.response;

import com.phuctri.shoesapi.entities.SpecialOffer;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SpecialOfferResponse {
    private long id;
    private long productId;
    private String img;
    private String name;
    private String description;
    private double value;
    private boolean active;

    public static SpecialOfferResponse toSpecialOfferResponse(SpecialOffer specialOffer) {
        return SpecialOfferResponse.builder()
                .id(specialOffer.getId())
                .productId(specialOffer.getProduct().getId())
                .img(specialOffer.getProduct().getMainImg())
                .name(specialOffer.getName())
                .description(specialOffer.getDescription())
                .value(specialOffer.getValue())
                .active(specialOffer.getActive())
                .build();
    }
}