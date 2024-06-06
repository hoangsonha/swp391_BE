package com.group6.swp391.enums;

public enum EnumColorName {
    D, E, F, G, H, I, J, K, L, M;

    public static boolean checkExistColor(char s) {
        try {
            if(EnumColorName.valueOf(String.valueOf(s)) != null) {
                return true;
            }
        } catch(Exception e) {
        }
        return false;
    }
}
