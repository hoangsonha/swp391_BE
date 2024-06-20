package com.group6.swp391.controller;

import com.group6.swp391.cart.Cart;
import com.group6.swp391.model.Order;
import com.group6.swp391.paypal.EnumPayPalPaymentMethod;
import com.group6.swp391.paypal.EnumPaypalPaymentIntent;
import com.group6.swp391.repository.OrderRepository;
import com.group6.swp391.request.OrderRequest;
import com.group6.swp391.response.PaymentResponse;
import com.group6.swp391.service.*;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
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
    public ResponseEntity<PaymentResponse> pay(HttpServletRequest request, @RequestBody OrderRequest orderRequest, HttpSession session) {
        String cancelUrl = request.getRequestURL().toString().replace(request.getServletPath(), "") + "/pay/cancel";
        String successUrl = request.getRequestURL().toString().replace(request.getServletPath(), "") + "/pay/information";

        try {
            Cart cart = (Cart) session.getAttribute("CART");
            Order order = new Order(orderRequest.getAddressShipping(), orderRequest.getFullName(), orderRequest.getOrderDate(), null, orderRequest.getPhoneShipping(), orderRequest.getPrice(), orderRequest.getQuantity(), orderRequest.getStatus(), userService.getUserByID(orderRequest.getUserID()));
            orderService.save(order);
            Payment payment = payPalService.createPayment(order, EnumPayPalPaymentMethod.paypal,
                    EnumPaypalPaymentIntent.sale, cancelUrl, successUrl, cart);
            session.setAttribute("payment", payment);
            for(Links links : payment.getLinks()) {
                if(links.getRel().equals("approval_url")) {
                    return ResponseEntity.status(HttpStatus.OK).body(new PaymentResponse("Success", "Redirect payment page successfully", payment, links.getHref()));
                }
            }
        } catch(Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentResponse("Failed", "Redirect payment page failed", null, null));
    }

    @PostMapping("/pay/cancel")
    public ResponseEntity<PaymentResponse> payCancel() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentResponse("Failed", "Payment page failed", null, null));
    }

    @PostMapping("/pay/information")
    public ResponseEntity<PaymentResponse> payInformation(HttpSession session, Model model, @RequestParam("paymentId") String paymentID, @RequestParam("PayerID") String payerID) {
        try {
            Payment payment = (Payment) session.getAttribute("payment");
            model.addAttribute("paymentId", paymentID);
            model.addAttribute("PayerId", payerID);
            return ResponseEntity.status(HttpStatus.OK).body(new PaymentResponse("Success", "Payment page information successfully", payment, "/pay/success"));
        } catch(Exception e) {
            log.error("ERROR at payment : {}", e.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentResponse("Failed", "Payment information failed", null, null));
        }
    }

    @PostMapping("/pay/success")
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

                session.setAttribute("CART", null);
                return ResponseEntity.status(HttpStatus.OK).body(new PaymentResponse("Success", "Payment successfully", payment, null));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentResponse("Failed", "Payment failed", null, null));
    }


    @GetMapping("/vnpay")
    public String vnpay(HttpServletRequest req) throws UnsupportedEncodingException {
        long amount = 100000*100;
        String s = vnPayService.getVNPay(amount, req);
        return s;
    }

    @GetMapping("/vnpaysuccess")
    public String vnpaysuccess(HttpServletRequest req) {
        String maloi = req.getParameter("vnp_ResponseCode");
        if(maloi.equals("00")) {
            return "payment successfully";
        }
        return "payment failed";
    }
}
