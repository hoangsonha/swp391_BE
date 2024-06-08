package com.group6.swp391.service;

import com.group6.swp391.model.Size;
import com.group6.swp391.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SizeServiceImp implements SizeService{
    @Autowired
    SizeRepository sizeRepository;

    @Override
    public Size createSize(Size size) {
        return sizeRepository.save(size);
    }

    @Override
    public Size getSizeById(int id) {
        return sizeRepository.findById(id).orElseThrow(() -> new RuntimeException("Size Not Found"));
    }

    @Override
    public Size getSizeByValue(int value) {
        return sizeRepository.findByValue(value);
    }

    @Override
    public void updateSize(int id, Size size) {
        try {
            Size existing = getSizeById(id);
            if(existing == null) {
                throw new RuntimeException("Size Not Found");
            } else {
                sizeRepository.save(size);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteSize(int id) {
        Size size = getSizeById(id);
        if (size == null) {
            throw new RuntimeException("Size Not Found");
        } else {
            sizeRepository.deleteById(id);
        }
    }
}
