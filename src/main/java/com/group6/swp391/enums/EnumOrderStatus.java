package com.group6.swp391.enums;

public enum EnumOrderStatus {
    Chờ_thanh_toán,
    Đã_giao,
    Đã_hủy,
    Chờ_giao_hàng,
    Đã_hoàn_tiền;

    public static boolean checkExistOrderStatus(String s) {
        try {
            if(EnumOrderStatus.valueOf(s.toLowerCase()) != null) {
                return true;
            }
        } catch(Exception e) {
        }
        return false;
    }

}
