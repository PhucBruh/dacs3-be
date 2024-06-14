package com.phuctri.shoesapi.services;


import com.phuctri.shoesapi.payload.request.InventoryRequest;
import com.phuctri.shoesapi.payload.response.ApiResponse;
import com.phuctri.shoesapi.payload.response.InventoryResponse;
import com.phuctri.shoesapi.payload.response.PagedResponse;
import org.springframework.http.ResponseEntity;

public interface InventoryService {
    PagedResponse<InventoryResponse> getAllInventory(int page, int size);

    ResponseEntity<ApiResponse> getInventoryInfo(Long id);

    ResponseEntity<ApiResponse> createInventory(InventoryRequest inventoryRequest);

    ResponseEntity<ApiResponse> updateStock(Long id, Integer stock);

    ResponseEntity<ApiResponse> deleteInventory(Long id);

    ResponseEntity<ApiResponse> checkProductByInventoryInfo(Long productId, Long colorId, Long sizeId);
}
