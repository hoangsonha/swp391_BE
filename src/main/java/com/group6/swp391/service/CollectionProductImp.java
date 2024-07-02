package com.group6.swp391.service;

import com.group6.swp391.model.CollectionProduct;
import com.group6.swp391.repository.CollectionProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionProductImp implements CollectionProductService{
    @Autowired  CollectionProductRepository collectionProductRepository;

    @Override
    public CollectionProduct createCollectionProduct(CollectionProduct collectionProduct) {
        return collectionProductRepository.save(collectionProduct);
    }

    @Override
    public CollectionProduct getCollectionProduct(int collectionProductId) {
        return collectionProductRepository.findById(collectionProductId).orElseThrow(() -> new RuntimeException("Collection product not found"));
    }

    @Override
    public List<CollectionProduct> getAllCollectionProducts() {
        return collectionProductRepository.findAll();
    }

    @Override
    public void updateCollectionProduct(int collectionId, CollectionProduct collectionProduct) {
        CollectionProduct collectionProductExisting = getCollectionProduct(collectionId);
        if(collectionProductExisting == null) {
            throw new RuntimeException("Collection product not found");
        }
    }

    @Override
    public void deleteCollectionProduct(String collectionProductId) {

    }
}
