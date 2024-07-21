package com.group6.swp391.service;

import com.google.gson.JsonObject;
import com.group6.swp391.vnpay.VNPay;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VNPayService {

    @Value("${vnpay.vnp_PayUrl}") private String vnp_PayUrl_Va;
    @Value("${vnpay.vnp_ReturnUrl}") private String vnp_ReturnUrl_Va;
    @Value("${vnpay.vnp_TmnCode}") private String vnp_TmnCode_Va;
    @Value("${vnpay.secretKey}") private String secretKey_Va;
    @Value("${vnpay.vnp_ApiUrl}") private String vnp_ApiUrl_Va;
    @Value("${vnpay.vnp_BankCode}") private String vnp_BankCode_Va;
    @Value("${vnpay.vnp_OrderType}") private String vnp_OrderType_Va;
    @Value("${vnpay.vnp_Command}") private String vnp_Command_Va;
    @Value("${vnpay.vnp_Version}") private String vnp_Version_Va;
    @Value("${vnpay.currency}") private String vnp_Currency_Va;
    @Value("${vnpay.timeZone}") private String vnp_TimeZone_Va;
    @Value("${vnpay.formatDate}") private String vnp_FormatDate_Va;

    public String getVNPay(double total, HttpServletRequest req, String orderID) throws UnsupportedEncodingException {
        String vnp_Version = vnp_Version_Va;
        String vnp_Command = vnp_Command_Va;
        String orderType = vnp_OrderType_Va;
        String bankCode = vnp_BankCode_Va;
        double amount = total;
//      String vnp_TxnRef = VNPay.getRandomNumber(8);
        String vnp_IpAddr = VNPay.getIpAddress(req);
        String vnp_TmnCode = vnp_TmnCode_Va;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", vnp_Currency_Va);
        vnp_Params.put("vnp_BankCode", bankCode);
        vnp_Params.put("vnp_TxnRef", orderID);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + orderID);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl_Va);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone(vnp_TimeZone_Va));
        SimpleDateFormat formatter = new SimpleDateFormat(vnp_FormatDate_Va);
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
                hashData.append(fieldName);                                                             // lấy ra field name
                hashData.append('=');                                                                   // gán dấu =
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));   // gán giá trị cho field
                                                                                                    // hashData : vnp_Amount=10000000
                                                                                                    //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));       // tạo ra query tên là field
                query.append('=');                                                                      // gán dấu =
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));      // gán giá trị cho query
                                                                                                    // query : vnp_Amount=10000000
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPay.hmacSHA512(secretKey_Va, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = vnp_PayUrl_Va + "?" + queryUrl;

        return paymentUrl;
    }

    public String refundVNPay(double total, HttpServletRequest req, String orderID) throws IOException {
        String vnp_RequestId = VNPay.getRandomNumber(8);
        String vnp_Version = "2.1.0";
        String vnp_Command = "refund";
        String vnp_TmnCode = vnp_TmnCode_Va;
        String vnp_TransactionType = "02";
        String vnp_TxnRef = orderID;
        double amount = total;
        String vnp_Amount = String.valueOf(amount);
        String vnp_OrderInfo = "Hoan tien GD OrderId:" + orderID;
        String vnp_TransactionNo = ""; //Assuming value of the parameter "vnp_TransactionNo" does not exist on your system.
        String vnp_CreateBy = "Hoang Son Ha";

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        String vnp_TransactionDate = formatter.format(cld.getTime());

        String vnp_IpAddr = VNPay.getIpAddress(req);

        JsonObject vnp_Params = new JsonObject ();

        vnp_Params.addProperty("vnp_RequestId", vnp_RequestId);
        vnp_Params.addProperty("vnp_Version", vnp_Version);
        vnp_Params.addProperty("vnp_Command", vnp_Command);
        vnp_Params.addProperty("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.addProperty("vnp_TransactionType", vnp_TransactionType);
        vnp_Params.addProperty("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.addProperty("vnp_Amount", vnp_Amount);
        vnp_Params.addProperty("vnp_OrderInfo", vnp_OrderInfo);

        if(vnp_TransactionNo != null && !vnp_TransactionNo.isEmpty())
        {
            vnp_Params.addProperty("vnp_TransactionNo", "{get value of vnp_TransactionNo}");
        }

        vnp_Params.addProperty("vnp_TransactionDate", vnp_TransactionDate);
        vnp_Params.addProperty("vnp_CreateBy", vnp_CreateBy);
        vnp_Params.addProperty("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.addProperty("vnp_IpAddr", vnp_IpAddr);

        String hash_Data= String.join("|", vnp_RequestId, vnp_Version, vnp_Command, vnp_TmnCode,
                vnp_TransactionType, vnp_TxnRef, vnp_Amount, vnp_TransactionNo, vnp_TransactionDate,
                vnp_CreateBy, vnp_CreateDate, vnp_IpAddr, vnp_OrderInfo);

        String vnp_SecureHash = VNPay.hmacSHA512(secretKey_Va, hash_Data.toString());

        vnp_Params.addProperty("vnp_SecureHash", vnp_SecureHash);

//      String paymentUrl = vnp_PayUrl_Va + "?" + "&vnp_SecureHash=" + vnp_SecureHash;
//
//      return paymentUrl;

        URL url = new URL (vnp_ApiUrl_Va);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

//      String queryUrl = vnp_Params.toString();
//      queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
//      String paymentUrl = vnp_ApiUrl_Va + "?" + queryUrl;

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(vnp_Params.toString());
        wr.flush();
        wr.close();
        int responseCode = con.getResponseCode();
        System.out.println("nSending 'POST' request to URL : " + url);
        System.out.println("Post Data : " + vnp_Params);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String output;
        StringBuffer response = new StringBuffer();
        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();
        return response.toString();
    }
}
