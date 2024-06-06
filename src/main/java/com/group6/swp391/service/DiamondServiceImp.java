package com.group6.swp391.service;

import com.group6.swp391.enums.EnumClarityName;
import com.group6.swp391.enums.EnumColorName;
import com.group6.swp391.enums.EnumShapeName;
import com.group6.swp391.model.Diamond;
import com.group6.swp391.repository.DiamondRepository;
import com.group6.swp391.request.SearchAdvanceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiamondServiceImp implements DiamondService {

    @Autowired
    private DiamondRepository diamondRepository;


    @Override
    public Diamond getDiamondByDiamondID(String diamondID) {
        return diamondRepository.getDiamondByDiamondID(diamondID);
    }

    @Override
    public Diamond creatDiamond(Diamond diamond) {
        return diamondRepository.save(diamond);
    }

    @Override
    public List<Diamond> getAllDiamond() {
        return diamondRepository.findAll();
    }

    @Override
    public Diamond updateDiamond(Diamond diamond) {
        return diamondRepository.save(diamond);
    }

    @Override
    public void deleteDiamond(String diamondID) {
        diamondRepository.deleteById(diamondID);
    }

    @Override
    public void markDiamondAsDeleted(String diamondID) {
        try {
            Diamond diamond = getDiamondByDiamondID(diamondID);
            if (diamond != null) {
                diamond.setStatus(false);
                diamondRepository.save(diamond);
            } else {
                throw new RuntimeException("Diamond not found with id: " + diamondID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<Diamond> searchAdvaned(SearchAdvanceRequest searchAdvanceRequest) {
        List<Diamond> lists_diamond = new ArrayList<>();

        String string_carat = searchAdvanceRequest.getCarat();
        String[] str_carat = string_carat.split(" - ");
        float caratBegin = Float.parseFloat(str_carat[0]);
        float caratEnd = Float.parseFloat(str_carat[1]);

        String string_size = searchAdvanceRequest.getSize();
        String[] str_size = string_size.split(" - ");
        float sizeBegin = Float.parseFloat(str_size[0]);
        float sizeEnd = Float.parseFloat(str_size[1]);

        boolean check_color = EnumColorName.checkExistColor(searchAdvanceRequest.getColor());
        char color = 0;
        if(check_color) {
            color = searchAdvanceRequest.getColor();
        }
        boolean check_clarify = EnumClarityName.checkExistClarity(searchAdvanceRequest.getClarify());
        String clarify = null;
        if(check_clarify) {
            clarify = searchAdvanceRequest.getClarify();
        }
        boolean check_shape = EnumShapeName.checkExistShape(searchAdvanceRequest.getShape());
        String shape = null;
        if(check_shape) {
            shape = searchAdvanceRequest.getShape();
        }
        String optionPrice = searchAdvanceRequest.getOptionPrice();

        String string_price = searchAdvanceRequest.getPrice();
        String[] str_price = string_price.split(" ");
        List<Double> lists_price = new ArrayList<>();
        for(int i =0; i< str_price.length; i++) {
            char c = str_price[i].charAt(0);
            if(c >= '0' && c <= '9') {
                double d = Double.parseDouble(str_price[i]);
                lists_price.add(d);
            }
        }
        double priceBegin = 0;
        double priceEnd = 0;
        if(lists_price.size() == 1) {
            priceBegin = lists_price.get(0);
        } else if(lists_price.size() == 2) {
            priceBegin = lists_price.get(0);
            priceEnd = lists_price.get(1);
        }



        if(optionPrice.equals("All product")) {
            if(priceBegin != 0 && priceEnd != 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin != 0 && sizeEnd != 0 && color != 0 && clarify != null && shape != null) {
                lists_diamond = diamondRepository.getDiamondBySearchAdvanced(priceEnd, priceBegin, caratEnd, caratBegin, sizeEnd, sizeBegin, color, clarify, shape);
            } else if(priceBegin != 0 && priceEnd != 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin != 0 && sizeEnd != 0 && color != 0 && clarify != null && shape == null) {

            }
        } else if(optionPrice.equals("All product ASC")) {
            if(priceBegin != 0 && priceEnd != 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin != 0 && sizeEnd != 0 && color != 0 && clarify != null && shape != null) {
                lists_diamond = diamondRepository.getDiamondBySearchAdvancedByASC(priceEnd, priceBegin, caratEnd, caratBegin, sizeEnd, sizeBegin, color, clarify, shape);
            }
        } else if(optionPrice.equals("All product DESC")) {
            if(priceBegin != 0 && priceEnd != 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin != 0 && sizeEnd != 0 && color != 0 && clarify != null && shape != null) {
                lists_diamond = diamondRepository.getDiamondBySearchAdvancedByDESC(priceEnd, priceBegin, caratEnd, caratBegin, sizeEnd, sizeBegin, color, clarify, shape);
            }
        }

        lists_diamond = diamondRepository.getDiamondBySearchAdvanced(priceEnd, priceBegin, caratEnd, caratBegin, sizeEnd, sizeBegin, color, clarify, shape);

        return lists_diamond;
    }


}
