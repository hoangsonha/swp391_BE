package com.group6.swp391.controller;

import com.group6.swp391.enums.*;
import com.group6.swp391.model.*;
import com.group6.swp391.request.CancelPaymentRequest;
import com.group6.swp391.request.PaymentRequest;
import com.group6.swp391.response.PaymentResponse;
import com.group6.swp391.security.CustomUserDetail;
import com.group6.swp391.service.*;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/payment")
@Slf4j
public class PaymentController {

    @Autowired private PayPalService payPalService;
    @Autowired private UserService userService;
    @Autowired private OrderService orderService;
    @Autowired private PaymentService paymentService;
    @Autowired private VNPayService vnPayService;
    @Autowired private DiamondService diamondService;
    @Autowired private RoleService roleService;
    @Autowired private ProductService productService;
    @Autowired private CrawledDataProperties dola;

    @Value("${frontend.url}")
    private String urlRedirect;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/checkout")
    public ResponseEntity<PaymentResponse> pay(@RequestBody PaymentRequest paymentRequest, HttpServletRequest request) {
        try {
            if(paymentRequest.getPaymentMethod().equals("paypal") && EnumPaymentMethod.checkExistPaymentMethod(paymentRequest.getPaymentMethod())) {
                String cancelUrl = request.getRequestURL().toString().replace(request.getServletPath(), "") + "/payment/paypal/cancel?orderID=" + paymentRequest.getOrderID();
                String successUrl = request.getRequestURL().toString().replace(request.getServletPath(), "") + "/payment/paypal/success?orderID=" + paymentRequest.getOrderID();
                try {
                    Order order = orderService.getOrderByOrderID(Integer.parseInt(paymentRequest.getOrderID()));
                        String orderStatus = EnumOrderStatus.Chờ_thanh_toán.name();
                        if(order != null && order.getStatus().equals(orderStatus.replaceAll("_", " "))) {
                            Payment payment = payPalService.createPayment(order, EnumPayPalPaymentMethod.paypal,
                                    EnumPaypalPaymentIntent.sale, cancelUrl, successUrl);
                            for(Links links : payment.getLinks()) {
                                if(links.getRel().equals("approval_url")) {
                                    return ResponseEntity.status(HttpStatus.OK).body(new PaymentResponse("Success", "Redirect payment page successfully", null, links.getHref()));
                                }
                            }
                        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PaymentResponse("Failed", "Redirect payment page failed", null, null));

                } catch(Exception e) {
                    log.error("Error at paypal payment {}", e.getMessage());
                    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new PaymentResponse("Failed", "Redirect payment page failed", null, null));
                }
            } else if(paymentRequest.getPaymentMethod().equals("vnpay") && EnumPaymentMethod.checkExistPaymentMethod(paymentRequest.getPaymentMethod())) {
                try {
                    Order order = orderService.getOrderByOrderID(Integer.parseInt(paymentRequest.getOrderID()));
                    String orderStatus = EnumOrderStatus.Chờ_thanh_toán.name();
                    if(order.getStatus().equals(orderStatus.replaceAll("_", " ")) && order != null) {
                        long amount = (long) (order.getPrice()*100);
                        double haveToPay = amount;
                        String link = vnPayService.getVNPay(haveToPay, request, paymentRequest.getOrderID());
                        return ResponseEntity.status(HttpStatus.OK).body(new PaymentResponse("Success", "Redirect payment page successfully", null, link));
                    }
                } catch (Exception e) {
                    log.error("Error at VNPay payment: {}", e.toString());
                    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new PaymentResponse("Failed", "Redirect payment page failed", null, null));
                }
            } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentResponse("Failed", "Redirect payment page failed", null, null));
        } catch(Exception e) {
            log.error("Error at checkout {}", e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentResponse("Failed", "Redirect payment page failed", null, null));
    }

    @GetMapping("/paypal/cancel")
    public void payByPayPalCancel(HttpServletResponse response) throws IOException {
        response.sendRedirect(urlRedirect);
    }

    @GetMapping("/paypal/success")
    public ResponseEntity<PaymentResponse> paySuccess(@Param("orderID") String orderID, @RequestParam("paymentId") String paymentID, @RequestParam("PayerID") String payerID, HttpServletResponse response) throws IOException {
        try {
            Order order = orderService.getOrderByOrderID(Integer.parseInt(orderID));
            String orderStatus = EnumOrderStatus.Chờ_thanh_toán.name();
            if(order.getStatus().equals(orderStatus.replaceAll("_", " ")) && order != null) {
                Payment payment = payPalService.executePayment(paymentID, payerID);
                if(payment.getState().equals("approved")) {
                    SimpleDateFormat spm = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = spm.parse(payment.getCreateTime());
                    String transactionID = payment.getTransactions().get(0).getRelatedResources().get(0).getSale().getId();
//                  double paymentAmount = Double.parseDouble(payment.getTransactions().get(0).getAmount().getTotal());
//                  double payAmount = Math.floor(paymentAmount * 25500);
                    double paymentAmount = order.getPrice();
                    double remainAmount = (order.getPrice() - paymentAmount);
                    double totalAmount = order.getPrice();
                    String paymentStatusSuccess = EnumPaymentStatus.Thanh_toán_thành_công.name();
                    com.group6.swp391.model.Payment payment_order = new com.group6.swp391.model.Payment(paymentAmount, order, date, remainAmount, totalAmount, transactionID, "Paypal", paymentStatusSuccess.replaceAll("_", " "));
                    paymentService.save(payment_order);
                    String orderStatusSuccess = EnumOrderStatus.Chờ_giao_hàng.name();
                    order.setStatus(orderStatusSuccess.replaceAll("_", " "));
                    orderService.save(order);
                    boolean check = userService.sendInvoice(order);
                    response.sendRedirect(urlRedirect);
                    return ResponseEntity.status(HttpStatus.OK).body(new PaymentResponse("Success", "Payment paypal successfully", null, null));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        response.sendRedirect(urlRedirect);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new PaymentResponse("Failed", "Payment paypal failed", null, null));
    }

    @GetMapping("/vnpaysuccess")
    public ResponseEntity<PaymentResponse> vnpaysuccess(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        String responseCode = request.getParameter("vnp_ResponseCode"); // lay qua url
        String orderID = request.getParameter("vnp_TxnRef");
        String dateAt = request.getParameter("vnp_PayDate");
//      String amount = request.getParameter("vnp_Amount");
        String nam = dateAt.substring(0, 4);
        String thang = dateAt.substring(4, 6);
        String ngay = dateAt.substring(6, 8);
        String gio = dateAt.substring(8, 10);
        String phut = dateAt.substring(10, 12);
        String giay = dateAt.substring(12, 14);
        String dateParse = nam + "-" + thang + "-" + ngay + " " + gio + ":" + phut + ":" + giay;
        Order order = orderService.getOrderByOrderID(Integer.parseInt(orderID));
        String orderStatus = EnumOrderStatus.Chờ_thanh_toán.name();
        if(order.getStatus().equals(orderStatus.replaceAll("_", " ")) && order != null) {
            if(responseCode.equals("00")) {
                SimpleDateFormat spm = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date date = spm.parse(dateParse);
                double paymentAmount = order.getPrice();
                double remainAmount = (order.getPrice() - paymentAmount);
                double totalAmount = order.getPrice();
                String paymentStatusSuccess = EnumPaymentStatus.Thanh_toán_thành_công.name();
                com.group6.swp391.model.Payment payment1 = new com.group6.swp391.model.Payment(paymentAmount, order, date, remainAmount, totalAmount, null, "VNPay", paymentStatusSuccess.replaceAll("_", " "));
                paymentService.save(payment1);
                String orderStatusSuccess = EnumOrderStatus.Chờ_giao_hàng.name();
                order.setStatus(orderStatusSuccess.replaceAll("_", " "));
                orderService.save(order);
                boolean check = userService.sendInvoice(order);
                response.sendRedirect(urlRedirect);
                return ResponseEntity.status(HttpStatus.OK).body(new PaymentResponse("Success", "Payment VNPay successfully", null, null));
            }
        }
        response.sendRedirect(urlRedirect);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new PaymentResponse("Failed", "Payment paypal failed", null, null));
    }

    @PreAuthorize("hasRole('USER') or hasRole('DELIVERY')")
    @PostMapping("/refund")
    public ResponseEntity<PaymentResponse> refund(@RequestBody CancelPaymentRequest cancelPaymentRequest, HttpServletRequest request) {
        try {
            Order order = orderService.getOrderByOrderID(Integer.parseInt(cancelPaymentRequest.getOrderID()));
            CustomUserDetail customUserDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(customUserDetail.getUserID() == order.getUser().getUserID()){
                String orderStatus = EnumOrderStatus.Chờ_giao_hàng.name();
                String orderStatus2 = EnumOrderStatus.Đã_giao.name();
                if (order != null && order.getStatus().equals(orderStatus.replaceAll("_", " ")) || order.getStatus().equals(orderStatus2.replaceAll("_", " "))) {
                    com.group6.swp391.model.Payment payment = paymentService.findByOrder(order);
                    double amount = 0;
                    String paymentStatus = EnumPaymentStatus.Thanh_toán_thành_công.name();
                    if(payment.getMethodPayment().toLowerCase().equals(EnumPaymentMethod.paypal.name()) && payment.getStatus().equals(paymentStatus.replaceAll("_", " "))) {
                        amount += payment.getPaymentAmount();
                        String saleID = payment.getTransactionId();
                        if(saleID != null) {
                            double amount_at = priceToUSD(amount);
                            boolean check = payPalService.cancelPayment(saleID, amount_at, "USD");
                            if(check) {
                                String orderStatusSuccess = EnumOrderStatus.Đã_hoàn_tiền.name();
                                order.setStatus(orderStatusSuccess.replaceAll("_", " "));
                                orderService.save(order);
                                String paymentStatusSuccess = EnumPaymentStatus.Đã_hoàn_tiền.name();
                                payment.setStatus(paymentStatusSuccess.replaceAll("_", " "));
                                paymentService.save(payment);
                                for(OrderDetail orderDetail : order.getOrderDetails()) {
                                    if(orderDetail.getDiamond() != null) {
                                        Diamond diamond = diamondService.getDiamondByDiamondID(orderDetail.getDiamond().getDiamondID());
                                        diamond.setStatus(true);
                                        diamondService.saveDiamond(diamond);
                                    }
                                    if(orderDetail.getProductCustomize() != null) {
                                        Diamond diamond = diamondService.getDiamondByDiamondID(orderDetail.getProductCustomize().getDiamond().getDiamondID());
                                        diamond.setStatus(true);

                                        Product product = productService.getProductById(orderDetail.getProductCustomize().getProduct().getProductID());
                                        product.setQuantity(product.getQuantity() + 1);
                                        productService.createProduct(product);
                                        diamondService.saveDiamond(diamond);
                                    }
                                }
                                return ResponseEntity.status(HttpStatus.OK).body(new PaymentResponse("Success", "Refund successfully", null, null));
                            }
                        }
                    } else if(payment.getMethodPayment().toLowerCase().equals(EnumPaymentMethod.vnpay.name()) && payment.getStatus().equals(paymentStatus.replaceAll("_", " "))) {
                        amount += payment.getPaymentAmount();
                        double amount_at = priceToUSD(amount);
                        String check = vnPayService.refundVNPay(amount_at, request, cancelPaymentRequest.getOrderID());
//                    if(check) {
//                        String orderStatusSuccess = EnumOrderStatus.Đã_hoàn_tiền.name();
//                        order.setStatus(orderStatusSuccess.replaceAll("_", " "));
//                        orderService.save(order);
//                        String paymentStatusSuccess = EnumPaymentStatus.Đã_hoàn_tiền.name();
//                        payment.setStatus(paymentStatusSuccess.replaceAll("_", " "));
//                        paymentService.save(payment);
//                        return ResponseEntity.status(HttpStatus.OK).body(new PaymentResponse("Success", "Refund successfully", null, null));
//                    }
                    } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentResponse("Failed", "Refund failed", null, null));
                } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentResponse("Failed", "Refund failed", null, null));
            } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new PaymentResponse("Failed", "Refund because not authorization failed", null, null));
        } catch(Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentResponse("Failed", "Refund failed", null, null));
    }

    private double priceToUSD(double price) {
        String dola_price = dola.getDola().replace(".", "");
        double value = (price / Double.parseDouble(dola_price));
        String s = String.format("%.2f",value);
        return Math.floor(Double.parseDouble(s));
    }



//    @PostMapping("/paypal/refund")
//    public ResponseEntity<PaymentResponse> refundWithPaypal(@RequestBody CancelPaymentRequest cancelPaymentRequest) throws PayPalRESTException {
//        try {
//            Order order = orderService.getOrderByOrderID(Integer.parseInt(cancelPaymentRequest.getOrderID()));
//            String orderStatus = EnumOrderStatus.Chờ_giao_hàng.name();
//            if (order != null && order.getStatus().equals(orderStatus.replaceAll("_", " "))) {
//                com.group6.swp391.model.Payment payment = paymentService.findByOrder(order);
//                double amount = 0;
//                String paymentStatus = EnumPaymentStatus.Thanh_toán_thành_công.name();
//                if(payment.getMethodPayment().toLowerCase().equals(EnumPaymentMethod.paypal.name()) && payment.getStatus().equals(paymentStatus.replaceAll("_", " "))) {
//                    amount += payment.getPaymentAmount();
//                }
//                String saleID = payment.getTransactionId();
//                if(saleID != null) {
//                    double value = (amount / 25500);
//                    String result = String.format("%.2f",value);
//                    double amount_at = Math.floor(Double.parseDouble(result));
//                    boolean check = payPalService.cancelPayment(saleID, amount_at, "USD");
//                    if(check) {
//                        String orderStatusSuccess = EnumOrderStatus.Đã_hoàn_tiền.name();
//                        order.setStatus(orderStatusSuccess.replaceAll("_", " "));
//                        orderService.save(order);
//                        String paymentStatusSuccess = EnumPaymentStatus.Đã_hoàn_tiền.name();
//                        payment.setStatus(paymentStatusSuccess.replaceAll("_", " "));
//                        paymentService.save(payment);
//                        for(OrderDetail orderDetail : order.getOrderDetails()) {
//                            if(orderDetail.getDiamond() != null) {
//                                Diamond diamond = diamondService.getDiamondByDiamondID(orderDetail.getDiamond().getDiamondID());
//                                diamond.setStatus(true);
//                                diamondService.saveDiamond(diamond);
//                            }
//                            if(orderDetail.getProductCustomize() != null) {
//                                Diamond diamond = diamondService.getDiamondByDiamondID(orderDetail.getProductCustomize().getDiamond().getDiamondID());
//                                diamond.setStatus(true);
//                                diamondService.saveDiamond(diamond);
//                            }
//                        }
//                        return ResponseEntity.status(HttpStatus.OK).body(new PaymentResponse("Success", "Refund successfully", null, null));
//                    }
//                }
//            }
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentResponse("Failed", "Refund failed", null, null));
//        } catch (Exception e) {
//            log.error("ERROR: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentResponse("Failed", "Refund failed", null, null));
//        }
//    }
//
//
//    @PostMapping("/vnpay/refund")
//    public ResponseEntity<PaymentResponse> refundWithVNPay(@RequestBody CancelPaymentRequest cancelPaymentRequest, HttpServletRequest request) throws PayPalRESTException {
//        try {
//            Order order = orderService.getOrderByOrderID(Integer.parseInt(cancelPaymentRequest.getOrderID()));
//            String orderStatus = EnumOrderStatus.Chờ_giao_hàng.name();
//            if (order != null && order.getStatus().equals(orderStatus.replaceAll("_", " "))) {
//                com.group6.swp391.model.Payment payment = paymentService.findByOrder(order);
//                double amount = 0;
//                String paymentStatus = EnumPaymentStatus.Thanh_toán_thành_công.name();
//                if(payment.getMethodPayment().toLowerCase().equals(EnumPaymentMethod.vnpay.name()) && payment.getStatus().equals(paymentStatus.replaceAll("_", " "))) {
//                    amount += payment.getPaymentAmount();
//                }
//                    double value = (amount / 25500);
//                    String result = String.format("%.2f",value);
//                    double amount_at = Math.floor(Double.parseDouble(result));
//                    String check = vnPayService.refundVNPay(amount_at, request, cancelPaymentRequest.getOrderID());
////                    if(check) {
////                        String orderStatusSuccess = EnumOrderStatus.Đã_hoàn_tiền.name();
////                        order.setStatus(orderStatusSuccess.replaceAll("_", " "));
////                        orderService.save(order);
////                        String paymentStatusSuccess = EnumPaymentStatus.Đã_hoàn_tiền.name();
////                        payment.setStatus(paymentStatusSuccess.replaceAll("_", " "));
////                        paymentService.save(payment);
////                        return ResponseEntity.status(HttpStatus.OK).body(new PaymentResponse("Success", "Refund successfully", null, null));
////                    }
//
//            }
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentResponse("Failed", "Refund failed", null, null));
//        } catch (Exception e) {
//            log.error("ERROR: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentResponse("Failed", "Refund failed", null, null));
//        }
//    }






    //    @PostMapping("/paypal")
//    public String payByPayPal(@Param("orderID") String orderID ,HttpServletRequest request) {
//        String cancelUrl = request.getRequestURL().toString().replace(request.getServletPath(), "") + "/payment/paypal/cancel?orderID=" + orderID;
//        String successUrl = request.getRequestURL().toString().replace(request.getServletPath(), "") + "/payment/paypal/success?orderID=" + orderID;
//        try {
//            Order order = orderService.getOrderByOrderID(Integer.parseInt(orderID));
//            if(order != null && order.getStatus().toLowerCase().equals("chờ thanh toán")) {
//                Payment payment = payPalService.createPayment(order, EnumPayPalPaymentMethod.paypal,
//                        EnumPaypalPaymentIntent.sale, cancelUrl, successUrl);
//                for(Links links : payment.getLinks()) {
//                    if(links.getRel().equals("approval_url")) {
//                        return "redirect:" + links.getHref();
//                    }
//                }
//            }
//        } catch(Exception e) {
//            log.error(e.getMessage());
//        }
//        return "Payment failed";
//    }

    //    @GetMapping("/vnpay")
//    public void vnpay(@Param("orderID") String orderID, HttpServletRequest req, HttpServletResponse response) throws IOException {
//        try {
//            Order order = orderService.getOrderByOrderID(Integer.parseInt(orderID));
//            if(order.getStatus().toLowerCase().equals("chờ thanh toán") && order != null) {
//                long amount = (long) (order.getPrice()*100);
//                long haveToPay = (amount / 25000);
//                String s = vnPayService.getVNPay(haveToPay, req, orderID);
//                response.sendRedirect(s);
//            }
//        } catch (Exception e) {
//            log.error("Error at VNPay payment: {}", e.toString());
//        }
//    }

    //    @GetMapping("/paypal/information")
//    public String payByPayPalInformation(HttpSession session, Model model, @RequestParam("paymentId") String paymentID, @RequestParam("PayerID") String payerID, HttpServletRequest request) {
//        try {
//            Payment payment = (Payment) session.getAttribute("payment");
//            model.addAttribute("paymentId", paymentID);
//            model.addAttribute("PayerId", payerID);
//            String href = request.getRequestURL().toString().replace(request.getServletPath(), "") + "/payment/paypal/success";
//           return "redirect:" + href;
//        } catch(Exception e) {
//            log.error("ERROR at payment : {}", e.toString());
//        }
//        return "Payment failed";
//    }



}
