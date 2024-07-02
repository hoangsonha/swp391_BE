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
    @Autowired UserService userService;
    @Autowired ProductCustomizeService productCustomizeService;
    @Autowired DiamondService diamondService;


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

    @Override
    public List<WarrantyCard> getAll() {
        return warrantyCardRepository.findAll();
    }

    @Override
    public List<WarrantyCard> findByProductID(String productID) {
        return warrantyCardRepository.findByProductCustomize_ProdcutCustomIdOrDiamond_DiamondID(productID, productID);
    }

    @Override
    public List<WarrantyCard> findByProductCustomerId(String productCustomerId) {
        return warrantyCardRepository.findByProductCustomize_ProdcutCustomIdContaining(productCustomerId);
    }

    @Override
    public List<WarrantyCard> searchWarrantyCards(String searchQuery) {
        List<WarrantyCard> results;
        try {
            int warrantyCardId = Integer.parseInt(searchQuery);
            results = warrantyCardRepository.findByWarrantyCardID(warrantyCardId);
        } catch (NumberFormatException e) {
            results = warrantyCardRepository.findByProductCustomize_ProdcutCustomIdContaining(searchQuery);
        }
        return results;
    }
}
