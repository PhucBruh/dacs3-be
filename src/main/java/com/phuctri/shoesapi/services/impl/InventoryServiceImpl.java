package com.phuctri.shoesapi.services.impl;

import com.phuctri.shoesapi.entities.Inventory;
import com.phuctri.shoesapi.entities.order.OrderInfo;
import com.phuctri.shoesapi.entities.product.Color;
import com.phuctri.shoesapi.entities.product.Product;
import com.phuctri.shoesapi.entities.product.ProductStatus;
import com.phuctri.shoesapi.entities.product.Size;
import com.phuctri.shoesapi.exception.ResourceNotFoundException;
import com.phuctri.shoesapi.exception.ShoesApiException;
import com.phuctri.shoesapi.payload.request.InventoryRequest;
import com.phuctri.shoesapi.payload.response.*;
import com.phuctri.shoesapi.repository.InventoryRepository;
import com.phuctri.shoesapi.repository.ProductRepository;
import com.phuctri.shoesapi.services.InventoryService;
import com.phuctri.shoesapi.util.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    @Override
    public PagedResponse<InventoryResponse> getAllInventory(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Inventory> inventory = inventoryRepository.findAll(pageable);

        if (inventory.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), inventory.getNumber(), inventory.getSize(), inventory.getTotalElements(),
                    inventory.getTotalPages(), inventory.isLast());
        }

        List<InventoryResponse> productResponses = inventory.stream()
                .map(InventoryResponse::toInventoryResponse).toList();

        return new PagedResponse<>(productResponses, inventory.getNumber(), inventory.getSize(), inventory.getTotalElements(), inventory.getTotalPages(), inventory.isLast());
    }

    @Override
    public PagedResponse<InventoryResponse> query(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Inventory> inventory = inventoryRepository.findAllByQuery(query, pageable);

        if (inventory.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), inventory.getNumber(), inventory.getSize(), inventory.getTotalElements(),
                    inventory.getTotalPages(), inventory.isLast());
        }

        List<InventoryResponse> productResponses = inventory.stream()
                .map(InventoryResponse::toInventoryResponse).toList();

        return new PagedResponse<>(productResponses, inventory.getNumber(), inventory.getSize(), inventory.getTotalElements(), inventory.getTotalPages(), inventory.isLast());
    }

    @Override
    public ResponseEntity<ApiResponse> getInventoryInfo(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", id));

        ApiResponse response = new DataResponse(true, InventoryResponse.toInventoryResponse(inventory));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> check(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ShoesApiException(HttpStatus.OK, ""));
        ApiResponse response = new ApiResponse(true, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

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

    @Override
    public ResponseEntity<ApiResponse> checkProductByInventoryInfo(Long productId, Long colorId, Long sizeId) {
        Inventory inventory = inventoryRepository.findByProductIdAndColorIdAndSizeId(productId, colorId, sizeId)
                .orElseThrow(() -> new ShoesApiException(HttpStatus.NOT_FOUND, "This product not in inventory"));
        DataResponse dataResponse = new DataResponse(true, ProductResponse.toProductResponse(inventory.getProduct()));
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }
}
