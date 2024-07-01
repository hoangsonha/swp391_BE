package com.group6.swp391.service;

import com.group6.swp391.model.Collection;
import com.group6.swp391.repository.CollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionServiceImp implements CollectionService {
    @Autowired CollectionRepository collectionRepository;

    @Override
    public Collection createCollection(Collection collection) {
        return collectionRepository.save(collection);
    }

    @Override
    public List<Collection> getAllCollections() {
        return collectionRepository.findAll();
    }

    @Override
    public Collection getCollection(int id) {
        return collectionRepository.findById(id).orElseThrow();
    }

    @Override
    public void updateCollection(Collection collection) {
        collectionRepository.save(collection);
    }

    @Override
    public void deleteCollection(int id) {
        collectionRepository.deleteById(id);
    }
}
