package com.group6.swp391.service;

import com.group6.swp391.model.ChangePrice;
import com.group6.swp391.repository.ChangePriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChangePriceImp implements ChangePriceService{

    @Autowired
    ChangePriceRepository changePriceRepository;

    @Override
    public ChangePrice createPrice(ChangePrice changePrice) {
        return changePriceRepository.save(changePrice);
    }

    @Override
    public void updatePrice(int id, ChangePrice changePrice) {
        try {
            ChangePrice newPrice = getPrice(id);
            if(newPrice == null) {
                throw new RuntimeException("Change price not found");
            } else {
                newPrice.setChangePriceID(changePrice.getChangePriceID());
                newPrice.setWage(changePrice.getWage());
                newPrice.setSheathDiamond(changePrice.getSheathDiamond());
                newPrice.setTotalPrice(changePrice.getTotalPrice());
                changePriceRepository.save(newPrice);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ChangePrice getPrice(int id) {
        return changePriceRepository.findById(id).orElseThrow(() -> new RuntimeException("Change price not found"));
    }
}
