package com.group6.swp391.service;

import com.group6.swp391.model.ProductCustomize;
import com.group6.swp391.repository.ProductCustomizeRepository;
import com.group6.swp391.request.ProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCustomizeServiceImp implements ProductCustomizeService {
    @Autowired
    ProductCustomizeRepository productCustomizeRepository;
    @Autowired
    private ProductServiceImp productServiceImp;
    @Autowired
    private DiamondServiceImp diamondServiceImp;

    @Override
    public ProductCustomize createProductCustomize(ProductCustomize productCustomize) {
        return productCustomizeRepository.save(productCustomize);
    }


    @Override
    public ProductCustomize updateProductCustomize(String id, ProductCustomize productCustomize) {
        ProductCustomize existingProduct = getProductCustomizeById(id);
        if(existingProduct == null){
            throw new RuntimeException("Product Custom Not Found");
        } else {
            existingProduct.setProdcutCustomId(productCustomize.getProdcutCustomId());
            existingProduct.setProduct(productCustomize.getProduct());
            existingProduct.setDiamond(productCustomize.getDiamond());
            existingProduct.setTotalPrice(productCustomize.getTotalPrice());
            productCustomizeRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    public void deleteProductCustomize(String id) {
        ProductCustomize existingProduct = getProductCustomizeById(id);
        if(existingProduct == null){
            throw new RuntimeException("Product Custom Not Found");
        } else {
            productCustomizeRepository.delete(existingProduct);
        }
    }

    @Override
    public ProductCustomize getProductCustomizeById(String id) {
        return productCustomizeRepository.getProductCustomByProdcutCustomId(id);
    }

    @Override
    public List<ProductCustomize> getAllProductCustomize() {
        return productCustomizeRepository.findAll();
    }

}
