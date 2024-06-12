package com.group6.swp391.service;

import com.group6.swp391.model.WarrantyCard;
import com.group6.swp391.repository.WarrantyCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class WarrantyCardServiceImp implements WarrantyCardService {

    @Autowired private WarrantyCardRepository warrantyCardRepository;


    @Override
    public WarrantyCard getById(int warrantyCardID) {
        return warrantyCardRepository.findById(warrantyCardID).orElseThrow(() -> new RuntimeException("Warrant card not found!"));
    }

    @Override
    public List<WarrantyCard> getByUser(int userId) {
        return warrantyCardRepository.findByUser(userId);
    }

    @Override
    public WarrantyCard createNew(WarrantyCard warrantyCard) {
        return warrantyCardRepository.save(warrantyCard);
    }

    @Override
    public void deleteWarrantyCard(int id) {
        WarrantyCard warrantyCard = getById(id);
        if (warrantyCard == null) {
            throw new RuntimeException("Warrant card not found!");
        }
        warrantyCardRepository.deleteById(id);
    }

    @Override
    public List<WarrantyCard> findWarrantyCardsExpiringSoon() {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 10);
        Date futureDate = calendar.getTime();
        return warrantyCardRepository.findWarrantyCardsExpiringSoon(now, futureDate);
    }
}
