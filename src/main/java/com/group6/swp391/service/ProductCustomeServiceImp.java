package com.group6.swp391.service;

import com.group6.swp391.model.ProductCustom;
import com.group6.swp391.repository.ProductCustomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductCustomeServiceImp implements  ProductCustomeService{
    @Autowired
    ProductCustomeRepository productCustomeRepository;
    @Override
    public ProductCustom createProductCustom(ProductCustom productCustom) {
        return productCustomeRepository.save(productCustom);
    }

    @Override
    public ProductCustom getProductCustom(int id) {
        return productCustomeRepository.findById(id).orElseThrow(() -> new RuntimeException("Product Custom Not Found"));
    }

    @Override
    public ProductCustom updateProductCustom(ProductCustom productCustom) {
        ProductCustom existingProduct = getProductCustom(productCustom.getProdcutCustomId());
        if(existingProduct == null){
            throw new RuntimeException("Product Custom Not Found");
        } else {
            existingProduct.setProdcutCustomId(productCustom.getProdcutCustomId());
            existingProduct.setProduct(productCustom.getProduct());
            existingProduct.setDiamond(productCustom.getDiamond());
            existingProduct.setWagePrice(productCustom.getWagePrice());
            existingProduct.setRatio(productCustom.getRatio());
            existingProduct.setWarrantyCard(productCustom.getWarrantyCard());
            existingProduct.setTotalPrice(productCustom.getTotalPrice());
            productCustomeRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    public void deleteProductCustom(int id) {

    }
}
