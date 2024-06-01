package com.group6.swp391.service;

import com.group6.swp391.model.Diamond;
import com.group6.swp391.repository.DiamondRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiamondServiceImp implements DiamondService {

    @Autowired private DiamondRepository diamondRepository;


    @Override
    public Diamond getDiamondByDiamondID(String diamondID) {
        return diamondRepository.getDiamondByDiamondID(diamondID);
    }

    @Override
    public Diamond creatDiamond(Diamond diamond) {
        return diamondRepository.save(diamond);
    }

    @Override
    public List<Diamond> getAllDiamond() {
        return diamondRepository.findAll();
    }
}
