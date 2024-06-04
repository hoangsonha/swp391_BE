package com.group6.swp391.service;

import com.group6.swp391.model.Size;
import com.group6.swp391.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SizeServiceImp implements SizeSerivce{
    @Autowired
    SizeRepository sizeRepository;

    @Override
    public Size createSize(Size sizes) {
        return sizeRepository.save(sizes);
    }

    @Override
    public List<Size> getAllSize() {
        return sizeRepository.findAll();
    }

    @Override
    public Size getSizeByValue(int sizeValue) {
        return sizeRepository.finByValue(sizeValue);
    }
}
