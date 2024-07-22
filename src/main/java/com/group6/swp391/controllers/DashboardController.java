package com.group6.swp391.controllers;

import com.group6.swp391.enums.EnumOrderStatus;
import com.group6.swp391.pojos.Diamond;
import com.group6.swp391.pojos.Order;
import com.group6.swp391.pojos.Product;
import com.group6.swp391.responses.ObjectResponse;
import com.group6.swp391.responses.ProductRespone;
import com.group6.swp391.services.DashboardService;
import com.group6.swp391.services.OrderService;
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
            if(prevDate == 0.0 || prevDate == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Doanh thu hôm trước = 0", null));
            }
            Double total = (currentDate - prevDate)/prevDate * 100;
            if (total == null || total == 0.0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Doanh thu cả 2 ngày đêu bằng 0", null));
            }
            String numberFormat = formatNumberDouble(total);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Giá trị so sánh giữa ngày " + currentDate + " và " + prevDate, numberFormat));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
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
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Tổng doanh thu hôm nay bằng 0", null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Tổng doanh thu hôm nay", total));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
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
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Tổng doanh thu của cửa hàng rỗng", null));
            }
            for (Order order : orders) {
                toteal += order.getPrice();
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Tổng doanh thu của cửa hàng", toteal));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
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
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Tổng doanh thu trong một năm", totalRevenue));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/total_year_revenue")
    public ResponseEntity<ObjectResponse> getTotalYearRevenue() {
        try {
            int currentYear = LocalDate.now().getYear();
            Double totalYearCuurrent = 0.0;
            for(int i = 1; i < 13; i++) {
                Double moonth = dashboardService.getTotalRevenueInMonth(i,currentYear);
                totalYearCuurrent += moonth;
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Tổng doanh thu trong một năm", totalYearCuurrent));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
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
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Tổng doanh thu kim cương", totalRevenue));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
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
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Tổng doanh thu sản phẩm tùy chọn", totalRevenue));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
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
            List<Order> listNewOrders = dashboardService.getOrderDate((EnumOrderStatus.Chờ_xác_nhận).name().replaceAll("_", " "), startDate, endDate);
            if (listNewOrders == null || listNewOrders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ObjectResponse("Failed", "Danh sách đơn hàng mới rỗng", null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ObjectResponse("Success", "Danh sách đơn hàng mới", listNewOrders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Success", "Message: " + e.getMessage(), null));

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
            List<Order> listFailedOrders = dashboardService.getOrderDate(EnumOrderStatus.Đã_hủy.name().replaceAll("_"," "), startDate, endDate);
            if(listFailedOrders == null || listFailedOrders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ObjectResponse("Failed", "Danh sách đơn hàng đã hủy rỗng", null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ObjectResponse("Success", "Danh sách đơn hàng đã hủy", listFailedOrders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
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
            List<Order> listFailedOrders = dashboardService.getOrderDate(EnumOrderStatus.Đã_hoàn_tiền.name().replaceAll("_"," "), startDate, endDate);
            if(listFailedOrders == null || listFailedOrders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ObjectResponse("Failed", "Danh sách đơn hàng Đã hoàn tiền rỗng", null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ObjectResponse("Success", "Danh sách đơn hàng Đã hoàn tiền", listFailedOrders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
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
            List<Order> listSuccessfulOrders = dashboardService.getOrderDate(EnumOrderStatus.Đã_giao.name().replaceAll("_"," "), startDate, endDate);
            if(listSuccessfulOrders == null || listSuccessfulOrders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ObjectResponse("Failed", "Danh sách đơn hàng ã giao rỗng", null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ObjectResponse("Success", "Danh sách đơn hàng ã giao", listSuccessfulOrders));
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
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Danh sách người dùng mới trong tháng " + monthCurrent + " rỗng", null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Danh sách người dùng mới trong tháng " + monthCurrent, newUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
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
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Doanh thu giữa tháng " + monthPrev + " Và " + monthCurrent, numberFormat));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
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
                return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Không có sản phẩm mới trong tháng " + month, null));
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
            return  ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Sản phẩm mới trong tháng " + month, ObjectProduct));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
        }
    }
}
