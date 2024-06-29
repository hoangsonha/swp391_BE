package com.group6.swp391.service;


import com.group6.swp391.vnpay.VNPay;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VNPayService {

    public String getVNPay(long total, HttpServletRequest req, String orderID) throws UnsupportedEncodingException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long amount = total;
        String bankCode = "NCB";

//        String vnp_TxnRef = VNPay.getRandomNumber(8);

        String vnp_IpAddr = VNPay.getIpAddress(req);
        // 127.0.0.1
        String vnp_TmnCode = VNPay.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_BankCode", bankCode);

        vnp_Params.put("vnp_TxnRef", orderID);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + orderID);
        vnp_Params.put("vnp_OrderType", orderType);

        vnp_Params.put("vnp_Locale", "vn");

        vnp_Params.put("vnp_ReturnUrl", VNPay.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // ghép lại thành URL truy cập thanh toán

        List fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data (chèn chuỗi với append để tạo thành chuỗi )
                hashData.append(fieldName); // lấy ra field name
                hashData.append('=');          // gán dấu =
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString())); // gán giá trị cho field
                // hashData : vnp_Amount=10000000
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString())); // tạo ra query tên là field
                query.append('=');      // gán dấu =
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString())); // gán giá trị cho query
                // query : vnp_Amount=10000000
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPay.hmacSHA512(VNPay.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPay.vnp_PayUrl + "?" + queryUrl;

        return paymentUrl;
    }
}
