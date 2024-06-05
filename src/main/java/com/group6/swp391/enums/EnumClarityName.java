package com.group6.swp391.enums;

public enum EnumClarityName {
    IF, VVS1, VVS2, VS1, VS2, SI1, SI2, I1, I2;;

    public static boolean checkExistClarity(String s) {
        try {
            if(EnumClarityName.valueOf(s.toUpperCase()) != null) {
                return true;
            }
        } catch(Exception e) {
        }
        return false;
    }

}
