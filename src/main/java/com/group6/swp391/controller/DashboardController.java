package com.group6.swp391.controller;

import com.group6.swp391.response.ObjectResponse;
import com.group6.swp391.service.DashboardService;
import com.group6.swp391.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/swp391/api/dashboards")
public class DashboardController {
    @Autowired OrderService orderService;
    @Autowired DashboardService dashboardService;

    @GetMapping("/total_revenue")
    public ResponseEntity<ObjectResponse> getTotalRevenue() {
        try {
            int currentYear = LocalDate.now().getYear();
            List<Double> totalRevenue = new ArrayList<>();
            for(int i = 1; i < 13; i++) {
                Double moonth = dashboardService.getTotalRevenueInMonth(i,currentYear);
                totalRevenue.add(moonth);
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Total Revenue in month", totalRevenue));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Failed", "get data failed", null));
        }
    }

    @GetMapping("/total_revenue_diamond")
    public ResponseEntity<ObjectResponse> gettotalDiamondRevenue() {
        try {
            int currentYear = LocalDate.now().getYear();
            List<Double> totalRevenue = new ArrayList<>();
            for(int i = 1; i < 13; i++) {
                Double moonth = dashboardService.getTotalRevenueDiamond(i, currentYear);
                totalRevenue.add(moonth);
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Total Revenue in month", totalRevenue));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Failed", "get data failed", null));
        }
    }

    @GetMapping("/total_revenue_productcustomize")
    public ResponseEntity<ObjectResponse> gettotalProductCustomizeRevenue() {
        try {
            int currentYear = LocalDate.now().getYear();
            List<Double> totalRevenue = new ArrayList<>();
            for(int i = 1; i < 13; i++) {
                Double moonth = dashboardService.getTotalRevenueProductcustomizer(i, currentYear);
                totalRevenue.add(moonth);
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Total Revenue in month", totalRevenue));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Failed", "get data failed", null));
        }
    }



}
