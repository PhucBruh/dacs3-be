package com.phuctri.shoesapi.controller;

import com.phuctri.shoesapi.payload.request.ChangePasswordRequest;
import com.phuctri.shoesapi.payload.request.OrderRequest;
import com.phuctri.shoesapi.payload.response.ApiResponse;
import com.phuctri.shoesapi.security.CurrentUser;
import com.phuctri.shoesapi.security.UserPrincipal;
import com.phuctri.shoesapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> getCurrentUserInfo(@CurrentUser UserPrincipal currentUser) {
        return userService.getCurrentUserProfile(currentUser);
    }

    @PostMapping("/me/password")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> updatePassword(@CurrentUser UserPrincipal currentUser, @RequestBody ChangePasswordRequest changePasswordRequest) {
        return null;
    }

    @GetMapping("/me/order")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> getCurrentUserOrder(
            @CurrentUser UserPrincipal currentUser,
            @RequestParam(name = "completed", required = false, defaultValue = "false") boolean completed) {
        return userService.getCurrentUserOrderList(currentUser, completed);
    }

    @GetMapping("/me/order/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> getCurrentUserOrderById(@CurrentUser UserPrincipal currentUser, @PathVariable Long id) {
        return userService.getCurrentUserOrderById(currentUser, id);
    }

    @PostMapping("/me/order")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> createNewOrder(@CurrentUser UserPrincipal currentUser, @RequestBody OrderRequest orderRequest) {
        return userService.createOrder(currentUser, orderRequest);
    }
}
