package com.phuctri.shoesapi.services.impl;

import com.phuctri.shoesapi.entities.Inventory;
import com.phuctri.shoesapi.entities.order.OrderInfo;
import com.phuctri.shoesapi.entities.order.OrderStatus;
import com.phuctri.shoesapi.entities.product.ProductStatus;
import com.phuctri.shoesapi.exception.ResourceNotFoundException;
import com.phuctri.shoesapi.exception.ShoesApiException;
import com.phuctri.shoesapi.payload.response.*;
import com.phuctri.shoesapi.repository.InventoryRepository;
import com.phuctri.shoesapi.repository.OrderRepository;
import com.phuctri.shoesapi.repository.ProductRepository;
import com.phuctri.shoesapi.services.OrderService;
import com.phuctri.shoesapi.util.AppConstants;
import lombok.AllArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    @Override
    public AnalysisResponse getAnalysis() {
        AnalysisResponse analysisResponse = new AnalysisResponse(
                0.0,
                0.0,
                0,
                0
        );
        Double monthlyEarning = orderRepository.findMonthlyEarnings(LocalDate.now().getYear(), LocalDate.now().getMonthValue());
        Double yearlyEarning = orderRepository.findYearlyEarnings(LocalDate.now().getYear());
        Integer incompleteOrder = orderRepository.countByStatusNotIn(List.of(OrderStatus.FAILED,
                OrderStatus.CANCELED,
                OrderStatus.REFUNDED,
                OrderStatus.COMPLETED));
        Integer completedOrder = orderRepository.countByStatus(OrderStatus.COMPLETED);

        if (monthlyEarning != null) analysisResponse.setMonthly(monthlyEarning);
        if (yearlyEarning != null) analysisResponse.setYearly(yearlyEarning);
        analysisResponse.setIncompleteOrder(incompleteOrder);
        analysisResponse.setCompletedOrder(completedOrder);

        return analysisResponse;
    }


    @Override
    public List<WeeklyReport> getWeeklyReports(int year, int month) {
        List<WeeklyReport> reports = new ArrayList<>();

        LocalDate firstOfMonth = LocalDate.of(year, month, 1);
        LocalDate lastOfMonth = firstOfMonth.with(TemporalAdjusters.lastDayOfMonth());

        LocalDate current = firstOfMonth;
        while (current.isBefore(lastOfMonth) || current.equals(lastOfMonth)) {
            LocalDate weekStart = current.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate weekEnd = current.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

            if (weekEnd.isAfter(lastOfMonth)) {
                weekEnd = lastOfMonth;
            }

            Date startDate = java.sql.Date.valueOf(weekStart);
            Date endDate = java.sql.Date.valueOf(weekEnd);

            long completedOrders = orderRepository.countCompletedOrders(OrderStatus.COMPLETED, startDate, endDate);
            Double earnings = orderRepository.calculateEarnings(OrderStatus.COMPLETED, startDate, endDate);

            if (earnings == null) {
                earnings = 0.0;
            }

            WeeklyReport report = new WeeklyReport(weekStart.toString(), weekEnd.toString(), completedOrders, earnings);
            reports.add(report);

            current = current.plusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        }

        return reports;
    }

    @Override
    public PagedResponse<OrderResponse> getAllOrder(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<OrderInfo> orderInfos
                = orderRepository.findAll(pageable);

        if (orderInfos.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), orderInfos.getNumber(), orderInfos.getSize(), orderInfos.getTotalElements(),
                    orderInfos.getTotalPages(), orderInfos.isLast());
        }

        List<OrderResponse> orderResponses = orderInfos.stream()
                .map(OrderResponse::toOrderResponse).toList();

        return new PagedResponse<>(orderResponses, orderInfos.getNumber(), orderInfos.getSize(), orderInfos.getTotalElements(), orderInfos.getTotalPages(), orderInfos.isLast());
    }

    @Override
    public PagedResponse<OrderResponse> query(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<OrderInfo> orderInfos
                = orderRepository.findAllByQuery(query, pageable);

        if (orderInfos.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), orderInfos.getNumber(), orderInfos.getSize(), orderInfos.getTotalElements(),
                    orderInfos.getTotalPages(), orderInfos.isLast());
        }

        List<OrderResponse> orderResponses = orderInfos.stream()
                .map(OrderResponse::toOrderResponse).toList();

        return new PagedResponse<>(orderResponses, orderInfos.getNumber(), orderInfos.getSize(), orderInfos.getTotalElements(), orderInfos.getTotalPages(), orderInfos.isLast());
    }

    @Override
    public ResponseEntity<ApiResponse> getOrderInfo(Long id) {
        OrderInfo order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "ID", id));

        DataResponse response = new DataResponse(true, OrderDetailResponse.toOrderResponse(order));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> check(Long id) {
        OrderInfo order = orderRepository.findById(id)
                .orElseThrow(() -> new ShoesApiException(HttpStatus.OK, ""));
        ApiResponse response = new ApiResponse(true, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> updateOrderStatus(Long id, OrderStatus orderStatus) {
        OrderInfo orderInfo = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "ID", id));

        if (orderStatus.equals(orderInfo.getStatus())) {
            ApiResponse response = new ApiResponse(false,
                    "current order status is " + orderStatus.toString());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        switch (orderInfo.getStatus()) {
            case COMPLETED, CANCELED, REFUNDED, FAILED -> {
                ApiResponse response = new ApiResponse(false,
                        "Cannot update the order that 'COMPLETED', 'CANCELED', 'REFUNDED' and 'FAILED', pls create new one");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }

        orderInfo.setStatus(orderStatus);

        switch (orderStatus) {
            case CANCELED, FAILED, REFUNDED -> {
                orderInfo.getOrderDetails().forEach(orderDetail -> {
                    Optional<Inventory> optionalInventory = inventoryRepository.findByProductAndColorAndSize(
                            orderDetail.getProduct(),
                            orderDetail.getColor(),
                            orderDetail.getSize());
                    optionalInventory.ifPresent(inventory -> {
                        if (inventory.getProduct().getStatus().equals(ProductStatus.OUT_OF_STOCK)) {
                            inventory.getProduct().setStatus(ProductStatus.ACTIVE);
                            productRepository.save(inventory.getProduct());
                        }
                        optionalInventory.get().setStock(optionalInventory.get().getStock() + orderDetail.getQuantity());
                        inventoryRepository.save(inventory);
                    });
                });
            }
            case COMPLETED -> {
                orderInfo.getOrderDetails().forEach(orderDetail -> {
                    orderDetail.getProduct().setTotalSold(
                            orderDetail.getProduct().getTotalSold() + orderDetail.getQuantity()
                    );
                    productRepository.save(orderDetail.getProduct());
                });
            }
            default -> {
            }
        }

        OrderInfo newOrderInfo = orderRepository.save(orderInfo);
        DataResponse response = new DataResponse(true, AppConstants.UPDATE_SUCCESSFULLY, OrderDetailResponse.toOrderResponse(newOrderInfo));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}