package com.group6.swp391.service;

import com.group6.swp391.model.CollectionProduct;

import java.util.List;

public interface CollectionProductService {
    CollectionProduct createCollectionProduct(CollectionProduct collectionProduct);
    CollectionProduct getCollectionProduct(int collectionProductId);
    List<CollectionProduct> getAllCollectionProducts();
    void updateCollectionProduct(int collectionId, CollectionProduct collectionProduct);
    void deleteCollectionProduct(String collectionProductId);
}
