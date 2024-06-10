package com.group6.swp391.service;

import com.group6.swp391.model.ProductCustom;
import com.group6.swp391.repository.ProductCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductCustomeServiceImp implements  ProductCustomeService{
    @Autowired
    ProductCustomRepository productCustomRepository;
    @Override
    public ProductCustom createProductCustom(ProductCustom productCustom) {
        return productCustomRepository.save(productCustom);
    }


    @Override
    public ProductCustom updateProductCustom(ProductCustom productCustom) {
        ProductCustom existingProduct = getProductCustomById(productCustom.getProdcutCustomId());
        if(existingProduct == null){
            throw new RuntimeException("Product Custom Not Found");
        } else {
            existingProduct.setProdcutCustomId(productCustom.getProdcutCustomId());
            existingProduct.setProduct(productCustom.getProduct());
            existingProduct.setDiamond(productCustom.getDiamond());
   //         existingProduct.setWagePrice(productCustom.getWagePrice());
     //       existingProduct.setRatio(productCustom.getRatio());
            existingProduct.setWarrantyCard(productCustom.getWarrantyCard());
            existingProduct.setTotalPrice(productCustom.getTotalPrice());
            productCustomRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    public void deleteProductCustom(int id) {

    }

    @Override
    public ProductCustom getProductCustomById(String id) {
        return productCustomRepository.getProductCustomByProdcutCustomId(id);
    }
}
