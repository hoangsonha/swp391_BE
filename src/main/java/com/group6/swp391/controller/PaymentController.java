package com.group6.swp391.controller;

import com.group6.swp391.model.Order;
import com.group6.swp391.paypal.EnumPayPalPaymentMethod;
import com.group6.swp391.paypal.EnumPaypalPaymentIntent;
import com.group6.swp391.request.PaymentRequest;
import com.group6.swp391.response.PaymentResponse;
import com.group6.swp391.service.*;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/payment")
@Slf4j
public class PaymentController {

    @Autowired private PayPalService payPalService;
    @Autowired private OrderService orderService;
    @Autowired private PaymentService paymentService;
    @Autowired private VNPayService vnPayService;

    @PostMapping("/checkout")
    public String pay(@RequestBody PaymentRequest paymentRequest, HttpServletRequest request) {
        try {
            if(paymentRequest.getPaymentMethod().equals("paypal")) {
                String href = request.getRequestURL().toString().replace(request.getServletPath(), "") + "/payment/paypal?orderID=" + paymentRequest.getOrderId();
                return "redirect:" + href;

            } else if(paymentRequest.getPaymentMethod().equals("vnpay")) {
                String href = request.getRequestURL().toString().replace(request.getServletPath(), "") + "/payment/vnpay?orderID=" + paymentRequest.getOrderId();
                return "redirect:" + href;
            }
        } catch(Exception e) {
            log.error(e.getMessage());
        }
        return "Checkout failed";
    }

    @PostMapping("/paypal")
    public String payByPayPal(@Param("orderID") String orderID ,HttpServletRequest request) {
        String cancelUrl = request.getRequestURL().toString().replace(request.getServletPath(), "") + "/payment/paypal/cancel?orderID=" + orderID;
        String successUrl = request.getRequestURL().toString().replace(request.getServletPath(), "") + "/payment/paypal/success?orderID=" + orderID;
        try {
            Order order = orderService.getOrderByOrderID(Integer.parseInt(orderID));
            if(order != null && order.getStatus().toLowerCase().equals("chờ thanh toán")) {
                Payment payment = payPalService.createPayment(order, EnumPayPalPaymentMethod.paypal,
                        EnumPaypalPaymentIntent.sale, cancelUrl, successUrl);
                for(Links links : payment.getLinks()) {
                    if(links.getRel().equals("approval_url")) {
                        return "redirect:" + links.getHref();
                    }
                }
            }
        } catch(Exception e) {
            log.error(e.getMessage());
        }
        return "Payment failed";
    }

    @GetMapping("/paypal/cancel")
    public ResponseEntity<PaymentResponse> payByPayPalCancel() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentResponse("Failed", "Payment page failed", null, null));
    }

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
//
//        }
//        return "Payment failed";
//    }

    @GetMapping("/paypal/success")
    public ResponseEntity<PaymentResponse> paySuccess(@Param("orderID") String orderID, @RequestParam("paymentId") String paymentID, @RequestParam("PayerID") String payerID) {
        Order order = orderService.getOrderByOrderID(Integer.parseInt(orderID));
        try {
            if(order.getStatus().toLowerCase().equals("chờ thanh toán")) {
                Payment payment = payPalService.executePayment(paymentID, payerID);
                if(payment.getState().equals("approved")) {
                    double paymentAmount = Double.parseDouble(payment.getTransactions().get(0).getAmount().getTotal());
                    SimpleDateFormat spm = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = spm.parse(payment.getCreateTime());
                    double remain = (order.getPrice() - (paymentAmount * 25455));
                    com.group6.swp391.model.Payment payment1 = new com.group6.swp391.model.Payment((paymentAmount * 25455), order, date, remain, order.getPrice());
                    paymentService.save(payment1);
                    order.setStatus("Chờ giao hàng");
                    orderService.save(order);
                    return ResponseEntity.status(HttpStatus.OK).body(new PaymentResponse("Success", "Payment successfully", null, null));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentResponse("Failed", "Payment failed", null, null));
    }


    @GetMapping("/vnpay")
    public void vnpay(@Param("orderID") String orderID, HttpServletRequest req, HttpServletResponse response) throws IOException {
        Order order = orderService.getOrderByOrderID(Integer.parseInt(orderID));
        long amount = (long) (order.getPrice()*100);
        long haveToPay = (amount / 25000);
        if(order.getStatus().toLowerCase().equals("chờ thanh toán")) {
            String s = vnPayService.getVNPay(haveToPay, req, orderID);
            response.sendRedirect(s);
        }
    }

    @GetMapping("/vnpaysuccess")
    public ResponseEntity<PaymentResponse> vnpaysuccess(HttpServletRequest req, HttpSession session) throws ParseException {
        String maloi = req.getParameter("vnp_ResponseCode"); // lay qua url
        String orderID = req.getParameter("vnp_TxnRef");
        String dateAt = req.getParameter("vnp_PayDate");
        String nam = dateAt.substring(0, 4);
        String thang = dateAt.substring(4, 6);
        String ngay = dateAt.substring(6, 8);
        String gio = dateAt.substring(8, 10);
        String phut = dateAt.substring(10, 12);
        String giay = dateAt.substring(12, 14);
        String dateParse = nam + "-" + thang + "-" + ngay + " " + gio + ":" + phut + ":" + giay;
        String amount = req.getParameter("vnp_Amount").substring(0, 5);
        Order order = orderService.getOrderByOrderID(Integer.parseInt(orderID));
        if(order.getStatus().toLowerCase().equals("chờ thanh toán")) {
            if(maloi.equals("00")) {
                SimpleDateFormat spm = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date date = spm.parse(dateParse);
                double remain = (order.getPrice() - (Double.parseDouble(amount) * 25000));
                com.group6.swp391.model.Payment payment1 = new com.group6.swp391.model.Payment((Double.parseDouble(amount) * 25000), order, date, remain, order.getPrice());
                paymentService.save(payment1);
                order.setStatus("Chờ giao hàng");
                orderService.save(order);
                return ResponseEntity.status(HttpStatus.OK).body(new PaymentResponse("Success", "Payment successfully", null, null));
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentResponse("Failed", "Payment failed", null, null));
    }
}
