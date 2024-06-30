package com.group6.swp391.service;

import com.group6.swp391.model.Diamond;
import com.group6.swp391.model.Product;
import com.group6.swp391.repository.DiamondRepository;
import com.group6.swp391.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImp implements SearchService {

    @Autowired
    private DiamondRepository diamondRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Object> search(String query) {
        List<Object> results = new ArrayList<>();

        List<Diamond> diamondsByName = diamondRepository.findByDiamondNameContaining(query);
        List<Diamond> diamondsByID = diamondRepository.findByDiamondIDContaining(query);

        for (Diamond diamond : diamondsByID) {
            if (!diamondsByName.contains(diamond)) {
                diamondsByName.add(diamond);
            }
        }
        results.addAll(diamondsByName);

        List<Product> products = productRepository.findByProductNameContaining(query);
        results.addAll(products);

        return results;
    }
}
