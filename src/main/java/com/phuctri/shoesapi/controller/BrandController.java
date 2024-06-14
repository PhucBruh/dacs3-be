package com.phuctri.shoesapi.controller;

import com.phuctri.shoesapi.entities.product.Brand;
import com.phuctri.shoesapi.payload.request.BrandRequest;
import com.phuctri.shoesapi.payload.response.ApiResponse;
import com.phuctri.shoesapi.payload.response.PagedResponse;
import com.phuctri.shoesapi.services.BrandService;
import com.phuctri.shoesapi.util.AppConstants;
import com.phuctri.shoesapi.util.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @GetMapping()
    @PreAuthorize("hasRole('USER')")
    public PagedResponse<Brand> getAllBrand(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size
    ) {
        AppUtils.validatePageNumberAndSize(page, size);
        return brandService.getAllBrand(page, size);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return brandService.getById(id);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAllAlbums(@RequestBody BrandRequest brand) {
        return brandService.addNewBrand(brand);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateBrand(@RequestBody BrandRequest brand, @PathVariable Long id) {
        return brandService.updateBrand(id, brand);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteBrand(@PathVariable Long id) {
        return brandService.deleteBrand(id);
    }
}