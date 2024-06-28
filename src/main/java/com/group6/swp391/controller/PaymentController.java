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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
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
    public String pay(@RequestBody PaymentRequest paymentRequest, HttpSession session, HttpServletRequest request) {
        try {
            session.setAttribute("orderID", paymentRequest.getOrderId());
            if(paymentRequest.getPaymentMethod().equals("paypal")) {
                String href = request.getRequestURL().toString().replace(request.getServletPath(), "") + "/payment/paypal";
                return "redirect:" + href;

            } else if(paymentRequest.getPaymentMethod().equals("vnpay")) {
                String href = request.getRequestURL().toString().replace(request.getServletPath(), "") + "/payment/vnpay";
                return "redirect:" + href;
            }
        } catch(Exception e) {
            log.error(e.getMessage());
        }
        return "Checkout failed";
    }

    @PostMapping("/paypal")
    public String payByPayPal(HttpServletRequest request, HttpSession session) {
        String cancelUrl = request.getRequestURL().toString().replace(request.getServletPath(), "") + "/payment/paypal/cancel";
        String successUrl = request.getRequestURL().toString().replace(request.getServletPath(), "") + "/payment/paypal/success";
        try {
            Order order = orderService.getOrderByOrderID(Integer.parseInt((String) session.getAttribute("orderID")));
            if(order != null) {
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

    @PostMapping("/paypal/cancel")
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

    @PostMapping("/paypal/success")
    public ResponseEntity<PaymentResponse> paySuccess(HttpSession session, @RequestParam("paymentId") String paymentID, @RequestParam("PayerID") String payerID) {
        int orderID = Integer.parseInt((String) session.getAttribute("orderID"));
        Order order = orderService.getOrderByOrderID(orderID);
        try {
            Payment payment = payPalService.executePayment(paymentID, payerID);
            if(payment.getState().equals("approved")) {
                double paymentAmount = Double.parseDouble(payment.getTransactions().get(0).getAmount().getTotal());
                SimpleDateFormat spm = new SimpleDateFormat("yyyy-MM-dd");
                Date date = spm.parse(payment.getCreateTime());
                com.group6.swp391.model.Payment payment1 = new com.group6.swp391.model.Payment(paymentAmount, order, date, ((order.getPrice() / 25000) - paymentAmount) , order.getPrice());
                paymentService.save(payment1);
                order.setStatus("Chờ giao hàng");
                orderService.save(order);
                session.setAttribute("orderID", null);
                return ResponseEntity.status(HttpStatus.OK).body(new PaymentResponse("Success", "Payment successfully", payment, null));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentResponse("Failed", "Payment failed", null, null));
    }


    @GetMapping("/vnpay")
    public void vnpay(HttpServletRequest req, HttpSession session, HttpServletResponse response) throws IOException {
        Order order = orderService.getOrderByOrderID(Integer.parseInt((String) session.getAttribute("orderID")));
        long amount = (long) (order.getPrice()*100);
        long haveToPay = (amount / 25000);
        String s = vnPayService.getVNPay(haveToPay, req);
        response.sendRedirect(s);
    }

    @GetMapping("/vnpaysuccess")
    public ResponseEntity<PaymentResponse> vnpaysuccess(HttpServletRequest req, HttpSession session) {
        String maloi = req.getParameter("vnp_ResponseCode");
        if(maloi.equals("00")) {
            session.setAttribute("orderID", null);
            return ResponseEntity.status(HttpStatus.OK).body(new PaymentResponse("Success", "Payment successfully", null, null));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentResponse("Failed", "Payment failed", null, null));
    }
}
