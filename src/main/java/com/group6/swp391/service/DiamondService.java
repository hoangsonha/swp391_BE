package com.group6.swp391.service;

import com.group6.swp391.model.Diamond;
import com.group6.swp391.request.SearchAdvanceRequest;

import java.util.List;

public interface DiamondService {
    public Diamond getDiamondByDiamondID(String diamondID);

    public Diamond creatDiamond(Diamond diamond);

    public List<Diamond> getAllDiamond();

    public Diamond updateDiamond(Diamond diamond);

    public void deleteDiamond(String diamondID);

    public void markDiamondAsDeleted(String diamondID);

    public List<Diamond> getByCondition(String shapeDiamond, float dimensions);

    public List<Diamond> searchAdvaned(SearchAdvanceRequest searchAdvanceRequest);
}
