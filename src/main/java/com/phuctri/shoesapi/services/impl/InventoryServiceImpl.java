package com.phuctri.shoesapi.services.impl;

import com.phuctri.shoesapi.entities.Inventory;
import com.phuctri.shoesapi.entities.product.Color;
import com.phuctri.shoesapi.entities.product.Image;
import com.phuctri.shoesapi.entities.product.Product;
import com.phuctri.shoesapi.entities.product.Size;
import com.phuctri.shoesapi.exception.ResourceNotFoundException;
import com.phuctri.shoesapi.payload.request.InventoryRequest;
import com.phuctri.shoesapi.payload.response.ApiResponse;
import com.phuctri.shoesapi.repository.InventoryRepository;
import com.phuctri.shoesapi.repository.ProductRepository;
import com.phuctri.shoesapi.services.InventoryService;
import com.phuctri.shoesapi.util.AppConstants;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.SimpleTimeZone;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    @Override
    public ResponseEntity<ApiResponse> createInventory(InventoryRequest inventoryRequest) {
        Inventory inventory = Inventory.builder()
                .stock(0)
                .build();

        Product product = productRepository.findById(inventoryRequest.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", inventoryRequest.getProductId()));
        inventory.setProduct(product);

        Color inventoryColor = product.getColors().stream()
                .filter(color -> color.getId().equals(inventoryRequest.getColorId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Color", "ID", inventoryRequest.getColorId()));
        inventory.setColor(inventoryColor);

        Size inventorySize = product.getSizes().stream()
                .filter(size -> size.getId().equals(inventoryRequest.getSizeId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Size", "ID", inventoryRequest.getSizeId()));
        inventory.setSize(inventorySize);

        inventoryRepository.save(inventory);
        ApiResponse response = new ApiResponse(true, AppConstants.CREATE_SUCCESSFULLY);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiResponse> updateStock(Long id, Integer stock) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", id));
        inventory.setStock(stock);

        inventoryRepository.save(inventory);
        ApiResponse response = new ApiResponse(true, AppConstants.UPDATE_SUCCESSFULLY);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteInventory(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", id));

        inventoryRepository.delete(inventory);
        ApiResponse response = new ApiResponse(true, AppConstants.DELETE_SUCCESSFULLY);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
