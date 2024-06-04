package com.group6.swp391.service;

import com.group6.swp391.model.ProductCustom;

public interface ProductCustomeService {
    ProductCustom createProductCustom(ProductCustom productCustom);
    ProductCustom getProductCustom(int id);
    ProductCustom updateProductCustom(ProductCustom productCustom);
    void deleteProductCustom(int id);
}
