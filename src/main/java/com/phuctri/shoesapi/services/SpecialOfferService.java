package com.phuctri.shoesapi.services;

import com.phuctri.shoesapi.payload.request.SpecialOfferRequest;
import com.phuctri.shoesapi.payload.response.ApiResponse;
import com.phuctri.shoesapi.payload.response.PagedResponse;
import com.phuctri.shoesapi.payload.response.SpecialOfferResponse;
import org.springframework.http.ResponseEntity;

public interface SpecialOfferService {

    PagedResponse<SpecialOfferResponse> getAllSpecialOffers(int page, int size);

    PagedResponse<SpecialOfferResponse> getAllSpecialOffersByAdmin(int page, int size);

    PagedResponse<SpecialOfferResponse> getAllSpecialOffersQuery(String query, int page, int size);

    ResponseEntity<ApiResponse> check(Long id);

    ResponseEntity<ApiResponse> getAllSpecialOffersById(Long id);

    ResponseEntity<ApiResponse> addSpecialOffer(SpecialOfferRequest specialOfferRequest);

    ResponseEntity<ApiResponse> updateActive(Long id, Boolean active);

    ResponseEntity<ApiResponse> deleteAllSpecialOfferOfProduct(Long id);

    ResponseEntity<ApiResponse> deleteSpecialOffer(Long id);
}
