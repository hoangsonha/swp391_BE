package com.group6.swp391.service;

import com.group6.swp391.model.Size;

import java.util.List;

public interface SizeService {
    Size createSize(Size size);
    Size getSizeById(int id);
    Size getSizeByValue(int value);
    void updateSize(int id, Size size);
    void deleteSize(int id);
}
