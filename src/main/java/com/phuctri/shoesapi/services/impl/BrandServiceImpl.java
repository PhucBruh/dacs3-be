package com.phuctri.shoesapi.services.impl;

import com.phuctri.shoesapi.entities.SpecialOffer;
import com.phuctri.shoesapi.entities.product.Brand;
import com.phuctri.shoesapi.entities.product.Product;
import com.phuctri.shoesapi.exception.ResourceNotFoundException;
import com.phuctri.shoesapi.payload.request.BrandRequest;
import com.phuctri.shoesapi.payload.response.ApiResponse;
import com.phuctri.shoesapi.payload.response.DataResponse;
import com.phuctri.shoesapi.payload.response.PagedResponse;
import com.phuctri.shoesapi.payload.response.SpecialOfferResponse;
import com.phuctri.shoesapi.repository.BrandRepository;
import com.phuctri.shoesapi.repository.ProductRepository;
import com.phuctri.shoesapi.services.BrandService;
import com.phuctri.shoesapi.util.AppConstants;
import com.phuctri.shoesapi.util.AppUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    @Override
    public PagedResponse<Brand> getAllBrand(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Brand> brands = brandRepository.findAll(pageable);

        if (brands.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), brands.getNumber(), brands.getSize(), brands.getTotalElements(),
                    brands.getTotalPages(), brands.isLast());
        }

        List<Brand> specialOfferResponses = brands.stream().toList();

        return new PagedResponse<>(specialOfferResponses, brands.getNumber(), brands.getSize(), brands.getTotalElements(), brands.getTotalPages(), brands.isLast());
    }

    @Override
    public ResponseEntity<ApiResponse> getById(Long id) {

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "ID", id));
        ApiResponse response = new DataResponse(true, brand);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> addNewBrand(BrandRequest brandRequest) {
        Brand newBrand = new Brand(brandRequest.getName(), brandRequest.getImgUrl());
        newBrand = brandRepository.save(newBrand);
        DataResponse response = new DataResponse(true, newBrand);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiResponse> updateBrand(Long id, BrandRequest brandRequest) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "ID", id));

        brand.setName(brandRequest.getName());
        brand.setImg_url(brandRequest.getImgUrl());
        brandRepository.save(brand);

        ApiResponse response = new ApiResponse(true, AppConstants.UPDATE_SUCCESSFULLY);
        return new ResponseEntity<>(response, HttpStatus.OK);
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
