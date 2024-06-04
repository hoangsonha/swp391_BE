package com.group6.swp391.service;

import com.group6.swp391.model.Thumnail;
import com.group6.swp391.repository.ThumnailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThumbnailSericeImp implements ThumbnailService{

    @Autowired
    ThumnailRepository thumnailRepository;

    @Override
    public Thumnail createThumbnail(Thumnail thumbnails) {
        return thumnailRepository.save(thumbnails);
    }

    @Override
    public Thumnail getThumbnailById(int thumnailId) {
        return thumnailRepository.findById(thumnailId).orElseThrow(() -> new RuntimeException("Thumbnail not found"));
    }

    @Override
    public void updateThumbnail(int thumbnaiId, Thumnail thumnail) {
        Thumnail existingThumbnail = getThumbnailById(thumbnaiId);
        if(existingThumbnail == null) {
            throw new RuntimeException("Thumbnail not found");
        } else {
            existingThumbnail.setImageUrl(thumnail.getImageUrl());
            thumnailRepository.save(existingThumbnail);
        }
    }

    @Override
    public void deleteThumbnail(int thumbnailId) {
        Thumnail existingThumbnail = getThumbnailById(thumbnailId);
        if(existingThumbnail != null) {
            thumnailRepository.delete(existingThumbnail);
        } else {
            throw new RuntimeException("Thumbnail not found");
        }
    }

    @Override
    public List<Thumnail> getAllWithProduct(String productId) {
        return thumnailRepository.getByProduct(productId);
    }
}
