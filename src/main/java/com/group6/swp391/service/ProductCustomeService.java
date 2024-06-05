package com.group6.swp391.service;

import com.group6.swp391.model.ProductCustom;

public interface ProductCustomeService {
    ProductCustom createProductCustom(ProductCustom productCustom);

    ProductCustom updateProductCustom(ProductCustom productCustom);
    void deleteProductCustom(int id);

    ProductCustom getProductCustomById(String id);
}
