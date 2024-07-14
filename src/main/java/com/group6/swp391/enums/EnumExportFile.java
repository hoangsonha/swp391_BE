package com.group6.swp391.enums;

public enum EnumExportFile {
    EXCEL, HTML, PDF;

    public static boolean checkExistExportFile(String s) {
        try {
            if(EnumExportFile.valueOf(s.toUpperCase()) != null) {
                return true;
            }
        } catch(Exception e) {
        }
        return false;
    }

}
