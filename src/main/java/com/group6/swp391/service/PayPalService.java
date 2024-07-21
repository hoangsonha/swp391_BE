package com.group6.swp391.service;

import com.group6.swp391.model.*;
import com.group6.swp391.model.Order;
import com.group6.swp391.enums.EnumPayPalPaymentMethod;
import com.group6.swp391.enums.EnumPaypalPaymentIntent;
import com.group6.swp391.repository.UserRepository;
import com.paypal.api.payments.*;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PayPalService {
    @Autowired private APIContext apiContext;
    @Autowired private UserRepository userRepository;

    @Autowired private CrawledDataProperties priceInDola;

    @Value("${paypal.currency}")
    private String currency;

    @Value("${paypal.tax}")
    private String tax;

    @Value("${paypal.shipping}")
    private String shipping;

    @Value("${paypal.countryCode}")
    private String countryCode;

    @Value("${paypal.verified}")
    private String verified;

    @Value("${paypal.successPayment}")
    private String successPayment;

    public Payment createPayment(Order order,
                                 EnumPayPalPaymentMethod method,
                                 EnumPaypalPaymentIntent intent,
                                 String cancelURL,
                                 String successURL) throws PayPalRESTException {
        Amount amount = new Amount();
        double priceToUSD = priceToUSD(order.getPrice());
        amount.setCurrency(currency);
//        order.getPrice() = new BigDecimal(order.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        Details details = new Details();
        details.setShipping(shipping);
        details.setTax(tax);
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
        payerInfo.setCountryCode(countryCode);
//        payerInfo.setPhone(user.getPhone());
        payerInfo.setPayerId(String.valueOf(user.getUserID()));

        ShippingAddress shippingAddress = new ShippingAddress();
        String fullName = user.getFirstName() + " " + user.getLastName();
        shippingAddress.setRecipientName(fullName);
        shippingAddress.setLine1(user.getAddress());
        payerInfo.setShippingAddress(shippingAddress);

        boolean checkActive = user.isEnabled();
        String actived = null;
        if(checkActive) {
            actived = verified;
        }

        payer.setPayerInfo(payerInfo);
        payer.setStatus(actived);

//        ItemList itemList = new ItemList();
//        List<Item> items = new ArrayList<>();

//        if(cart != null) {
//            List<Object> lists = cart.getTotalGoodsInCart();
//            if(lists != null) {
//                for(Object good : lists) {
//                    if(good instanceof Product) {
//                        Product product = (Product) good;
//                        Item item = new Item();
//                        item.setCurrency("USD");
//                        item.setName(product.getProductName());
//                        double priceProduct = priceToUSD(product.getTotalPrice());
//                        item.setPrice((String.format("%.2f", priceProduct)));
//                        item.setTax("0.00");
//                        item.setQuantity("1");
//                        items.add(item);
//                    } else if(good instanceof Diamond) {
//                        Diamond diamond = (Diamond) good;
//                        Item item = new Item();
//                        item.setCurrency("USD");
//                        item.setName(diamond.getDiamondName());
//                        double priceDiamond = priceToUSD(diamond.getTotalPrice());
//                        item.setPrice((String.format("%.2f", priceDiamond)));
//                        item.setTax("0.00");
//                        item.setQuantity("1");
//                        items.add(item);
//                    }
//                }
//            }
//        }

//        itemList.setItems(items);
//        transaction.setItemList(itemList);

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
        String dola = priceInDola.getDola().replace(".", "");
        double value = (price / Double.parseDouble(dola));
        String s = String.format("%.2f",value);
        return Math.floor(Double.parseDouble(s));
    }

    public boolean cancelPayment(String transactionId, double amount, String currency) {
        try {
            Amount refundAmount = new Amount();
            refundAmount.setCurrency(currency);
            refundAmount.setTotal(String.valueOf(amount));

            Refund refund = new Refund();
            refund.setAmount(refundAmount);

            // Thực hiện hoàn tiền
            Sale sale = new Sale();
            sale.setId(transactionId);

            refund = sale.refund(apiContext, refund);

            if (successPayment.equalsIgnoreCase(refund.getState())) {
                return true;
            } else {
                log.error("Error canceling payment {}", transactionId);
                return false;
            }
        } catch (PayPalRESTException e) {
            log.error("Error canceling payment {}", transactionId, e);
            return false;
        }
    }

//    public void cancelPayment(String saleID) throws PayPalRESTException {
//        String transactionId = saleID;
//
//        double amount = 10000.00;
//        String currency = "USD";
//        RefundRequest refundRequest = new RefundRequest();
//        Amount refundAmount = new Amount();
//        refundAmount.setCurrency(currency);
//        refundAmount.setTotal(String.valueOf(amount));
//        refundRequest.setAmount(refundAmount);
//        Sale sale = new Sale();
//        sale.setId(transactionId);
//        Refund refund = sale.refund(apiContext, refundRequest);
//
//        System.out.println("Trạng thái hoàn tiền: " + refund.getState());
//    }

}
