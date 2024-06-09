package com.phuctri.shoesapi.services;


import com.phuctri.shoesapi.payload.request.InventoryRequest;
import com.phuctri.shoesapi.payload.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface InventoryService {
    ResponseEntity<ApiResponse> createInventory(InventoryRequest inventoryRequest);

    ResponseEntity<ApiResponse> updateStock(Long id, Integer stock);

    ResponseEntity<ApiResponse> deleteInventory(Long id);
}
