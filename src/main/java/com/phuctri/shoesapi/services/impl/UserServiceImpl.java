package com.phuctri.shoesapi.services.impl;

import com.phuctri.shoesapi.entities.Inventory;
import com.phuctri.shoesapi.entities.User;
import com.phuctri.shoesapi.entities.order.OrderInfo;
import com.phuctri.shoesapi.entities.order.OrderDetail;
import com.phuctri.shoesapi.entities.order.OrderStatus;
import com.phuctri.shoesapi.entities.product.Color;
import com.phuctri.shoesapi.entities.product.Product;
import com.phuctri.shoesapi.entities.product.Size;
import com.phuctri.shoesapi.exception.ResourceNotFoundException;
import com.phuctri.shoesapi.exception.ShoesApiException;
import com.phuctri.shoesapi.payload.request.ChangePasswordRequest;
import com.phuctri.shoesapi.payload.request.OrderRequest;
import com.phuctri.shoesapi.payload.response.ApiResponse;
import com.phuctri.shoesapi.payload.response.DataResponse;
import com.phuctri.shoesapi.payload.response.OrderDetailResponse;
import com.phuctri.shoesapi.payload.response.UserProfile;
import com.phuctri.shoesapi.repository.InventoryRepository;
import com.phuctri.shoesapi.repository.OrderRepository;
import com.phuctri.shoesapi.repository.ProductRepository;
import com.phuctri.shoesapi.repository.UserRepository;
import com.phuctri.shoesapi.security.UserPrincipal;
import com.phuctri.shoesapi.services.UserService;
import com.phuctri.shoesapi.util.AppConstants;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public ResponseEntity<ApiResponse> getCurrentUserProfile(UserPrincipal currentUser) {
        User user = userRepository.getUser(currentUser);
        UserProfile profile = UserProfile.builder()
                .id(user.getId())
                .firstName(user.getFirstname())
                .lastName(user.getLastname())
                .phone(user.getPhone())
                .email(user.getEmail())
                .username(user.getUsername())
                .roles(user.getRoles().stream()
                        .map(role -> role.getName().toString())
                        .toList())
                .build();
        DataResponse response = new DataResponse(true, profile);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> getUserProfile(String username) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> getCurrentUserOrderList(UserPrincipal currentUser) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> getCurrentUserOrderById(UserPrincipal currentUser, Long id) {
        OrderInfo order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "ID", id));
        if (!order.getUser().getId().equals(currentUser.getId())) {
            throw new ShoesApiException(HttpStatus.FORBIDDEN, "you can't access this order");
        }

        DataResponse response = new DataResponse(true, OrderDetailResponse.toOrderResponse(order));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional(rollbackOn = ShoesApiException.class)
    public ResponseEntity<ApiResponse> createOrder(UserPrincipal currentUser, OrderRequest orderRequest) {
        User user = userRepository.getUser(currentUser);
        OrderInfo order = OrderInfo.builder()
                .user(user)
                .shippingAddress(orderRequest.getShippingAddress())
                .description(orderRequest.getDescription())
                .status(OrderStatus.PENDING)
                .build();

        List<OrderDetail> orderDetails = orderRequest.getDetail().stream()
                .map(orderDetailRequest -> {
                    Product detailProduct = productRepository.findById(orderDetailRequest.getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", orderDetailRequest.getProductId()));

                    Color detailColor = detailProduct.getColors().stream()
                            .filter(color -> color.getId().equals(orderDetailRequest.getColorId()))
                            .findFirst()
                            .orElseThrow(() -> new ResourceNotFoundException("Color product", "ID", orderDetailRequest.getColorId()));

                    Size detailSize = detailProduct.getSizes().stream()
                            .filter(size -> size.getId().equals(orderDetailRequest.getSizeId()))
                            .findFirst()
                            .orElseThrow(() -> new ResourceNotFoundException("Size product", "ID", orderDetailRequest.getColorId()));

                    Inventory inventory = inventoryRepository.findByProductIdAndColorIdAndSizeId(detailProduct.getId(), detailColor.getId(), detailSize.getId())
                            .orElseThrow(() -> new ShoesApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                                    "There no product '%s' with sizes '%s', color '%s' in inventory"
                                            .formatted(detailProduct.getName(), detailSize.getSize(), detailColor.getName())));

                    if (orderDetailRequest.getQuantity() > inventory.getStock()) {
                        throw new ShoesApiException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "There are %s stock product '%s' with sizes '%s', color '%s' left!"
                                        .formatted(inventory.getStock(), detailProduct.getName(), detailSize.getSize(), detailColor.getName()));
                    }

                    inventory.setStock(inventory.getStock() - orderDetailRequest.getQuantity());
                    inventoryRepository.save(inventory);

                    Double price = detailProduct.getPromotionalPrice() != 0.0
                            ? detailProduct.getPromotionalPrice() * orderDetailRequest.getQuantity()
                            : detailProduct.getPrice() * orderDetailRequest.getQuantity();

                    return OrderDetail.builder()
                            .order(order)
                            .product(detailProduct)
                            .quantity(orderDetailRequest.getQuantity())
                            .color(detailColor)
                            .size(detailSize)
                            .price(price)
                            .build();
                })
                .toList();
        order.setOrderDetails(orderDetails);
        order.setPrice(orderDetails.stream()
                .mapToDouble(OrderDetail::getPrice)
                .sum());

        OrderInfo newOrder = orderRepository.save(order);

        DataResponse response = new DataResponse(true, AppConstants.CREATE_SUCCESSFULLY, OrderDetailResponse.toOrderResponse(newOrder));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiResponse> changePassword(UserPrincipal currentUser, ChangePasswordRequest changePasswordRequest) {
        return null;
    }
}
