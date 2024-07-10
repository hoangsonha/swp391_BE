package com.group6.swp391.service;

import com.group6.swp391.model.WarrantyCard;

import java.util.List;
import java.util.Optional;

public interface WarrantyCardService {

    WarrantyCard getById(int warrantyCardID);

    List<WarrantyCard> getByUser(int userId);

    WarrantyCard createNew(WarrantyCard warrantyCard);

    void deleteWarrantyCard(int id);

    List<WarrantyCard> findWarrantyCardsExpiringSoon();

    List<WarrantyCard> getAll();

    Optional<WarrantyCard> searchWarrantyCards(String searchQuery);

}
