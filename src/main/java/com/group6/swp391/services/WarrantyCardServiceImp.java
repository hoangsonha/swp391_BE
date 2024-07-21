package com.group6.swp391.services;

import com.group6.swp391.pojos.WarrantyCard;
import com.group6.swp391.repositories.WarrantyCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public Optional<WarrantyCard> searchWarrantyCards(String searchQuery) {
        Optional<WarrantyCard> byId = Optional.empty();
        try {
            int id = Integer.parseInt(searchQuery);
            byId = warrantyCardRepository.findById(id);
        } catch (NumberFormatException e) {
            // Không làm gì, tiếp tục tìm kiếm theo các trường hợp khác
        }

        if (byId.isPresent()) {
            return byId;
        }

        // Nếu không phải ID trực tiếp, tìm kiếm trong các trường hợp khác
        return warrantyCardRepository.searchWarrantyCard(searchQuery);
    }

//    @Override
//    public List<WarrantyCard> searchWarrantyCards(String searchQuery) {
//        Set<WarrantyCard> results = new LinkedHashSet<>(warrantyCardRepository.searchWarrantyCards(searchQuery));
//        try {
//            int id = Integer.parseInt(searchQuery);
//            List<WarrantyCard> warrantyCards = warrantyCardRepository.findByWarrantyCardID(id);
//            if (warrantyCards != null) {
//                results.addAll(warrantyCards);
//            }
//        } catch (NumberFormatException e) {
//
//        }
//        return results.stream().collect(Collectors.toList());
//    }

}
