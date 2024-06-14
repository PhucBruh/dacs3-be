package com.phuctri.shoesapi.services.impl;

import com.phuctri.shoesapi.entities.Inventory;
import com.phuctri.shoesapi.entities.SpecialOffer;
import com.phuctri.shoesapi.entities.product.*;
import com.phuctri.shoesapi.exception.ResourceNotFoundException;
import com.phuctri.shoesapi.payload.request.FilterRequest;
import com.phuctri.shoesapi.payload.request.InventoryRequest;
import com.phuctri.shoesapi.payload.request.ProductRequest;
import com.phuctri.shoesapi.payload.response.*;
import com.phuctri.shoesapi.repository.*;
import com.phuctri.shoesapi.services.ProductService;
import com.phuctri.shoesapi.util.AppConstants;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final InventoryRepository inventoryRepository;
    private final SpecialOfferRepository specialOfferRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Override
    public PagedResponse<ProductResponse> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Product> products = productRepository.findByStatus(ProductStatus.ACTIVE, pageable);

        if (products.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), products.getNumber(), products.getSize(), products.getTotalElements(),
                    products.getTotalPages(), products.isLast());
        }

        List<ProductResponse> productResponses = products.stream()
                .map(ProductResponse::toProductResponse).toList();

        return new PagedResponse<>(productResponses, products.getNumber(), products.getSize(), products.getTotalElements(), products.getTotalPages(), products.isLast());
    }

    @Override
    public PagedResponse<ProductResponse> searchProduct(int page, int size, FilterRequest filterRequest) {
        String jpql = "SELECT p FROM Product p WHERE 1 = 1";
        return null;
    }

    @Override
    public PagedResponse<ProductResponse> getAllProductsForAdmin(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Product> products = productRepository.findAll(pageable);

        if (products.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), products.getNumber(), products.getSize(), products.getTotalElements(),
                    products.getTotalPages(), products.isLast());
        }

        List<ProductResponse> productResponses = products.stream()
                .map(ProductResponse::toProductResponse).toList();

        return new PagedResponse<>(productResponses, products.getNumber(), products.getSize(), products.getTotalElements(), products.getTotalPages(), products.isLast());
    }

    @Override
    public PagedResponse<ProductResponse> searchProductForAdmin(int page, int size, FilterRequest filterRequest) {
        return null;
    }

    @Override
    public ResponseEntity<Product> addProduct(ProductRequest productRequest) {
        try {
            Brand brand = brandRepository.findById(productRequest.getBrandId())
                    .orElseThrow();

            Product newProduct = Product.builder()
                    .name(productRequest.getName())
                    .description(productRequest.getDescription())
                    .price(productRequest.getPrice())
                    .promotionalPrice(0.0)
                    .brand(brand)
                    .mainImg(productRequest.getMainImg())
                    .totalSold(0L)
                    .rating(5.0)
                    .status(productRequest.getStatus())
                    .build();


            List<Color> colors = productRequest.getColors().stream()
                    .map(colorRequest -> Color.builder()
                            .name(colorRequest.getName())
                            .value(colorRequest.getValue())
                            .product(newProduct)
                            .build())
                    .toList();
            newProduct.setColors(colors);

            List<Size> sizes = productRequest.getSizes().stream()
                    .map(size -> Size.builder()
                            .size(size)
                            .product(newProduct)
                            .build())
                    .toList();
            newProduct.setSizes(sizes);

            List<Image> images = productRequest.getImgs().stream()
                    .map(url -> Image.builder()
                            .url(url)
                            .product(newProduct)
                            .build())
                    .toList();
            newProduct.setImgs(images);

            productRepository.save(newProduct);

            return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error saving product: ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ApiResponse> getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", id));
        DataResponse response = new DataResponse(true, product);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> getProductPrice(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", id));
        DataResponse response = new DataResponse(true, ProductPriceResponse.toProductPriceResponse(product));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> updateProduct(Long id, ProductRequest updateProductRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", id));

        if (updateProductRequest.getName() != null && !updateProductRequest.getName().isEmpty()) {
            product.setName(updateProductRequest.getName());
        }

        if (updateProductRequest.getDescription() != null && !updateProductRequest.getDescription().isEmpty()) {
            product.setDescription(updateProductRequest.getDescription());
        }

        if (updateProductRequest.getBrandId() != 0) {
            Brand brand = brandRepository.findById(updateProductRequest.getBrandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Brand", "ID", updateProductRequest.getBrandId()));
            product.setBrand(brand);
        }

        if (updateProductRequest.getMainImg() != null && !updateProductRequest.getMainImg().isEmpty()) {
            product.setMainImg(updateProductRequest.getMainImg());
        }

        if (updateProductRequest.getPrice() != 0.0) {
            product.setPrice(updateProductRequest.getPrice());
        }

        if (updateProductRequest.getColors() != null && !updateProductRequest.getColors().isEmpty()) {
            product.getColors().addAll(
                    updateProductRequest.getColors().stream()
                            .map(colorRequest -> Color.builder()
                                    .name(colorRequest.getName())
                                    .value(colorRequest.getValue())
                                    .product(product)
                                    .build())
                            .toList()
            );
        }

        if (updateProductRequest.getSizes() != null && !updateProductRequest.getSizes().isEmpty()) {
            product.getSizes().addAll(
                    updateProductRequest.getSizes().stream()
                            .map(size -> Size.builder()
                                    .size(size)
                                    .product(product)
                                    .build())
                            .toList()
            );
        }

        if (updateProductRequest.getImgs() != null && !updateProductRequest.getImgs().isEmpty()) {
            product.getImgs().addAll(
                    updateProductRequest.getImgs().stream()
                            .map(url -> Image.builder()
                                    .url(url)
                                    .product(product)
                                    .build())
                            .toList()
            );
        }

        Product updatedProduct = productRepository.save(product);
        DataResponse response = new DataResponse(true, AppConstants.UPDATE_SUCCESSFULLY, updatedProduct);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", id));

        List<Inventory> inventories = inventoryRepository.findByProduct(product);
        if (!inventories.isEmpty()) {
            ApiResponse response = new ApiResponse(false, "Some inventory is using this product");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        List<SpecialOffer> specialOffers = specialOfferRepository.findByProduct(product);
        if (!specialOffers.isEmpty()) {
            ApiResponse response = new ApiResponse(false, "Some special offer is using this product");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        try {
            productRepository.delete(product);
            ApiResponse response = new ApiResponse(true, AppConstants.DELETE_SUCCESSFULLY);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(false, AppConstants.SOMETHING_WENT_WRONG);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<ApiResponse> deleteProductImage(Long productId, Long imgId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", productId));

        Image img = product.getImgs().stream()
                .filter(image -> image.getId().equals(imgId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Image", "ID", imgId));
        product.getImgs().remove(img);
        productRepository.save(product);
        ApiResponse response = new ApiResponse(true, AppConstants.DELETE_SUCCESSFULLY);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteProductColor(Long productId, Long colorId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", productId));

        Color colorToDelete = product.getColors().stream()
                .filter(color -> color.getId().equals(colorId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Color", "ID", colorId));
        product.getColors().remove(colorToDelete);

        List<Inventory> inventories = inventoryRepository.findByColor(colorToDelete);
        if (!inventories.isEmpty()) {
            ApiResponse response = new ApiResponse(false, "Some inventories is using this product");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        productRepository.save(product);
        ApiResponse response = new ApiResponse(true, AppConstants.DELETE_SUCCESSFULLY);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteProductSize(Long productId, Long sizeId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", productId));
        Size sizeToDelete = product.getSizes().stream()
                .filter(size -> size.getId().equals(sizeId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Size", "ID", sizeId));
        product.getSizes().remove(sizeToDelete);

        List<Inventory> inventories = inventoryRepository.findBySize(sizeToDelete);
        if (!inventories.isEmpty()) {
            ApiResponse response = new ApiResponse(false, "Some inventories is using this product");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        productRepository.save(product);
        ApiResponse response = new ApiResponse(true, AppConstants.DELETE_SUCCESSFULLY);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
