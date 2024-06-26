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
import org.springframework.ui.Model;
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
    @Autowired private UserService userService;
    @Autowired private VNPayService vnPayService;

    @PostMapping("/checkout")
    public ResponseEntity<PaymentResponse> pay(@RequestBody PaymentRequest paymentRequest, HttpSession session, HttpServletResponse response) {
        try {
            session.setAttribute("orderID", paymentRequest.getOrderId());
            if(paymentRequest.equals("paypal")) {
                response.sendRedirect("/paypal");
            } else if(paymentRequest.equals("vnpay")) {
                response.sendRedirect("/vnpay");
            }
        } catch(Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentResponse("Failed", "Redirect payment page failed", null, null));
    }

    @PostMapping("/paypal")
    public String payByPayPal(HttpServletRequest request, HttpSession session) {
        String cancelUrl = request.getRequestURL().toString().replace(request.getServletPath(), "") + "/paypal/cancel";
        String successUrl = request.getRequestURL().toString().replace(request.getServletPath(), "") + "/paypal/information";
        try {
            Order order = orderService.getOrderByOrderID(Integer.parseInt((String) session.getAttribute("orderID")));
            session.setAttribute("orderID", null);
            if(order != null) {
                Payment payment = payPalService.createPayment(order, EnumPayPalPaymentMethod.paypal,
                        EnumPaypalPaymentIntent.sale, cancelUrl, successUrl);
                session.setAttribute("payment", payment);
                for(Links links : payment.getLinks()) {
                    if(links.getRel().equals("approval_url")) {
                        return "redirect:" + links.getHref();
                    }
                }
            }
        } catch(Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @PostMapping("/paypal/cancel")
    public ResponseEntity<PaymentResponse> payByPayPalCancel() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentResponse("Failed", "Payment page failed", null, null));
    }

    @PostMapping("/paypal/information")
    public ResponseEntity<PaymentResponse> payByPayPalInformation(HttpSession session, Model model, @RequestParam("paymentId") String paymentID, @RequestParam("PayerID") String payerID, HttpServletResponse response) {
        try {
            Payment payment = (Payment) session.getAttribute("payment");
            model.addAttribute("paymentId", paymentID);
            model.addAttribute("PayerId", payerID);
            response.sendRedirect("/paypal/success");
            return ResponseEntity.status(HttpStatus.OK).body(new PaymentResponse("Success", "Payment page information successfully", payment, "/pay/success"));
        } catch(Exception e) {
            log.error("ERROR at payment : {}", e.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentResponse("Failed", "Payment information failed", null, null));
        }
    }

    @PostMapping("/paypal/success")
    public ResponseEntity<PaymentResponse> paySuccess(HttpSession session, @RequestParam("paymentId") String paymentID, @RequestParam("PayerID") String payerID) {
        Payment payment_order = (Payment) session.getAttribute("payment");
        int orderID = Integer.parseInt(payment_order.getId());
        Order order = orderService.getOrderByOrderID(orderID);

        try {
            Payment payment = payPalService.executePayment(paymentID, payerID);
            if(payment.getState().equals("approved")) {
                double paymentAmount = Double.parseDouble(payment.getTransactions().get(0).getAmount().getTotal());
                SimpleDateFormat spm = new SimpleDateFormat("yyyy-MM-dd");
                Date date = spm.parse(payment.getCreateTime());
                com.group6.swp391.model.Payment payment1 = new com.group6.swp391.model.Payment(paymentAmount, order, date, (order.getPrice() - paymentAmount) , order.getPrice());
                paymentService.save(payment1);

                // if customer pay all price successfully, in here add order detail

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
        session.setAttribute("orderID", null);
        long amount = (long) (order.getPrice()*100);
        String s = vnPayService.getVNPay(amount, req);
        response.sendRedirect(s);
    }

    @GetMapping("/vnpaysuccess")
    public ResponseEntity<PaymentResponse> vnpaysuccess(HttpServletRequest req) {
        String maloi = req.getParameter("vnp_ResponseCode");
        if(maloi.equals("00")) {
            return ResponseEntity.status(HttpStatus.OK).body(new PaymentResponse("Success", "Payment successfully", null, null));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentResponse("Failed", "Payment failed", null, null));
    }
}
