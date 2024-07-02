package com.group6.swp391.service;

import com.group6.swp391.model.Collection;

import java.util.List;

public interface CollectionService {
    Collection createCollection(Collection collection);
    List<Collection> getAllCollections();
    Collection getCollection(String id);
    void updateCollection(Collection collection);
    void deleteCollection(String id);
}
