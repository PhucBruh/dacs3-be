package com.phuctri.shoesapi.controller;

import com.phuctri.shoesapi.entities.product.Brand;
import com.phuctri.shoesapi.payload.request.BrandRequest;
import com.phuctri.shoesapi.payload.response.ApiResponse;
import com.phuctri.shoesapi.services.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brand")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @GetMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> getAllBrand() {
        return brandService.getAllBrand();
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAllAlbums(@RequestBody BrandRequest brand) {
        return brandService.addNewBrand(brand);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateBrand(@RequestBody BrandRequest brand, @PathVariable Long id) {
        return brandService.addNewBrand(brand);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteBrand(@PathVariable Long id) {
        return brandService.deleteBrand(id);
    }
}