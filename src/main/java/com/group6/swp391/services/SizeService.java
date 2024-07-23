package com.group6.swp391.services;

import com.group6.swp391.pojos.Product;
import com.group6.swp391.pojos.Size;

public interface SizeService {
    Size createSize(Size size);

    Size getSizeById(int id);

    Size getSizeByValue(int value);

    void updateSize(int id, Size size);

    void deleteSize(int id);

    Size getSizeByProduct(Product product);
}
