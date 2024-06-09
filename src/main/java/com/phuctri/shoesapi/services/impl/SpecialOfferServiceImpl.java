package com.phuctri.shoesapi.services.impl;

import com.phuctri.shoesapi.entities.SpecialOffer;
import com.phuctri.shoesapi.entities.product.Product;
import com.phuctri.shoesapi.exception.ResourceNotFoundException;
import com.phuctri.shoesapi.payload.request.SpecialOfferRequest;
import com.phuctri.shoesapi.payload.response.ApiResponse;
import com.phuctri.shoesapi.repository.ProductRepository;
import com.phuctri.shoesapi.repository.SpecialOfferRepository;
import com.phuctri.shoesapi.services.SpecialOfferService;
import com.phuctri.shoesapi.util.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialOfferServiceImpl implements SpecialOfferService {

    private final ProductRepository productRepository;
    private final SpecialOfferRepository specialOfferRepository;

    @Override
    public ResponseEntity<ApiResponse> addSpecialOffer(SpecialOfferRequest specialOfferRequest) {

        Product product = productRepository.findById(specialOfferRequest.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", specialOfferRequest.getProductId()));

        if (specialOfferRequest.getValue() > 100 || specialOfferRequest.getValue() < 0) {
            ApiResponse response = new ApiResponse(false, "value should be in 0-100");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        SpecialOffer specialOffer = SpecialOffer.builder()
                .product(product)
                .name(specialOfferRequest.getName())
                .description(specialOfferRequest.getDescription())
                .value(specialOfferRequest.getValue())
                .active(true)
                .build();
        specialOfferRepository.save(specialOffer);

        product.setPromotionalPrice(product.getPrice() * (specialOfferRequest.getValue() / 100));

        ApiResponse response = new ApiResponse(true, AppConstants.CREATE_SUCCESSFULLY);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiResponse> updateActive(Long id, Boolean active) {

        // check if any other special offer of product is active
        if (active) {
            List<SpecialOffer> specialOffers = specialOfferRepository.findByActiveTrue();
            if (!specialOffers.isEmpty()) {
                ApiResponse response = new ApiResponse(
                        false,
                        "Some other special offer of product with id %s is active".formatted(id));
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }

        SpecialOffer specialOffer = specialOfferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SpecialOffer", "ID", id));

        if (specialOffer.getActive().equals(active)) {
            ApiResponse response = new ApiResponse(
                    false,
                    "Special offer is current %s".formatted(active));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        specialOffer.setActive(active);
        specialOfferRepository.save(specialOffer);

        // Reset promotion price if active is false
        if (!active) {
            specialOffer.getProduct().setPromotionalPrice(0.0);
            productRepository.save(specialOffer.getProduct());
        }

        ApiResponse response = new ApiResponse(false, AppConstants.UPDATE_SUCCESSFULLY);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteAllSpecialOfferOfProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", id));

        List<SpecialOffer> specialOffers = specialOfferRepository.findByProduct(product);

        if (!specialOffers.isEmpty()) {
            ApiResponse response = new ApiResponse(false, "This product has no special offers");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        specialOfferRepository.deleteAll(specialOffers);
        product.setPromotionalPrice(0.0);
        productRepository.save(product);

        ApiResponse response = new ApiResponse(true, AppConstants.DELETE_SUCCESSFULLY);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteSpecialOffer(Long id) {
        SpecialOffer specialOffer = specialOfferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SpecialOffer", "ID", id));
        if (specialOffer.getActive()) {
            specialOffer.getProduct().setPromotionalPrice(0.0);
            productRepository.save(specialOffer.getProduct());
        }

        specialOfferRepository.delete(specialOffer);

        ApiResponse response = new ApiResponse(true, AppConstants.DELETE_SUCCESSFULLY);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
