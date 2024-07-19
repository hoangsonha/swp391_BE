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
import org.springframework.security.access.prepost.PreAuthorize;
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

    /**
     * Method format number 0.00
     * @return number with format 0.00
     */
    private String formatNumberDouble(Double number) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        String formattedNumber = df.format(number);
        return formattedNumber;
    }

    /**
     * Method calculate total currentDate
     * @return total in date
     */
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

    /**
     * Method calculate total date before
     * @return total in date before
     */
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

    /**
     * Method compare total between two date
     * @return value compare between two date with format 0.00
     */
    @PreAuthorize("hasRole('ADMIN')")
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
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get value compare between two date", numberFormat));
        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "failed", e.getMessage()));
        }
    }

    /**
     * Method total revenue date
     * @return total int date
     */
    @PreAuthorize("hasRole('ADMIN')")
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

    /**
     * Method total revenue in store
     * @return total
     */
    @PreAuthorize("hasRole('ADMIN')")
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

    /**
     * Method tinh tong doanh thu tung thang
     * api nay su dung cho bieu do the hien qua tung thang
     * @return total tung thang
     */
    @PreAuthorize("hasRole('ADMIN')")
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

    /**
     * Method tinh tong doanh thu tung thang cua kim cuong
     * api nay su dung cho bieu do the hien qua tung thang
     * @return total tung thang cua kim cuong
     */
    @PreAuthorize("hasRole('ADMIN')")
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

    /**
     * Method tong doanh thu cua customize cua tung thang theo bieu do
     * api nay su dung cho bieu do the hien qua tung thang
     * @return total tung thang cua product customize
     */
    @PreAuthorize("hasRole('ADMIN')")
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

    /**
     * Method tra ve tat cua cac order voi status dang doi xac nhan
     * @return list<newOrder>
     */
//    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping("/listneworder")
//    public ResponseEntity<ObjectResponse> listNewOrder() {
//        try {
//            int monthCurrent = LocalDate.now().getMonthValue();
//            List<Order> listNewOrder = orderService.getNewestOrder("Chờ xác nhận");
//            if(listNewOrder == null || listNewOrder.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "List Is empty", null));
//            }
//            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "List new Order", listNewOrder));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "List Is empty", e.getMessage()));
//        }
//    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listneworder")
    public ResponseEntity<ObjectResponse> getNewOrders() {
        try {
            LocalDate currentDate = LocalDate.now();
            LocalDateTime startDate = currentDate.atStartOfDay();
            LocalDateTime endDate = startDate.plusDays(1).minusNanos(1);
            List<Order> listNewOrders = dashboardService.getOrderDate("Chờ xác nhận", startDate, endDate);
            if (listNewOrders == null || listNewOrders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ObjectResponse("Success", "List is empty", null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ObjectResponse("Success", "Get Orders Successfully", listNewOrders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ObjectResponse("Failed", "Get Orders Failed", null));
        }
    }

    /**
     * Method tra ve tat cua cac order voi status bi huy
     * @return list order bi huy
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listorderfailed")
    public ResponseEntity<ObjectResponse> listOrderFailed() {
        try {
            LocalDate currentDate = LocalDate.now();
            LocalDateTime startDate = currentDate.atStartOfDay();
            LocalDateTime endDate = startDate.plusDays(1).minusNanos(1);
            List<Order> listFailedOrders = dashboardService.getOrderDate("Đã hủy", startDate, endDate);
            if(listFailedOrders == null || listFailedOrders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ObjectResponse("Success", "List is empty", null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ObjectResponse("Success", "Get Orders Successfully", listFailedOrders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ObjectResponse("Failed", "Get Orders Failed", e.getMessage()));
        }
    }

    /**
     * Method tra ve tat cua cac order voi status hoan trar
     * @return list order hoan trar
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listorderreturn")
    public ResponseEntity<ObjectResponse> listOrderReturn() {
        try {
            LocalDate currentDate = LocalDate.now();
            LocalDateTime startDate = currentDate.atStartOfDay();
            LocalDateTime endDate = startDate.plusDays(1).minusNanos(1);
            List<Order> listFailedOrders = dashboardService.getOrderDate("Đã hoàn tiền", startDate, endDate);
            if(listFailedOrders == null || listFailedOrders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ObjectResponse("Success", "List is empty", null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ObjectResponse("Success", "Get Orders Successfully", listFailedOrders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ObjectResponse("Failed", "Get Orders Failed", e.getMessage()));
        }
    }

    /**
     * Method tra ve tat cua cac order voi status da giao
     * @return list order da giao
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listordersuccessfully")
    public ResponseEntity<ObjectResponse> listOrderSuccessfully() {
        try {
            LocalDate currentDate = LocalDate.now();
            LocalDateTime startDate = currentDate.atStartOfDay();
            LocalDateTime endDate = startDate.plusDays(1).minusNanos(1);
            List<Order> listSuccessfulOrders = dashboardService.getOrderDate("Đã giao", startDate, endDate);
            if(listSuccessfulOrders == null || listSuccessfulOrders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ObjectResponse("Success", "List is empty", null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ObjectResponse("Success", "Get Orders Successfully", listSuccessfulOrders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ObjectResponse("Failed", "Get Orders Failed", e.getMessage()));
        }
    }

    /**
     * Method so luong user moi dang ki
     * duoc tong ket trong mot thang
     * @return so luong user moi
     */
    @PreAuthorize("hasRole('ADMIN')")
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

    /**
     * Method so sanh tong doanh thu giua hai thang
     * so sanh thang truoc va thang hien tai
     * @return gia tri chenh lech giua thang truoc va sau format 0.00
     */
    @PreAuthorize("hasRole('ADMIN')")
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

    /**
     * Method san pham moi duoc them vao
     * tong ket trong mot thang
     * @return list new product, new diamond
     */
    @PreAuthorize("hasRole('ADMIN')")
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
