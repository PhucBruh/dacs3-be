package com.phuctri.shoesapi.controller;

import com.phuctri.shoesapi.payload.request.InventoryRequest;
import com.phuctri.shoesapi.payload.response.ApiResponse;
import com.phuctri.shoesapi.payload.response.InventoryResponse;
import com.phuctri.shoesapi.payload.response.OrderResponse;
import com.phuctri.shoesapi.payload.response.PagedResponse;
import com.phuctri.shoesapi.services.InventoryService;
import com.phuctri.shoesapi.util.AppConstants;
import com.phuctri.shoesapi.util.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PagedResponse<InventoryResponse> getInventory(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size
    ) {
        AppUtils.validatePageNumberAndSize(page, size);
        return inventoryService.getAllInventory(page, size);
    }

    @GetMapping("/q")
    @PreAuthorize("hasRole('ADMIN')")
    public PagedResponse<InventoryResponse> query(
            @RequestParam(name = "query", required = false, defaultValue = "") String query,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size
    ) {
        AppUtils.validatePageNumberAndSize(page, size);
        return inventoryService.query(query, page, size);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getInventoryById(@PathVariable Long id) {
        return inventoryService.getInventoryInfo(id);
    }

    @GetMapping("/check/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> check(
            @PathVariable Long id
    ) {
        return inventoryService.check(id);
    }

    @GetMapping("/s")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> searchInventory(

    ) {
        return null;
    }

    @GetMapping("/product")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> inventoryProductCheck(
            @RequestParam(name = "productId") Long productId,
            @RequestParam(name = "colorId") Long colorId,
            @RequestParam(name = "sizeId") Long sizeId
    ) {
        return inventoryService.checkProductByInventoryInfo(productId, colorId, sizeId);
    }

    @GetMapping("/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getInventoryOfProduct(@PathVariable Long id) {
        return null;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createInventory(@RequestBody InventoryRequest inventoryRequest) {
        return inventoryService.createInventory(inventoryRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateStock(
            @PathVariable Long id,
            @RequestParam(name = "stock", required = false, defaultValue = "0") Integer stock) {
        return inventoryService.updateStock(id, stock);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteInventory(@PathVariable Long id) {
        return inventoryService.deleteInventory(id);
    }
}
