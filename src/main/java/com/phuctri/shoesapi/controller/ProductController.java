package com.phuctri.shoesapi.controller;

import com.phuctri.shoesapi.entities.product.Product;
import com.phuctri.shoesapi.payload.request.FilterRequest;
import com.phuctri.shoesapi.payload.request.ProductRequest;
import com.phuctri.shoesapi.payload.response.ApiResponse;
import com.phuctri.shoesapi.payload.response.PagedResponse;
import com.phuctri.shoesapi.payload.response.ProductResponse;
import com.phuctri.shoesapi.services.ProductService;
import com.phuctri.shoesapi.util.AppConstants;
import com.phuctri.shoesapi.util.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public PagedResponse<ProductResponse> getProductForAdmin(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size
    ) {
        AppUtils.validatePageNumberAndSize(page, size);
        return productService.getAllProductsForAdmin(page, size);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public PagedResponse<ProductResponse> getProduct(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size
    ) {
        AppUtils.validatePageNumberAndSize(page, size);
        return productService.getAllProducts(page, size);
    }


    @GetMapping("/admin/s")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> searchProductsForAdmin(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(required = false) String query
    ) {
        ApiResponse response = new ApiResponse(true, "search product");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/admin/s/f")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> searchHavFilterProductsForAdmin(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String saleStatus,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) double minPrice,
            @RequestParam(required = false) double maxPrice
    ) {
        ApiResponse response = new ApiResponse(true, "search product");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/s")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> searchProducts(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(required = false) String query
    ) {
        ApiResponse response = new ApiResponse(true, "search product");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @GetMapping("/{id}/price")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> getProductPriceById(@PathVariable Long id) {
        return productService.getProductPrice(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> addProduct(@RequestBody ProductRequest productRequest) {
        return productService.addProduct(productRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductRequest newProduct, @PathVariable Long id) {
        return productService.updateProduct(id, newProduct);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

    @DeleteMapping("{productId}/image/{imgId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteProductImage(@PathVariable Long productId, @PathVariable Long imgId) {
        return productService.deleteProductImage(productId, imgId);
    }

    @DeleteMapping("{productId}/size/{sizeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteProductSize(@PathVariable Long productId, @PathVariable Long sizeId) {
        return productService.deleteProductSize(productId, sizeId);
    }

    @DeleteMapping("{productId}/colors/{colorId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteProductColor(@PathVariable Long productId, @PathVariable Long colorId) {
        return productService.deleteProductColor(productId, colorId);
    }
}