package com.phuctri.shoesapi.services;

import com.phuctri.shoesapi.entities.product.Brand;
import com.phuctri.shoesapi.payload.request.BrandRequest;
import com.phuctri.shoesapi.payload.response.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BrandService {
    ResponseEntity<ApiResponse> addNewBrand(BrandRequest brandRequest);

    ResponseEntity<ApiResponse> getAllBrand();

    ResponseEntity<ApiResponse> deleteBrand(Long id);
}
