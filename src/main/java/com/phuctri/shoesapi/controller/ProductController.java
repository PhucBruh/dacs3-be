package com.phuctri.shoesapi.controller;

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

    @GetMapping("/q")
    @PreAuthorize("hasRole('ADMIN')")
    public PagedResponse<ProductResponse> query(
            @RequestParam(name = "query", required = false, defaultValue = "") String query,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size
    ) {
        AppUtils.validatePageNumberAndSize(page, size);
        return productService.getAllProductsByQueryByAdmin(query, page, size);
    }

    @GetMapping("/brand/{id}")
    @PreAuthorize("hasRole('USER')")
    public PagedResponse<ProductResponse> getProductByBrandId(
            @PathVariable Long id,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size
    ) {
        AppUtils.validatePageNumberAndSize(page, size);
        return productService.getAllProductsByBrandId(id, page, size);
    }


    @GetMapping("/s")
    @PreAuthorize("hasRole('USER')")
    public PagedResponse<ProductResponse> search(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "query", required = false, defaultValue = "") String query,
            @RequestParam(name = "isFilter", required = false, defaultValue = "false") Boolean isFilter,
            @RequestParam(name = "saleStatus", required = false, defaultValue = "NORMAL") String saleStatus,
            @RequestParam(name = "orderBy", required = false, defaultValue = "NAME") String orderBy,
            @RequestParam(name = "sortBy", required = false, defaultValue = "ASCENDING") String sortBy,
            @RequestParam(name = "minPrice", required = false, defaultValue = "0") double minPrice,
            @RequestParam(name = "maxPrice", required = false, defaultValue = "5000000") double maxPrice
    ) {
        return productService.search(
                query,
                isFilter,
                saleStatus,
                orderBy,
                sortBy,
                minPrice,
                maxPrice,
                page,
                size
        );
    }

    @GetMapping("/check/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> check(
            @PathVariable Long id
    ) {
        return productService.check(id);
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
    public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductRequest productRequest) {
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