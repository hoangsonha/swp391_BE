package com.group6.swp391.services;

import com.group6.swp391.pojos.Diamond;
import com.group6.swp391.requests.SearchAdvanceRequest;

import java.util.List;

public interface DiamondService {
    public Diamond getDiamondByDiamondID(String diamondID);

    public Diamond creatDiamond(Diamond diamond);

    public List<Diamond> getAllDiamond();

    public Diamond updateDiamond(Diamond diamond);

    public void deleteDiamond(String diamondID);

    public void markDiamondAsDeleted(String diamondID);

    public List<Diamond> searchAdvanced(SearchAdvanceRequest searchAdvanceRequest);

    public List<Diamond> searchAdvancedSpecification(SearchAdvanceRequest searchAdvanceRequest);

    List<Diamond> getByCondition(String shape, float dimensions);

    void deleteDiamonds(List<String> diamondIDs);

    Diamond saveDiamond(Diamond diamond);

    void updateStatus(String diamondId);

    List<Diamond> getByListDimensions(List<Float> dimensions);

    List<Diamond> getByDimension(float dimension);

    List<Diamond> getAllDiamondWithStatus();

}
