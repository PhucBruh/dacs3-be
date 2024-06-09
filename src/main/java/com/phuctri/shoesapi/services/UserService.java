package com.phuctri.shoesapi.services;

import com.phuctri.shoesapi.payload.request.ChangePasswordRequest;
import com.phuctri.shoesapi.payload.request.OrderRequest;
import com.phuctri.shoesapi.payload.response.ApiResponse;
import com.phuctri.shoesapi.security.UserPrincipal;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<ApiResponse> getCurrentUserProfile(UserPrincipal currentUser);

    ResponseEntity<ApiResponse> getUserProfile(String username);

    ResponseEntity<ApiResponse> getCurrentUserOrderList(UserPrincipal currentUser);

    ResponseEntity<ApiResponse> getCurrentUserOrderById(UserPrincipal currentUser, Long id);

    ResponseEntity<ApiResponse> createOrder(UserPrincipal currentUser, OrderRequest order);

    ResponseEntity<ApiResponse> changePassword(UserPrincipal currentUser, ChangePasswordRequest changePasswordRequest);
}
