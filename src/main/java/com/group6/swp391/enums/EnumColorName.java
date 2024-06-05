package com.group6.swp391.enums;

public enum EnumColorName {
    D, E, F, G, H, I, J, K, L, M;

    public static boolean checkExistColor(String s) {
        try {
            if(EnumColorName.valueOf(s.toUpperCase()) != null) {
                return true;
            }
        } catch(Exception e) {
        }
        return false;
    }
}
