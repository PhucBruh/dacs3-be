package com.phuctri.shoesapi.services.impl;

import com.phuctri.shoesapi.entities.product.Brand;
import com.phuctri.shoesapi.entities.product.Product;
import com.phuctri.shoesapi.exception.ResourceNotFoundException;
import com.phuctri.shoesapi.payload.request.BrandRequest;
import com.phuctri.shoesapi.payload.response.ApiResponse;
import com.phuctri.shoesapi.payload.response.DataResponse;
import com.phuctri.shoesapi.repository.BrandRepository;
import com.phuctri.shoesapi.repository.ProductRepository;
import com.phuctri.shoesapi.services.BrandService;
import com.phuctri.shoesapi.util.AppConstants;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    @Override
    public ResponseEntity<ApiResponse> addNewBrand(BrandRequest brandRequest) {
        Brand newBrand = new Brand(brandRequest.getName(), brandRequest.getImgUrl());
        newBrand = brandRepository.save(newBrand);
        DataResponse response = new DataResponse(true, newBrand);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiResponse> getAllBrand() {
        DataResponse response = new DataResponse(true, brandRepository.findAll());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteBrand(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "ID", id));

        List<Product> products = productRepository.findByBrand(brand);
        if (!brand.getProducts().isEmpty()) {
            ApiResponse response = new ApiResponse(false, "This brand already use by some products");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        brandRepository.delete(brand);
        ApiResponse response = new ApiResponse(true, AppConstants.DELETE_SUCCESSFULLY);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
