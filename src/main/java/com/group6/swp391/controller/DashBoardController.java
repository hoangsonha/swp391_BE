package com.group6.swp391.controller;

import com.group6.swp391.model.Order;
import com.group6.swp391.service.OrderDetailService;
import com.group6.swp391.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/swp391/api/dashboards")
public class DashBoardController {
    @Autowired OrderService orderService;
    @Autowired
    OrderDetailService orderDetailService;

    // tong doanh thu tren thang.
    @GetMapping("/total_revenue")
    public ResponseEntity<?> getTotalRevenueInMonth(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate ) {
        try {
            Double totalRevenue = orderService.getTotalRevenueInMonth(startDate, endDate);
            return ResponseEntity.ok().body(totalRevenue);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // tong tien kim cuong
    @GetMapping("/diamond_revenue")
    public ResponseEntity<?> getTotalDiamondRevenueInMonth(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            Double totalDiamondRevenue = orderDetailService.getTotalDiamondRevenueInMonth(startDate, endDate);
            return ResponseEntity.ok().body(totalDiamondRevenue);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // tong tien productcustomize
    @GetMapping("/productcustomize_revenue")
    public ResponseEntity<?> getTotalProductCustomizeRevenueInMonth(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            Double totalProductcustomizeRevenue = orderDetailService.getTotalProductcustomizeRevenueInMonth(startDate, endDate);
            return ResponseEntity.ok().body(totalProductcustomizeRevenue);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // tong / date
    @GetMapping("/total_revenue_date")
    public ResponseEntity<?> getTotalRevenueInDay(@RequestParam("date")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            Double totalRevenue = orderService.getTotalRevenueInDay(date);
            return ResponseEntity.ok().body(totalRevenue);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/total_failed")
    public ResponseEntity<?> getTotalStatusFaile(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                 @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<Order> totalFailed = orderService.getStatusInMonth(startDate, endDate, "Đã hủy");
            if(totalFailed == null || totalFailed.isEmpty()) {
                return ResponseEntity.ok().body(0);
            }
            int countFailed = totalFailed.size();
            return ResponseEntity.ok().body(countFailed);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(0);
        }
    }

    @GetMapping("/total_return")
    public ResponseEntity<?> getTotalStatusReturn(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                 @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<Order> totalFailed = orderService.getStatusInMonth(startDate, endDate, "Hoàn trả");
            if(totalFailed == null || totalFailed.isEmpty()) {
                return ResponseEntity.ok().body(0);
            }
            int countFailed = totalFailed.size();
            return ResponseEntity.ok().body(countFailed);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(0);
        }
    }

    @GetMapping("/orderpending")
    public ResponseEntity<Integer> countOrderPending() {
        try {
            List<Order> orders = orderService.getNewestOrder("Chờ xác nhận");
            if(orders == null || orders.isEmpty()) {
                return ResponseEntity.ok().body(0);
            }
            int count = orders.size();
            return ResponseEntity.ok().body(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
        }
    }


}
