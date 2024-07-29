package com.group6.swp391.services;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface VNPayService {

    public String getVNPay(long total, HttpServletRequest req, String orderID) throws UnsupportedEncodingException;

    public String refundVNPay(double total, HttpServletRequest req, String orderID) throws IOException;

}
