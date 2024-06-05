package com.group6.swp391.enums;

public enum EnumShapeName {
    ROUND, PEAR, EMERALD, HEART, CUSHION;

    public static boolean checkExistShape(String s) {
        try {
            if(EnumShapeName.valueOf(s.toUpperCase()) != null) {
                return true;
            }
        } catch(Exception e) {
        }
        return false;
    }
}
