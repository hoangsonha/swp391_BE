package com.group6.swp391.controller;

import com.group6.swp391.model.Order;
import com.group6.swp391.response.ObjectResponse;
import com.group6.swp391.service.DashboardService;
import com.group6.swp391.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.interfaces.EdECKey;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/swp391/api/dashboards")
public class DashboardController {
    @Autowired OrderService orderService;
    @Autowired DashboardService dashboardService;

    //tong loi nhuan tu luc kinh doanh
    @GetMapping("/total_revenue_store")
    public ResponseEntity<ObjectResponse> totelRevenue() {
        try {
            double toteal = 0.0;
            List<Order> orders = dashboardService.totalRevenueStore();
            if(orders == null || orders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Failed", "get data failed", null));
            }
            for (Order order : orders) {
                toteal = order.getPrice();
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Total Revenue Store", toteal));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "get data failed", null));
        }
    }
    //tong doanh thu tung thang theo bieu do
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "get data failed", null));
        }
    }

    //tong doanh thu kim cuong cua tung thanng theo bieu do
    @GetMapping("/total_revenue_diamond")
    public ResponseEntity<ObjectResponse> gettotalDiamondRevenue() {
        try {
            int currentYear = LocalDate.now().getYear();
            List<Double> totalRevenue = new ArrayList<>();
            for(int i = 1; i < 13; i++) {
                Double moonth = dashboardService.getTotalRevenueDiamond(i, currentYear);
                totalRevenue.add(moonth);
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Total Diamond Revenue in month", totalRevenue));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "get data failed", null));
        }
    }

    // tong doanh thu cua customize cua tung thang theo bieu do
    @GetMapping("/total_revenue_productcustomize")
    public ResponseEntity<ObjectResponse> gettotalProductCustomizeRevenue() {
        try {
            int currentYear = LocalDate.now().getYear();
            List<Double> totalRevenue = new ArrayList<>();
            for(int i = 1; i < 13; i++) {
                Double moonth = dashboardService.getTotalRevenueProductcustomizer(i, currentYear);
                totalRevenue.add(moonth);
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Total Productcustomize Revenue in month", totalRevenue));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "get data failed", null));
        }
    }

    //list order dang doi xac nhan
    @GetMapping("/listneworder")
    public ResponseEntity<ObjectResponse> listNewOrder() {
        try {
            int monthCurrent = LocalDate.now().getMonthValue();
            List<Order> listNewOrder = orderService.getNewestOrder("Chờ xác nhận");
            if(listNewOrder == null || listNewOrder.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "List Is empty", null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "List new Order", listNewOrder));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "List Is empty", null));
        }
    }

    // list order bi huy
    @GetMapping("/listorderfailed")
    public ResponseEntity<ObjectResponse> listOrderFailed() {
        try {
            int monthCurrent = LocalDate.now().getMonthValue();
            List<Order> listOrderFailed = dashboardService.getOrderByStatus(monthCurrent, "Đã hủy");
            if(listOrderFailed == null || listOrderFailed.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "List Is empty", null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "List order failed", listOrderFailed));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "List Is empty", null));
        }
    }

    //list order bij trar lai trong thang
    @GetMapping("/listorderreturn")
    public ResponseEntity<ObjectResponse> listOrderReturn() {
        try {
            int monthCurrent = LocalDate.now().getMonthValue();
            List<Order> listOrderReturn = dashboardService.getOrderByStatus(monthCurrent, "Hoàn trả");
            if(listOrderReturn == null || listOrderReturn.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "List Is empty", null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "List order return", listOrderReturn));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "List Is empty", null));
        }
    }

    // list order duoc giao thanh cong trong thang
    @GetMapping("/listordersuccessfully")
    public ResponseEntity<ObjectResponse> listOrderSuccessfully() {
        try {
            int monthCurrent = LocalDate.now().getMonthValue();
            List<Order> listOrderSuccessfully = dashboardService.getOrderByStatus(monthCurrent, "Giao thành công");
            if(listOrderSuccessfully == null || listOrderSuccessfully.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "List Is empty", null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "List order return", listOrderSuccessfully));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "List Is empty", null));
        }
    }

    // so luong user moi
    @GetMapping("/newuser")
    public ResponseEntity<ObjectResponse> newUser() {
        try {
            int monthCurrent = LocalDate.now().getMonthValue();
            int yearCurrent = LocalDate.now().getYear();
            Integer newUser = dashboardService.newUser(monthCurrent, yearCurrent);
            if(newUser == null || newUser == 0) {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "No new user", 0));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Quantity new user", newUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "failed", null));
        }
    }

    // so sanh loi nhuan giua 2 thansg
    @GetMapping("/compare_month")
    public ResponseEntity<ObjectResponse> compareBetweenTwoMonth() {
        try {
            int monthCurrent = LocalDate.now().getMonthValue();
            int monthPrev = monthCurrent - 1;
            int year = LocalDate.now().getYear();
            Double totalRevenueMonthPrev = dashboardService.getTotalRevenueInMonth(monthPrev, year);
            Double totalRevenueMonthCurrent = dashboardService.getTotalRevenueInMonth(monthCurrent, year);
            Double totalCompare = (((totalRevenueMonthCurrent - totalRevenueMonthPrev)/totalRevenueMonthPrev) * 100);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get value compare between two month", totalCompare));
        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "failed", null));
        }
    }
}
