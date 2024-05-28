package com.group6.swp391.service;

import com.group6.swp391.model.Diamond;
import com.group6.swp391.repository.DiamondRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiamondServiceImp implements DiamondService {

    @Autowired private DiamondRepository diamondRepository;

    @Override
    public Diamond getDiamondByName(String diamondName) {
        Diamond diamond = diamondRepository.getDiamondByDiamondName(diamondName);
        return diamond;
    }
}
