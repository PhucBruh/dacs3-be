package com.phuctri.shoesapi.services.impl;

import com.phuctri.shoesapi.entities.Inventory;
import com.phuctri.shoesapi.entities.SpecialOffer;
import com.phuctri.shoesapi.entities.product.*;
import com.phuctri.shoesapi.exception.ResourceNotFoundException;
import com.phuctri.shoesapi.payload.request.FilterRequest;
import com.phuctri.shoesapi.payload.request.ProductRequest;
import com.phuctri.shoesapi.payload.response.ApiResponse;
import com.phuctri.shoesapi.payload.response.DataResponse;
import com.phuctri.shoesapi.payload.response.PagedResponse;
import com.phuctri.shoesapi.payload.response.ProductResponse;
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

import java.util.Collections;
import java.util.List;

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

        Page<Product> products = productRepository.findByStatusNot(ProductStatus.IN_ACTIVE, pageable);

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
                    .build();


            List<Color> colors = productRequest.getColors().stream()
                    .map(colorRequest -> Color.builder()
                            .name(colorRequest.getName())
                            .value(colorRequest.getValue())
                            .product(newProduct)
                            .build())
                    .toList();
            newProduct.setColors(colors);

            List<Size> sizes = productRequest.getSize().stream()
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
    public ResponseEntity<ApiResponse> updateProduct(Long id, ProductRequest newProduct) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", id));

        if (newProduct.getName() != null && !newProduct.getName().isEmpty()) {
            product.setName(newProduct.getName());
        }

        if (newProduct.getDescription() != null && !newProduct.getDescription().isEmpty()) {
            product.setDescription(newProduct.getDescription());
        }

        if (newProduct.getBrandId() != 0) {
            Brand brand = brandRepository.findById(newProduct.getBrandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Brand", "ID", newProduct.getBrandId()));
            product.setBrand(brand);
        }

        if (newProduct.getMainImg() != null && !newProduct.getMainImg().isEmpty()) {
            product.setMainImg(newProduct.getMainImg());
        }

        if (newProduct.getPrice() != 0.0) {
            product.setPrice(newProduct.getPrice());
        }

        if (newProduct.getColors() != null && !newProduct.getColors().isEmpty()) {
            product.getColors().addAll(
                    newProduct.getColors().stream()
                            .map(colorRequest -> Color.builder()
                                    .name(colorRequest.getName())
                                    .value(colorRequest.getValue())
                                    .product(product)
                                    .build())
                            .toList()
            );
        } else if (newProduct.getColors() != null) {
            product.getColors().clear();
        }

        if (newProduct.getSize() != null && !newProduct.getSize().isEmpty()) {
            product.getSizes().addAll(
                    newProduct.getSize().stream()
                            .map(size -> Size.builder()
                                    .size(size)
                                    .product(product)
                                    .build())
                            .toList()
            );
        } else if (newProduct.getSize() != null) {
            product.getSizes().clear();
        }

        if (newProduct.getImgs() != null && !newProduct.getImgs().isEmpty()) {
            product.getImgs().addAll(
                    newProduct.getImgs().stream()
                            .map(url -> Image.builder()
                                    .url(url)
                                    .product(product)
                                    .build())
                            .toList()
            );
        } else if (newProduct.getImgs() != null) {
            product.getImgs().clear();
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
