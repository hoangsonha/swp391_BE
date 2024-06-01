package com.group6.swp391.service;

import com.group6.swp391.model.Diamond;

import java.util.List;

public interface DiamondService {
    Diamond getDiamondByDiamondID(String diamondID);
    Diamond creatDiamond(Diamond diamond);
    List<Diamond> getAllDiamond();
}
