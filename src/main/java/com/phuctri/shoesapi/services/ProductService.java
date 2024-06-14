package com.phuctri.shoesapi.services;

import com.phuctri.shoesapi.entities.product.Product;
import com.phuctri.shoesapi.payload.request.FilterRequest;
import com.phuctri.shoesapi.payload.request.ProductRequest;
import com.phuctri.shoesapi.payload.response.ApiResponse;
import com.phuctri.shoesapi.payload.response.ProductResponse;
import com.phuctri.shoesapi.payload.response.PagedResponse;
import org.springframework.http.ResponseEntity;

public interface ProductService {

    PagedResponse<ProductResponse> getAllProducts(int page, int size);

    PagedResponse<ProductResponse> searchProduct(int page, int size, FilterRequest filterRequest);

    PagedResponse<ProductResponse> getAllProductsForAdmin(int page, int size);

    PagedResponse<ProductResponse> searchProductForAdmin(int page, int size, FilterRequest filterRequest);

    ResponseEntity<Product> addProduct(ProductRequest productRequest);

    ResponseEntity<ApiResponse> getProduct(Long id);

    ResponseEntity<ApiResponse> getProductPrice(Long id);

    ResponseEntity<ApiResponse> updateProduct(Long id, ProductRequest updateProductRequest);

    ResponseEntity<ApiResponse> deleteProduct(Long id);

    ResponseEntity<ApiResponse> deleteProductImage(Long productId, Long imgId);

    ResponseEntity<ApiResponse> deleteProductColor(Long productId, Long colorId);

    ResponseEntity<ApiResponse> deleteProductSize(Long productId, Long sizeId);
}