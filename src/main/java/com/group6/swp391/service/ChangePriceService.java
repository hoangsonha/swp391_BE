package com.group6.swp391.service;

import com.group6.swp391.model.ChangePrice;

public interface ChangePriceService {
    ChangePrice createPrice(ChangePrice changePrice);

    void updatePrice(int id, ChangePrice changePrice);

    ChangePrice getPrice(int id);
}
