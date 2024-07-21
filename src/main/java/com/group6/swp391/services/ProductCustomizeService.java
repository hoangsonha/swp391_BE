package com.group6.swp391.services;

import com.group6.swp391.pojos.ProductCustomize;

import java.util.List;

public interface ProductCustomizeService {
    ProductCustomize createProductCustomize(ProductCustomize productCustomize);

    ProductCustomize updateProductCustomize(String id, ProductCustomize productCustomize);
    void deleteProductCustomize(String id);

    ProductCustomize getProductCustomizeById(String id);
    List<ProductCustomize> getAllProductCustomize();


}
