package com.group6.swp391.enums;

public enum EnumPaymentMethod {
    paypal, vnpay;

    public static boolean checkExistPaymentMethod(String s) {
        try {
            if(EnumPaymentMethod.valueOf(s.toLowerCase()) != null) {
                return true;
            }
        } catch(Exception e) {
        }
        return false;
    }
}
