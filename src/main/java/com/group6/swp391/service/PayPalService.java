package com.group6.swp391.service;

import com.group6.swp391.paypal.EnumPayPalPaymentMethod;
import com.group6.swp391.paypal.EnumPaypalPaymentIntent;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PayPalService {
    @Autowired private APIContext apiContext;

    public Payment createPayment(Double total, String currency,
                                 EnumPayPalPaymentMethod method,
                                 EnumPaypalPaymentIntent intent,
                                 String description,
                                 String cancelURL,
                                 String successURL) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(description);
        List<Transaction> transactions = new ArrayList<>();

        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method.toString());

        Payment payment = new Payment();
        payment.setIntent(intent.toString());
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelURL);
        redirectUrls.setReturnUrl(successURL);
        payment.setRedirectUrls(redirectUrls);
 //       apiContext.setMaskRequestId(true);
        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentID, String payerID) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentID);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerID);
        return payment.execute(apiContext, paymentExecution);
    }
}
