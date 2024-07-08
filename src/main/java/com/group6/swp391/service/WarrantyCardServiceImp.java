package com.group6.swp391.service;

import com.group6.swp391.model.WarrantyCard;
import com.group6.swp391.repository.WarrantyCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    public List<WarrantyCard> searchWarrantyCards(String searchQuery) {
        Set<WarrantyCard> results = new LinkedHashSet<>(warrantyCardRepository.searchWarrantyCards(searchQuery));
        try {
            int id = Integer.parseInt(searchQuery);
            List<WarrantyCard> warrantyCards = warrantyCardRepository.findByWarrantyCardID(id);
            if (warrantyCards != null) {
                results.addAll(warrantyCards);
            }
        } catch (NumberFormatException e) {

        }
        return results.stream().collect(Collectors.toList());
    }

//    @Override
//    public List<WarrantyCard> searchWarrantyCards(String searchQuery) {
//        List<WarrantyCard> results = warrantyCardRepository.searchWarrantyCards(searchQuery);
//        try {
//            int id = Integer.parseInt(searchQuery);
//            List<WarrantyCard> card = warrantyCardRepository.findByWarrantyCardID(id);
//            if (card != null) {
//                results.addAll(card);
//            }
//        } catch (NumberFormatException e) {
//
//        }
//        return results;
//    }

//    @Override
//    public WarrantyCard findByIdOrDiamondIdOrProductCustomId(String id) {
//        WarrantyCard warrantyCard = warrantyCardRepository.findByWarrantyCardID(id);
//        if (warrantyCard == null) {
//            warrantyCard = warrantyCardRepository.findByDiamond_DiamondID(id);
//        }
//        if (warrantyCard == null) {
//            warrantyCard = warrantyCardRepository.findByProductCustomize_ProdcutCustomId(id);
//        }
//        return warrantyCard;
//    }


}
