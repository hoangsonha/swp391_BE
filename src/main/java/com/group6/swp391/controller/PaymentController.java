package com.group6.swp391.controller;

import com.group6.swp391.enumPayments.EnumPaymentMethod;
import com.group6.swp391.model.Order;
import com.group6.swp391.enumPayments.EnumPayPalPaymentMethod;
import com.group6.swp391.enumPayments.EnumPaypalPaymentIntent;
import com.group6.swp391.request.CancelPaymentRequest;
import com.group6.swp391.request.PaymentRequest;
import com.group6.swp391.response.PaymentResponse;
import com.group6.swp391.service.*;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public String pay(@RequestBody PaymentRequest paymentRequest, HttpServletRequest request, HttpServletResponse response) {
        try {
            if(paymentRequest.getPaymentMethod().equals("paypal") && EnumPaymentMethod.checkExistPaymentMethod(paymentRequest.getPaymentMethod())) {
                String cancelUrl = request.getRequestURL().toString().replace(request.getServletPath(), "") + "/payment/paypal/cancel?orderID=" + paymentRequest.getOrderID();
                String successUrl = request.getRequestURL().toString().replace(request.getServletPath(), "") + "/payment/paypal/success?orderID=" + paymentRequest.getOrderID();
                try {
                    Order order = orderService.getOrderByOrderID(Integer.parseInt(paymentRequest.getOrderID()));
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
            } else if(paymentRequest.getPaymentMethod().equals("vnpay") && EnumPaymentMethod.checkExistPaymentMethod(paymentRequest.getPaymentMethod())) {
                try {
                    Order order = orderService.getOrderByOrderID(Integer.parseInt(paymentRequest.getOrderID()));
                    if(order.getStatus().toLowerCase().equals("chờ thanh toán") && order != null) {
                        long amount = (long) (order.getPrice()*100);
                        long haveToPay = (amount / 25000);
                        String s = vnPayService.getVNPay(haveToPay, request, paymentRequest.getOrderID());
                        response.sendRedirect(s);
                    }
                } catch (Exception e) {
                    log.error("Error at VNPay payment: {}", e.toString());
                }
            }
        } catch(Exception e) {
            log.error(e.getMessage());
        }
        return "Checkout failed";
    }

    @GetMapping("/paypal/cancel")
    public ResponseEntity<PaymentResponse> payByPayPalCancel() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentResponse("Failed", "Payment page failed", null, null));
    }

    @GetMapping("/paypal/success")
    public ResponseEntity<PaymentResponse> paySuccess(@Param("orderID") String orderID, @RequestParam("paymentId") String paymentID, @RequestParam("PayerID") String payerID) {
        try {
            Order order = orderService.getOrderByOrderID(Integer.parseInt(orderID));
            if(order.getStatus().toLowerCase().equals("chờ thanh toán") && order != null) {
                Payment payment = payPalService.executePayment(paymentID, payerID);
                if(payment.getState().equals("approved")) {
                    SimpleDateFormat spm = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = spm.parse(payment.getCreateTime());
                    String transactionID = payment.getTransactions().get(0).getRelatedResources().get(0).getSale().getId();
//                  double paymentAmount = Double.parseDouble(payment.getTransactions().get(0).getAmount().getTotal());
//                  double payAmount = Math.floor(paymentAmount * 25500);
                    double remain = (order.getPrice() - order.getPrice());
                    com.group6.swp391.model.Payment payment1 = new com.group6.swp391.model.Payment(order.getPrice(), order, date, remain, order.getPrice(), transactionID, "Paypal", "Thanh toán thành công");
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

    @GetMapping("/vnpaysuccess")
    public ResponseEntity<PaymentResponse> vnpaysuccess(HttpServletRequest request) throws ParseException {
        String maloi = request.getParameter("vnp_ResponseCode"); // lay qua url
        String orderID = request.getParameter("vnp_TxnRef");
        String dateAt = request.getParameter("vnp_PayDate");
        String amount = request.getParameter("vnp_Amount");
        String nam = dateAt.substring(0, 4);
        String thang = dateAt.substring(4, 6);
        String ngay = dateAt.substring(6, 8);
        String gio = dateAt.substring(8, 10);
        String phut = dateAt.substring(10, 12);
        String giay = dateAt.substring(12, 14);
        String dateParse = nam + "-" + thang + "-" + ngay + " " + gio + ":" + phut + ":" + giay;
        Order order = orderService.getOrderByOrderID(Integer.parseInt(orderID));
        if(order.getStatus().toLowerCase().equals("chờ thanh toán") && order != null) {
            if(maloi.equals("00")) {
                SimpleDateFormat spm = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date date = spm.parse(dateParse);
                double remain = (order.getPrice() - (order.getPrice()));
                com.group6.swp391.model.Payment payment1 = new com.group6.swp391.model.Payment(order.getPrice(), order, date, remain, order.getPrice(), null, "VNPay", "Thanh toán thành công");
                paymentService.save(payment1);
                order.setStatus("Chờ giao hàng");
                orderService.save(order);
                return ResponseEntity.status(HttpStatus.OK).body(new PaymentResponse("Success", "Payment successfully", null, null));
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentResponse("Failed", "Payment failed", null, null));
    }

    @PostMapping("/paypal/refund")
    public ResponseEntity<PaymentResponse> refundWithPaypal(@RequestBody CancelPaymentRequest cancelPaymentRequest) throws PayPalRESTException {
        try {
            Order order = orderService.getOrderByOrderID(Integer.parseInt(cancelPaymentRequest.getOrderID()));
            if (order != null && order.getStatus().toLowerCase().equals("chờ giao hàng")) {
                com.group6.swp391.model.Payment payment = paymentService.findByOrder(order);
                double amount = 0;
                if(payment.getMethodPayment().toLowerCase().equals("paypal") && payment.getStatus().toLowerCase().equals("thanh toán thành công")) {
                    amount += payment.getPaymentAmount();
                }
                String saleID = payment.getTransactionId();
                if(saleID != null) {
                    double value = (amount / 25500);
                    String result = String.format("%.2f",value);
                    double amount_at = Math.floor(Double.parseDouble(result));
                    boolean check = payPalService.cancelPayment(saleID, amount_at, "USD");
                    if(check) {
                        order.setStatus("Đã hoàn tiền");
                        orderService.save(order);
                        payment.setStatus("Đã hoàn tiền");
                        paymentService.save(payment);
                        return ResponseEntity.status(HttpStatus.OK).body(new PaymentResponse("Success", "Refund successfully", null, null));
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentResponse("Failed", "Refund failed", null, null));
        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentResponse("Failed", "Refund failed", null, null));
        }
    }






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
