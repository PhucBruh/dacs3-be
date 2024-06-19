package com.phuctri.shoesapi.controller;

import com.phuctri.shoesapi.payload.request.InventoryRequest;
import com.phuctri.shoesapi.payload.response.AnalysisResponse;
import com.phuctri.shoesapi.payload.response.ApiResponse;
import com.phuctri.shoesapi.payload.response.DataResponse;
import com.phuctri.shoesapi.payload.response.WeeklyReport;
import com.phuctri.shoesapi.services.InventoryService;
import com.phuctri.shoesapi.services.OrderService;
import com.phuctri.shoesapi.util.AppConstants;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class AnalysisController {

    private final InventoryService inventoryService;
    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAnalysis() {
        AnalysisResponse analysisResponse = orderService.getAnalysis();
        ApiResponse apiResponse = new DataResponse(true, analysisResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/monthly")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<WeeklyReport>> getMonthlyAnalysis(
            @RequestParam("year") int year,
            @RequestParam("month") int month
    ) {
        List<WeeklyReport> weeklyReports = orderService.getWeeklyReports(year, month);
        return ResponseEntity.ok(weeklyReports);
    }

    @GetMapping("/inventory")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getInventory(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size
    ) {
        return null;
    }

    @GetMapping("/inventory/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getInventoryOfProduct(@PathVariable Long id) {
        return null;
    }

    @PostMapping("/inventory")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createInventory(@RequestBody InventoryRequest inventoryRequest) {
        return inventoryService.createInventory(inventoryRequest);
    }

    @PutMapping("/inventory/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateStock(
            @PathVariable Long id,
            @RequestParam(name = "stock", required = false, defaultValue = "0") Integer stock) {
        return inventoryService.updateStock(id, stock);
    }

    @DeleteMapping("/inventory/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteInventory(@PathVariable Long id) {
        return inventoryService.deleteInventory(id);
    }
}