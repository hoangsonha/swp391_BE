package com.group6.swp391.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadImageService {

    public boolean deleteImageOnFireBase(String urlImage) throws IOException;

    public String uploadFileBase64(String base64Image) throws IOException;

    public String upload(MultipartFile multipartFile);

    public String upload(MultipartFile[] multipartFile);

    public String generateImageWithInitial(String userName) throws IOException;

}
