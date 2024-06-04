package com.group6.swp391.service;

import com.group6.swp391.model.Size;

import java.util.List;

public interface SizeSerivce {
    Size createSize(Size size);
    List<Size> getAllSize();
    Size getSizeByValue(int sizeValue);
}
