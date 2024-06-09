package com.phuctri.shoesapi.services;

import com.phuctri.shoesapi.payload.request.SpecialOfferRequest;
import com.phuctri.shoesapi.payload.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface SpecialOfferService {

    ResponseEntity<ApiResponse> addSpecialOffer(SpecialOfferRequest specialOfferRequest);

    ResponseEntity<ApiResponse> updateActive(Long id, Boolean active);

    ResponseEntity<ApiResponse> deleteAllSpecialOfferOfProduct(Long id);

    ResponseEntity<ApiResponse> deleteSpecialOffer(Long id);
}
