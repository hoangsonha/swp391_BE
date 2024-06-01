package com.group6.swp391.service;

import com.group6.swp391.cart.Cart;
import com.group6.swp391.model.Diamond;
import com.group6.swp391.model.Order;
import com.group6.swp391.model.Product;
import com.group6.swp391.model.User;
import com.group6.swp391.paypal.EnumPayPalPaymentMethod;
import com.group6.swp391.paypal.EnumPaypalPaymentIntent;


import com.group6.swp391.repository.UserRepository;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PayPalService {
    @Autowired private APIContext apiContext;
    @Autowired private UserRepository userRepository;

    public Payment createPayment(Order order,
                                 EnumPayPalPaymentMethod method,
                                 EnumPaypalPaymentIntent intent,
                                 String cancelURL,
                                 String successURL, Cart cart) throws PayPalRESTException {
        Amount amount = new Amount();
        double priceToUSD = priceToUSD(order.getPrice());
        amount.setCurrency("USD");
//        order.getPrice() = new BigDecimal(order.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        Details details = new Details();
        details.setShipping("0.00");
        details.setTax("0.00");
        details.setSubtotal(String.format("%.2f", priceToUSD));

        amount.setTotal(String.format("%.2f", priceToUSD));
        amount.setDetails(details);

        Transaction transaction = new Transaction();
        transaction.setDescription("Customer paying by method : " + method.toString());
        transaction.setAmount(amount);


        Payer payer = new Payer();
        payer.setPaymentMethod(method.toString());

        User user = userRepository.getUserByUserID(order.getUser().getUserID());

        PayerInfo payerInfo = new PayerInfo();
        payerInfo.setEmail(user.getEmail());
        payerInfo.setFirstName(user.getFirstName());
        payerInfo.setLastName(user.getLastName());
        payerInfo.setCountryCode("VN");
        payerInfo.setPhone(user.getPhone());
        payerInfo.setPayerId(String.valueOf(user.getUserID()));

        ShippingAddress shippingAddress = new ShippingAddress();
        String fullName = user.getFirstName() + " " + user.getLastName();
        shippingAddress.setRecipientName(fullName);
        shippingAddress.setLine1(user.getAddress());
        payerInfo.setShippingAddress(shippingAddress);

        payer.setPayerInfo(payerInfo);
        payer.setStatus("VERIFIED");

        ItemList itemList = new ItemList();
        List<Item> items = new ArrayList<>();

        if(cart != null) {
            List<Object> lists = cart.getTotalGoodsInCart();
            if(lists != null) {
                for(Object good : lists) {
                    if(good instanceof Product) {
                        Product product = (Product) good;
                        Item item = new Item();
                        item.setCurrency("USD");
                        item.setName(product.getProductName());
                        double priceProduct = priceToUSD(product.getTotalPrice());
                        item.setPrice((String.format("%.2f", priceProduct)));
                        item.setTax("0.00");
                        item.setQuantity("1");
                        items.add(item);
                    } else if(good instanceof Diamond) {
                        Diamond diamond = (Diamond) good;
                        Item item = new Item();
                        item.setCurrency("USD");
                        item.setName(diamond.getDiamondName());
                        double priceDiamond = priceToUSD(diamond.getTotalPrice());
                        item.setPrice((String.format("%.2f", priceDiamond)));
                        item.setTax("0.00");
                        item.setQuantity("1");
                        items.add(item);
                    }
                }
            }
        }

        itemList.setItems(items);
        transaction.setItemList(itemList);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payment payment = new Payment();
        payment.setIntent(intent.toString());

        payment.setId(String.valueOf(order.getOrderID()));

        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelURL);
        redirectUrls.setReturnUrl(successURL);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentID, String payerID) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentID);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerID);
        return payment.execute(apiContext, paymentExecution);
    }

    public double priceToUSD(double price) {
        return (price / 25450);
    }
}
