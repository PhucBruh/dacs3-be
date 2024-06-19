package com.phuctri.shoesapi.services;

import com.phuctri.shoesapi.entities.product.Brand;
import com.phuctri.shoesapi.entities.product.Size;
import com.phuctri.shoesapi.payload.request.BrandRequest;
import com.phuctri.shoesapi.payload.response.ApiResponse;
import com.phuctri.shoesapi.payload.response.PagedResponse;
import org.hibernate.query.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BrandService {
    PagedResponse<Brand> getAllBrand(int page, int size);

    PagedResponse<Brand> query(String query, int page, int size);

    ResponseEntity<ApiResponse> getById(Long id);

    ResponseEntity<ApiResponse> check(Long id);

    ResponseEntity<ApiResponse> addNewBrand(BrandRequest brandRequest);

    ResponseEntity<ApiResponse> updateBrand(Long id, BrandRequest brandRequest);

    ResponseEntity<ApiResponse> getAllBrand();

    ResponseEntity<ApiResponse> deleteBrand(Long id);
}
