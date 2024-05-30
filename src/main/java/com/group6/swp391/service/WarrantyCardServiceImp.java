package com.group6.swp391.service;

import com.group6.swp391.model.WarrantyCard;
import com.group6.swp391.repository.WarrantyCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WarrantyCardServiceImp implements WarrantyCardService {

    @Autowired private WarrantyCardRepository warrantyCardRepository;

    @Override
    public WarrantyCard getWarrantyCardByWarrantyCardID(int warrantyCardID) {
        return warrantyCardRepository.getWarrantyCardByWarrantyCardID(warrantyCardID);
    }
}
