package com.group6.swp391.service;


import com.group6.swp391.model.Thumnail;

import java.util.List;

public interface ThumbnailService {
    Thumnail createThumbnail(Thumnail thumbnails);
    Thumnail getThumbnailById(int thumnailId);
    void updateThumbnail(int thumbnaiId, Thumnail thumnail);
    void deleteThumbnail(int thumbnailId);
    List<Thumnail> getAllWithProduct(String productId);
}
