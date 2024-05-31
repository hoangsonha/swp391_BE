package com.group6.swp391.controller;

import com.group6.swp391.paypal.EnumPayPalPaymentMethod;
import com.group6.swp391.paypal.EnumPaypalPaymentIntent;
import com.group6.swp391.request.PaymentRequest;
import com.group6.swp391.service.PayPalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/payment")
@Slf4j
public class PaymentController {

    @Autowired private PayPalService payPalService;

    @PostMapping("/pay")
    public String pay(HttpServletRequest request, @RequestBody PaymentRequest price) {
        String cancelUrl = request.getRequestURL().toString().replace(request.getServletPath(), "") + "/payment/pay/cancel";
        String successUrl = request.getRequestURL().toString().replace(request.getServletPath(), "") + "/payment/pay/success";

        try {
            Payment payment = payPalService.createPayment(price.getPrice(), "USD", EnumPayPalPaymentMethod.paypal,
                    EnumPaypalPaymentIntent.sale, "payment description", cancelUrl, successUrl);
            for(Links links : payment.getLinks()) {
                if(links.getRel().equals("approval_url")) {
                    return "redirect:" + links.getHref();
                }
            }
        } catch(Exception e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping("/pay/cancel")
    public String payCancel() {
        return "Cancel";
    }

    @GetMapping("/pay/success")
    public String paySuccess(@RequestParam("paymentId") String paymentID, @RequestParam("PayerID") String payerID) {
        try {
            Payment payment = payPalService.executePayment(paymentID, payerID);
            if(payment.getState().equals("approved")) {
                return "Success";
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "cancel";
    }

}
