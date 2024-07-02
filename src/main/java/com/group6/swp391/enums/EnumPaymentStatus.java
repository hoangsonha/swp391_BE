package com.group6.swp391.enums;

public enum EnumPaymentStatus {
    Đã_hủy,
    Thanh_toán_thành_công,
    Đã_hoàn_tiền;

    public static boolean checkExistPaymentStatus(String s) {
        try {
            if(EnumPaymentStatus.valueOf(s.toLowerCase()) != null) {
                return true;
            }
        } catch(Exception e) {
        }
        return false;
    }

}
