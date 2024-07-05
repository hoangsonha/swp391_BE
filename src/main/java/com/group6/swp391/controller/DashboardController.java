package com.group6.swp391.controller;

import com.group6.swp391.model.Diamond;
import com.group6.swp391.model.Order;
import com.group6.swp391.model.Product;
import com.group6.swp391.response.ObjectResponse;
import com.group6.swp391.response.ProductRespone;
import com.group6.swp391.service.DashboardService;
import com.group6.swp391.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/swp391/api/dashboards")
public class DashboardController {
    @Autowired OrderService orderService;
    @Autowired DashboardService dashboardService;

    private String formatNumberDouble(Double number) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        String formattedNumber = df.format(number);
        return formattedNumber;
    }

    private Double totalCurrentDate() {
        try {
            LocalDate currentDate = LocalDate.now();
            LocalDateTime startDate = currentDate.atStartOfDay();
            LocalDateTime endDate = startDate.plusDays(1).minusNanos(1);
            Double total = dashboardService.totalRevenueDate(startDate, endDate);
            if(total == null || total == 0.0) {
                return 0.0;
            }
            return total;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Double totalBegoreDate() {
        try {
            LocalDate currentDate = LocalDate.now();
            LocalDate prevDate = currentDate.minusDays(1);
            LocalDateTime starPrevDatetDate = prevDate.atStartOfDay();
            LocalDateTime endPevDateDate = starPrevDatetDate.plusDays(1).minusNanos(1);
            Double total = dashboardService.totalRevenueDate(starPrevDatetDate, endPevDateDate);
            if(total == null || total == 0.0) {
                return 0.0;
            }
            return total;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/compare_total_date")
    public ResponseEntity<ObjectResponse> campareBetweenPrevAndCurrentDate() {
        try {
            Double currentDate = totalCurrentDate();
            Double prevDate = totalBegoreDate();
            Double total = (currentDate - prevDate)/prevDate * 100;
            if (total == null || total == 0.0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "no data", 0.0));
            }
            String numberFormat = formatNumberDouble(total);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get value compare between two month", numberFormat));
        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "failed", e.getMessage()));
        }
    }

    // tong doanh thu trong ngay
    @GetMapping("/total_revenue_date")
    public ResponseEntity<ObjectResponse> totalRevenueToday() {
        try {
            LocalDate currentDate = LocalDate.now();
            LocalDateTime startDate = currentDate.atStartOfDay();
            LocalDateTime endDate = startDate.plusDays(1).minusNanos(1);
            Double total = dashboardService.totalRevenueDate(startDate, endDate);
            if(total == null || total == 0.0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "no data", 0.0));
            }

            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "total revenue in date", total));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "no data", e.getMessage()));
        }
    }

    //tong loi nhuan tu luc kinh doanh
    @GetMapping("/total_revenue_store")
    public ResponseEntity<ObjectResponse> totelRevenue() {
        try {
            double toteal = 0.0;
            List<Order> orders = dashboardService.totalRevenueStore();
            if(orders == null || orders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "get data failed", 0.0));
            }
            for (Order order : orders) {
                toteal += order.getPrice();
            }

            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Total Revenue Store", toteal));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "get data failed", e.getMessage()));
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "get data failed", e.getMessage()));
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "get data failed", e.getMessage()));
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "get data failed", e.getMessage()));
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "List Is empty", e.getMessage()));
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "List Is empty", e.getMessage()));
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "List Is empty", e.getMessage()));
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "List Is empty", e.getMessage()));
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "failed", e.getMessage()));
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
            String numberFormat = formatNumberDouble(totalCompare);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get value compare between two month", numberFormat));
        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "failed", e.getMessage()));
        }
    }

    //xem san pham moi duoc them vao
    @GetMapping("/recently-created")
    public ResponseEntity<ObjectResponse> newProductAndDiamond() {
        try {
            List<Object> ObjectProduct = new ArrayList<>();
            int month = LocalDate.now().getMonthValue();
            int year = LocalDate.now().getYear();
            List<Product> recentlyCreatedProducts = dashboardService.getNewProducts(month, year);
            List<Diamond> recentlyCreatedDiamonds = dashboardService.getNewDiamonds(month, year);
            if(recentlyCreatedDiamonds.isEmpty() && recentlyCreatedProducts.isEmpty()) {
                return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "There are no new product objects of the month", null));
            }
            for (Diamond diamond: recentlyCreatedDiamonds) {
                ProductRespone productRespone = new ProductRespone();
                productRespone.setProductId(diamond.getDiamondID());
                productRespone.setProductName(diamond.getDiamondName());
                productRespone.setPrice(diamond.getTotalPrice());
                productRespone.setThumnail(diamond.getImage());
                ObjectProduct.add(productRespone);
            }
            for (Product product: recentlyCreatedProducts) {
                ProductRespone productRespone = new ProductRespone();
                productRespone.setProductId(product.getProductID());
                productRespone.setProductName(product.getProductName());
                productRespone.setPrice(product.getTotalPrice());
                productRespone.setThumnail(product.getProductImages().get(0).getImageUrl());
                ObjectProduct.add(productRespone);
            }
            return  ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "New Product Objects of the Month", ObjectProduct));
        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "failed", e.getMessage()));
        }
    }
}
