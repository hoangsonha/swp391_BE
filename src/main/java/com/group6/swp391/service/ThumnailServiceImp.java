package com.group6.swp391.service;

import com.group6.swp391.model.Collection;
import com.group6.swp391.model.Product;
import com.group6.swp391.model.Thumnail;
import com.group6.swp391.repository.CollectionRepository;
import com.group6.swp391.repository.ProductRepository;
import com.group6.swp391.repository.ThumnailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThumnailServiceImp implements ThumnailService {
    @Autowired
    ThumnailRepository thumnailRepository;
    @Autowired ProductRepository productRepository;
    @Autowired CollectionRepository collectionRepository;

    @Override
    public Thumnail createThumnail(Thumnail thumnails) {
        return thumnailRepository.save(thumnails);
    }


    @Override
    public List<Thumnail> getAllThumnails() {
        return thumnailRepository.findAll();
    }

    @Override
    public Thumnail getById(int id) {
        return thumnailRepository.finThumnailById(id);
    }

    @Override
    public void updateThumnail(int thumbnailId, Thumnail thumnail) {
        try {
            Thumnail existingThumnail = getById(thumbnailId);
            if(existingThumnail != null) {
                existingThumnail.setImageId(thumnail.getImageId());
                existingThumnail.setImageUrl(thumnail.getImageUrl());
                existingThumnail.setProduct(thumnail.getProduct());
                thumnailRepository.save(existingThumnail);
            } else {
                throw new RuntimeException("Thumnail Not Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteThumnail(int thumnailId) {
        if(thumnailRepository.existsById(thumnailId)) {
            thumnailRepository.deleteById(thumnailId);
        }
    }

    @Override
    public Thumnail createThumnailV2(String objectId, String imageUrl) {
        Thumnail thumnail = new Thumnail();
        if(objectId.startsWith("S") || objectId.startsWith("s")) {
            Collection collectionExisting = collectionRepository.findById(objectId).orElseThrow();
            if(collectionExisting == null) {
                throw new RuntimeException("Collection Not Found");
            }
            thumnail.setCollection(collectionExisting);
            thumnail.setImageUrl(imageUrl);
            thumnail.setProduct(null);
        } else {
            Product productExisting = productRepository.findById(objectId).orElseThrow();
            if(productExisting == null) {
                throw new RuntimeException("Product Not Found");
            }
            thumnail.setProduct(productExisting);
            thumnail.setImageUrl(imageUrl);
            thumnail.setCollection(null);
        }
        return thumnailRepository.save(thumnail);
    }

    @Override
    public void updateThumnaiV2(Thumnail thumnail) {
        thumnailRepository.save(thumnail);
    }

    @Override
    public List<Thumnail> getThumnailByObject(String objectId) {
        return thumnailRepository.getByObject(objectId);
    }
}
