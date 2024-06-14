package com.phuctri.shoesapi.controller;

import com.phuctri.shoesapi.entities.SpecialOffer;
import com.phuctri.shoesapi.payload.request.SpecialOfferRequest;
import com.phuctri.shoesapi.payload.response.ApiResponse;
import com.phuctri.shoesapi.payload.response.PagedResponse;
import com.phuctri.shoesapi.payload.response.SpecialOfferResponse;
import com.phuctri.shoesapi.services.SpecialOfferService;
import com.phuctri.shoesapi.util.AppConstants;
import com.phuctri.shoesapi.util.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/special_offer")
@RequiredArgsConstructor
public class SpecialOfferController {

    private final SpecialOfferService specialOfferService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public PagedResponse<SpecialOfferResponse> getAllSpecialOffer(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size
    ) {
        AppUtils.validatePageNumberAndSize(page, size);
        return specialOfferService.getAllSpecialOffers(page, size);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public PagedResponse<SpecialOfferResponse> getAllSpecialOfferByAdmin(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size
    ) {
        AppUtils.validatePageNumberAndSize(page, size);
        return specialOfferService.getAllSpecialOffersByAdmin(page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> addSpecialOffer(@RequestBody SpecialOfferRequest specialOfferRequest) {
        return specialOfferService.addSpecialOffer(specialOfferRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateSpecialOffer(
            @PathVariable Long id,
            @RequestParam(name = "active") Boolean active) {
        return specialOfferService.updateActive(id, active);
    }

    @DeleteMapping("/product/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteAllSpecialOfferOfProduct(@PathVariable Long id) {
        return specialOfferService.deleteAllSpecialOfferOfProduct(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteSpecialOffer(@PathVariable Long id) {
        return specialOfferService.deleteSpecialOffer(id);
    }
}