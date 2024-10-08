package com.group6.swp391.services;

import com.group6.swp391.enums.EnumClarityName;
import com.group6.swp391.enums.EnumColorName;
import com.group6.swp391.enums.EnumShapeName;
import com.group6.swp391.enums.EnumSortSearchAdvance;
import com.group6.swp391.pojos.Diamond;
import com.group6.swp391.repositories.DiamondRepository;
import com.group6.swp391.requests.SearchAdvanceRequest;
import com.group6.swp391.specifications.DiamondSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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


    // Search


    @Override
    public List<Diamond> searchAdvancedSpecification(SearchAdvanceRequest searchAdvanceRequest) {

        // Specìication hỗ trợ build câu query,
        // spe đã lấy ra đc Diamond -> diamond đc tiìm kiếm là where trên
        // đối tượng nào là root (root là diamond), câu query, build query

        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();

        String caratBegin = "0";
        String caratEnd = String.valueOf(diamondRepository.getMaxCarat());
        String string_carat = searchAdvanceRequest.getCarat();
        if(string_carat != null && StringUtils.hasText(string_carat)) {
            String[] str_carat = string_carat.split(" - ");
            if(str_carat.length == 2) {
                caratBegin = str_carat[0];
                caratEnd = str_carat[1];
            }
        }
        keys.add("caratBegin");
        values.add(caratBegin);
        keys.add("caratEnd");
        values.add(caratEnd);


        String sizeBegin = "0";
        String sizeEnd = String.valueOf(diamondRepository.getMaxDimensions());
        String string_size = searchAdvanceRequest.getSize();
        if(string_size != null && StringUtils.hasText(string_size)) {
            String[] str_size = string_size.split(" - ");
            if(str_size.length == 2) {
                sizeBegin = str_size[0];
                sizeEnd = str_size[1];
            }
        }
        keys.add("sizeBegin");
        values.add(sizeBegin);
        keys.add("sizeEnd");
        values.add(sizeEnd);


        boolean check_color = EnumColorName.checkExistColor(searchAdvanceRequest.getColor());
        char colorLevel = 0;
        if(check_color) {
            colorLevel = searchAdvanceRequest.getColor();
            keys.add("colorLevel");
            values.add(String.valueOf(colorLevel));
        }


        boolean check_clarify = EnumClarityName.checkExistClarity(searchAdvanceRequest.getClarify());
        String clarify = null;
        if(check_clarify) {
            clarify = searchAdvanceRequest.getClarify();
            keys.add("clarify");
            values.add(clarify);
        }


        boolean check_shape = EnumShapeName.checkExistShape(searchAdvanceRequest.getShape());
        String shape = null;
        if(check_shape) {
            shape = searchAdvanceRequest.getShape();
            keys.add("shape");
            values.add(shape);
        }


        boolean checkUp = false;
        String priceBegin = "0";
        String priceEnd = String.valueOf(diamondRepository.getMaxTotalPrice());
        String string_price = searchAdvanceRequest.getPrice();
        if(string_price != null && StringUtils.hasText(string_price)) {
            String[] str_price = string_price.split(" ");
            List<String> lists_price = new ArrayList<>();
            for(int i =0; i< str_price.length; i++) {
                if(str_price[i].toLowerCase().equals("trên")) {
                    checkUp = true;
                }
                char c = str_price[i].charAt(0);
                if(c >= '0' && c <= '9') {
                    str_price[i] += "000000";
                    lists_price.add(str_price[i]);
                }
            }
            if(lists_price.size() == 1) {
                if(checkUp) {
                    priceBegin = lists_price.get(0);
                    priceEnd = String.valueOf(diamondRepository.getMaxTotalPrice());
                } else {
                    priceBegin = "0";
                    priceEnd = lists_price.get(0);
                }
            } else if(lists_price.size() == 2) {
                priceBegin = lists_price.get(0);
                priceEnd = lists_price.get(1);
            }
        }
        keys.add("priceBegin");
        values.add(priceBegin);
        keys.add("priceEnd");
        values.add(priceEnd);


        List<Diamond> diamonds = diamondRepository.findAll();
        Specification<Diamond> spec = Specification.where(null);

        if(keys.size() == values.size()) {
            for(int i = 0; i < keys.size(); i++) {
                String field = keys.get(i);
                String value = values.get(i);
                Specification<Diamond> newSpec = DiamondSpecification.searchByField(field, value);
                if(newSpec != null) {
                    spec = spec.and(newSpec);
                }
            }
            diamonds = diamondRepository.findAll(spec);
        }

        if(diamonds.size() >= 0) {
            if(StringUtils.hasText(searchAdvanceRequest.getOptionPrice())) {
                if (searchAdvanceRequest.getOptionPrice().trim().toLowerCase().equals(EnumSortSearchAdvance.giá_từ_thấp_đến_cao.name().replaceAll("_", " "))) {
                    Sort sort = Sort.by(Sort.Direction.ASC, "totalPrice");
                    diamonds = diamondRepository.findAll(spec, sort);
                } else if(searchAdvanceRequest.getOptionPrice().trim().toLowerCase().equals(EnumSortSearchAdvance.giá_từ_cao_đến_thấp.name().replaceAll("_", " "))) {
                    Sort sort = Sort.by(Sort.Direction.DESC, "totalPrice");
                    diamonds = diamondRepository.findAll(spec, sort);
                }
            }
        }

        return diamonds;
    }

    @Override
    public List<Diamond> searchAdvanced(SearchAdvanceRequest searchAdvanceRequest) {
        List<Diamond> lists_diamond = new ArrayList<>();

        float caratBegin = 0;
        float caratEnd = 0;
        String string_carat = searchAdvanceRequest.getCarat();
        if(string_carat != null && StringUtils.hasText(string_carat)) {
            String[] str_carat = string_carat.split(" - ");
            if(str_carat.length == 2) {
                caratBegin = Float.parseFloat(str_carat[0]);
                caratEnd = Float.parseFloat(str_carat[1]);
            }
        }

        float sizeBegin = 0;
        float sizeEnd = 0;
        String string_size = searchAdvanceRequest.getSize();
        if(string_size != null && StringUtils.hasText(string_size)) {
            String[] str_size = string_size.split(" - ");
            if(str_size.length == 2) {
                sizeBegin = Float.parseFloat(str_size[0]);
                sizeEnd = Float.parseFloat(str_size[1]);
            }
        }


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
        boolean checkUpOrDown = false;
        double priceBegin = 0;
        double priceEnd = 0;
        String string_price = searchAdvanceRequest.getPrice();
        if(string_price != null && StringUtils.hasText(string_price)) {
            String[] str_price = string_price.split(" ");
            List<Double> lists_price = new ArrayList<>();
            for(int i =0; i< str_price.length; i++) {
                if(str_price[i].toLowerCase().equals("trên")) {
                    checkUpOrDown = true;
                }
                char c = str_price[i].charAt(0);
                if(c >= '0' && c <= '9') {
                    str_price[i] += "000000";
                    double d = Double.parseDouble(str_price[i]);
                    lists_price.add(d);
                }
            }

            if(lists_price.size() == 1) {
                if(checkUpOrDown) {
                    priceBegin = lists_price.get(0);
                    priceEnd = diamondRepository.getMaxTotalPrice();
                } else {
                    priceBegin = 0;
                    priceEnd = lists_price.get(0);
                }
            } else if(lists_price.size() == 2) {
                priceBegin = lists_price.get(0);
                priceEnd = lists_price.get(1);
            }
        }

        if(priceBegin != 0 && priceEnd != 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin != 0 && sizeEnd != 0 && color != 0 && clarify != null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvanced(priceEnd, priceBegin, caratEnd, caratBegin, sizeEnd, sizeBegin, color, clarify, shape);
        } else if(priceBegin != 0 && priceEnd != 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin != 0 && sizeEnd != 0 && color != 0 && clarify != null && shape == null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeShape(priceEnd, priceBegin, caratEnd, caratBegin, sizeEnd, sizeBegin, color, clarify);
        } else if(priceBegin != 0 && priceEnd != 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin != 0 && sizeEnd != 0 && color != 0 && clarify == null && shape == null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeShapeClarify(priceEnd, priceBegin, caratEnd, caratBegin, sizeEnd, sizeBegin, color);
        } else if(priceBegin != 0 && priceEnd != 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin != 0 && sizeEnd != 0 && color == 0 && clarify != null && shape == null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeShapeColor(priceEnd, priceBegin, caratEnd, caratBegin, sizeEnd, sizeBegin, clarify);
        } else if(priceBegin != 0 && priceEnd != 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin == 0 && sizeEnd == 0 && color != 0 && clarify != null && shape == null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeShapeDimensions(priceEnd, priceBegin, caratEnd, caratBegin, color, clarify);
        } else if(priceBegin != 0 && priceEnd != 0 && caratBegin == 0 && caratEnd == 0 && sizeBegin != 0 && sizeEnd != 0 && color != 0 && clarify != null && shape == null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeShapeCarat(priceEnd, priceBegin, sizeEnd, sizeBegin, color, clarify);
        } else if(priceBegin == 0 && priceEnd == 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin != 0 && sizeEnd != 0 && color != 0 && clarify != null && shape == null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeShapeTotalPrice(caratEnd, caratBegin, sizeEnd, sizeBegin, color, clarify);
        } else if(priceBegin != 0 && priceEnd != 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin != 0 && sizeEnd != 0 && color != 0 && clarify == null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeClarify(priceEnd, priceBegin, caratEnd, caratBegin, sizeEnd, sizeBegin, color, shape);
        } else if(priceBegin != 0 && priceEnd != 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin != 0 && sizeEnd != 0 && color == 0 && clarify == null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeClarifyColor(priceEnd, priceBegin, caratEnd, caratBegin, sizeEnd, sizeBegin, shape);
        } else if(priceBegin != 0 && priceEnd != 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin == 0 && sizeEnd == 0 && color != 0 && clarify == null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeClarifyDimensions(priceEnd, priceBegin, caratEnd, caratBegin, color, shape);
        } else if(priceBegin != 0 && priceEnd != 0 && caratBegin == 0 && caratEnd == 0 && sizeBegin != 0 && sizeEnd != 0 && color != 0 && clarify == null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeClarifyCarat(priceEnd, priceBegin, sizeEnd, sizeBegin, color, shape);
        } else if(priceBegin == 0 && priceEnd == 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin != 0 && sizeEnd != 0 && color != 0 && clarify == null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeClarifyTotalPrice(caratEnd, caratBegin, sizeEnd, sizeBegin, color, shape);
        } else if(priceBegin != 0 && priceEnd != 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin != 0 && sizeEnd != 0 && color == 0 && clarify != null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeColor(priceEnd, priceBegin, caratEnd, caratBegin, sizeEnd, sizeBegin, clarify, shape);
        } else if(priceBegin != 0 && priceEnd != 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin == 0 && sizeEnd == 0 && color == 0 && clarify != null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeColorDimensions(priceEnd, priceBegin, caratEnd, caratBegin, clarify, shape);
        } else if(priceBegin != 0 && priceEnd != 0 && caratBegin == 0 && caratEnd == 0 && sizeBegin != 0 && sizeEnd != 0 && color == 0 && clarify != null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeColorCarat(priceEnd, priceBegin, sizeEnd, sizeBegin, clarify, shape);
        } else if(priceBegin == 0 && priceEnd == 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin != 0 && sizeEnd != 0 && color == 0 && clarify != null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeColorTotalPrice(caratEnd, caratBegin, sizeEnd, sizeBegin, clarify, shape);
        } else if(priceBegin != 0 && priceEnd != 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin == 0 && sizeEnd == 0 && color != 0 && clarify != null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeDimensions(priceEnd, priceBegin, caratEnd, caratBegin, color, clarify, shape);
        } else if(priceBegin != 0 && priceEnd != 0 && caratBegin == 0 && caratEnd == 0 && sizeBegin == 0 && sizeEnd == 0 && color != 0 && clarify != null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedDimensionsCarat(priceEnd, priceBegin, color, clarify, shape);
        } else if(priceBegin == 0 && priceEnd == 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin == 0 && sizeEnd == 0 && color != 0 && clarify != null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeDimensionsTotalPrice(caratEnd, caratBegin, color, clarify, shape);
        } else if(priceBegin != 0 && priceEnd != 0 && caratBegin == 0 && caratEnd == 0 && sizeBegin != 0 && sizeEnd != 0 && color != 0 && clarify != null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeCarat(priceEnd, priceBegin, sizeEnd, sizeBegin, color, clarify, shape);
        } else if(priceBegin == 0 && priceEnd == 0 && caratBegin == 0 && caratEnd == 0 && sizeBegin != 0 && sizeEnd != 0 && color != 0 && clarify != null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeCaratTotalPrice(sizeEnd, sizeBegin, color, clarify, shape);
        } else if(priceBegin == 0 && priceEnd == 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin != 0 && sizeEnd != 0 && color != 0 && clarify != null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeTotalPrice(caratEnd, caratBegin, sizeEnd, sizeBegin, color, clarify, shape);
        } else if(priceBegin != 0 && priceEnd != 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin != 0 && sizeEnd != 0 && color == 0 && clarify == null && shape == null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeShapeColorClarify(priceEnd, priceBegin, caratEnd, caratBegin, sizeEnd, sizeBegin);
        } else if(priceBegin != 0 && priceEnd != 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin == 0 && sizeEnd == 0 && color == 0 && clarify == null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeDimensionsColorClarify(priceEnd, priceBegin, caratEnd, caratBegin, shape);
        } else if(priceBegin != 0 && priceEnd != 0 && caratBegin == 0 && caratEnd == 0 && sizeBegin == 0 && sizeEnd == 0 && color == 0 && clarify != null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeDimensionsColorCarat(priceEnd, priceBegin, clarify, shape);
        } else if(priceBegin == 0 && priceEnd == 0 && caratBegin == 0 && caratEnd == 0 && sizeBegin == 0 && sizeEnd == 0 && color != 0 && clarify != null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeDimensionsTotalPriceCarat(color, clarify, shape);
        } else if(priceBegin != 0 && priceEnd != 0 && caratBegin == 0 && caratEnd == 0 && sizeBegin == 0 && sizeEnd == 0 && color != 0 && clarify == null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeCaratDimensionClarity(priceEnd, priceBegin, color, shape);
        } else if(priceBegin != 0 && priceEnd != 0 && caratBegin == 0 && caratEnd == 0 && sizeBegin == 0 && sizeEnd == 0 && color != 0 && clarify != null && shape == null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeCaratDimensionShape(priceEnd, priceBegin, color, clarify);
        } else if(priceBegin != 0 && priceEnd != 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin == 0 && sizeEnd == 0 && color == 0 && clarify != null && shape == null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeColorDimensionShape(priceEnd, priceBegin, caratEnd, caratBegin, clarify);
        } else if(priceBegin == 0 && priceEnd == 0 && caratBegin == 0 && caratEnd == 0 && sizeBegin != 0 && sizeEnd != 0 && color == 0 && clarify != null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeTotalPriceCaratColor(sizeEnd, sizeBegin, clarify, shape);
        } else if(priceBegin == 0 && priceEnd == 0 && caratBegin == 0 && caratEnd == 0 && sizeBegin != 0 && sizeEnd != 0 && color != 0 && clarify == null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeTotalPriceCaratClarify(sizeEnd, sizeBegin, color, shape);
        } else if(priceBegin == 0 && priceEnd == 0 && caratBegin == 0 && caratEnd == 0 && sizeBegin != 0 && sizeEnd != 0 && color != 0 && clarify != null && shape == null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeTotalPriceCaratShape(sizeEnd, sizeBegin, color, clarify);
        } else if(priceBegin == 0 && priceEnd == 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin == 0 && sizeEnd == 0 && color != 0 && clarify == null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeTotalPriceDimensionsClarify(caratEnd, caratBegin, color, shape);
        } else if(priceBegin == 0 && priceEnd == 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin == 0 && sizeEnd == 0 && color == 0 && clarify != null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeTotalPriceColorDimensions(caratEnd, caratBegin, clarify, shape);
        } else if(priceBegin == 0 && priceEnd == 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin == 0 && sizeEnd == 0 && color != 0 && clarify != null && shape == null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeTotalPriceDimensionsShape(caratEnd, caratBegin, color, clarify);
        } else if(priceBegin == 0 && priceEnd == 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin != 0 && sizeEnd != 0 && color != 0 && clarify == null && shape == null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeTotalPriceShapeClarify(caratEnd, caratBegin, sizeEnd, sizeBegin, color);
        } else if(priceBegin == 0 && priceEnd == 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin != 0 && sizeEnd != 0 && color == 0 && clarify == null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeTotalPriceColorClarify(caratEnd, caratBegin, sizeEnd, sizeBegin, shape);
        } else if(priceBegin == 0 && priceEnd == 0 && caratBegin == 0 && caratEnd == 0 && sizeBegin == 0 && sizeEnd == 0 && color == 0 && clarify != null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeTotalPriceCaratDimensionsColor(clarify, shape);
        } else if(priceBegin != 0 && priceEnd != 0 && caratBegin == 0 && caratEnd == 0 && sizeBegin == 0 && sizeEnd == 0 && color == 0 && clarify == null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeClarifyCaratDimensionsColor(priceEnd, priceBegin, shape);
        } else if(priceBegin != 0 && priceEnd != 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin == 0 && sizeEnd == 0 && color == 0 && clarify == null && shape == null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeClarifyShapeDimensionsColor(priceEnd, priceBegin, caratEnd, caratBegin);
        } else if(priceBegin == 0 && priceEnd == 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin == 0 && sizeEnd == 0 && color == 0 && clarify == null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeTotalPriceClarifyDimensionsColor(caratEnd, caratBegin, shape);
        } else if(priceBegin == 0 && priceEnd == 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin != 0 && sizeEnd != 0 && color == 0 && clarify == null && shape == null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeTotalPriceClarifyShapeColor(caratEnd, caratBegin, sizeEnd, sizeBegin);
        } else if(priceBegin == 0 && priceEnd == 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin != 0 && sizeEnd != 0 && color == 0 && clarify != null && shape == null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeTotalPriceColorShape(caratEnd, caratBegin, sizeEnd, sizeBegin, clarify);
        } else if(priceBegin != 0 && priceEnd != 0 && caratBegin == 0 && caratEnd == 0 && sizeBegin != 0 && sizeEnd != 0 && color == 0 && clarify == null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeCaratColorClarify(priceEnd, priceBegin, sizeEnd, sizeBegin, shape);
        } else if(priceBegin != 0 && priceEnd != 0 && caratBegin == 0 && caratEnd == 0 && sizeBegin != 0 && sizeEnd != 0 && color == 0 && clarify != null && shape == null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeCaratColorShape(priceEnd, priceBegin, sizeEnd, sizeBegin, clarify);
        } else if(priceBegin != 0 && priceEnd != 0 && caratBegin == 0 && caratEnd == 0 && sizeBegin != 0 && sizeEnd != 0 && color != 0 && clarify == null && shape == null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeCaratClarifyShape(priceEnd, priceBegin, sizeEnd, sizeBegin, color);
        } else if(priceBegin != 0 && priceEnd != 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin == 0 && sizeEnd == 0 && color != 0 && clarify == null && shape == null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeSizeClarifyShape(priceEnd, priceBegin, caratEnd, caratBegin, color);
        } else if(priceBegin == 0 && priceEnd == 0 && caratBegin == 0 && caratEnd == 0 && sizeBegin != 0 && sizeEnd != 0 && color == 0 && clarify == null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeTotalPriceClarifyCaratColor(sizeEnd, sizeBegin, shape);
        } else if(priceBegin == 0 && priceEnd == 0 && caratBegin == 0 && caratEnd == 0 && sizeBegin == 0 && sizeEnd == 0 && color != 0 && clarify != null && shape == null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeTotalPriceCaratSizeShape(color, clarify);
        } else if(priceBegin != 0 && priceEnd != 0 && caratBegin == 0 && caratEnd == 0 && sizeBegin != 0 && sizeEnd != 0 && color == 0 && clarify == null && shape == null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeCaratColorShapeClarify(priceEnd, priceBegin, sizeEnd, sizeBegin);
        } else if(priceBegin != 0 && priceEnd != 0 && caratBegin == 0 && caratEnd == 0 && sizeBegin == 0 && sizeEnd == 0 && color != 0 && clarify == null && shape == null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeCaratSizeShapeClarify(priceEnd, priceBegin, color);
        } else if(priceBegin == 0 && priceEnd == 0 && caratBegin == 0 && caratEnd == 0 && sizeBegin != 0 && sizeEnd != 0 && color != 0 && clarify == null && shape == null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeTotalPriceCaratShapeClarify(sizeEnd, sizeBegin, color);
        } else if(priceBegin == 0 && priceEnd == 0 && caratBegin != 0 && caratEnd != 0 && sizeBegin == 0 && sizeEnd == 0 && color != 0 && clarify == null && shape == null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeTotalPriceShapeShapeClarify(caratEnd, caratBegin, color);
        } else if(priceBegin == 0 && priceEnd == 0 && caratBegin == 0 && caratEnd == 0 && sizeBegin == 0 && sizeEnd == 0 && color != 0 && clarify == null && shape != null) {
            lists_diamond = diamondRepository.getDiamondBySearchAdvancedExcludeTotalPriceCaratSizeClarify(color, shape);
        } else lists_diamond = diamondRepository.findAll();


        if(StringUtils.hasText(searchAdvanceRequest.getOptionPrice())) {
            if(searchAdvanceRequest.getOptionPrice().toLowerCase().equals(EnumSortSearchAdvance.giá_từ_thấp_đến_cao.name().replaceAll("_", " "))) {
                Collections.sort(lists_diamond, new Comparator<Diamond>() {
                    @Override
                    public int compare(Diamond d1, Diamond d2) {
                        if(d1.getTotalPrice() < d2.getTotalPrice()) {
                            return 1;
                        }
                        return -1;
                    }
                });
            } else if(searchAdvanceRequest.getOptionPrice().toLowerCase().equals(EnumSortSearchAdvance.giá_từ_cao_đến_thấp.name().replaceAll("_", " "))) {
                Collections.sort(lists_diamond, new Comparator<Diamond>() {
                    @Override
                    public int compare(Diamond d1, Diamond d2) {
                        if(d1.getTotalPrice() > d2.getTotalPrice()) {
                            return 1;
                        }
                        return -1;
                    }
                });
            } else return lists_diamond;
        }
        return lists_diamond;
    }


    //


    @Override
    public List<Diamond> getByCondition(String shape, float dimensions) {
        return diamondRepository.getByCondition(shape, dimensions);
    }

    @Override
    public void deleteDiamonds(List<String> diamondIDs) {
        try {
            for (String diamondID : diamondIDs) {
                Diamond diamond = diamondRepository.getDiamondByDiamondID(diamondID);
                if(diamond != null) {
                    diamondRepository.save(diamond);
                } else {
                    throw new RuntimeException("Diamond " + diamondID + " not found");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Diamond saveDiamond(Diamond diamond) {
        return diamondRepository.save(diamond);
    }

    @Override
    public void updateStatus(String diamondId) {
        try {
            Diamond diamondExisting = diamondRepository.getDiamondByDiamondID(diamondId);
            if(diamondExisting == null) {
                throw new RuntimeException("Diamond " + diamondId + " not found");
            }
            diamondExisting.setStatus(true);
            diamondRepository.save(diamondExisting);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Diamond> getByListDimensions(List<Float> dimensions) {
        return diamondRepository.getByListDimensions(dimensions);
    }

    @Override
    public List<Diamond> getByDimension(float dimension) {
        return diamondRepository.getByDimension(dimension);
    }

    @Override
    public List<Diamond> getAllDiamondWithStatus() {
        return diamondRepository.getAllDiamond();
    }






//    @Override
//    public Page<ResponseProductDTO> findByCriteria(List<String> fields, List<String> values, Pageable pageable) {
//        Specification<Product> spec = Specification.where(null);
//
//        for (int i = 0; i < fields.size(); i++) {
//            String field = fields.get(i);
//            String value = values.get(i);
//            Specification<Product> newSpec = ProductSpecification.filterByField(field, value);
//            if (newSpec != null) {
//                spec = spec.and(newSpec);
//            }
//        }
//
//        Page<Product> products = productRepository.findAll(spec, pageable);
//        return products.map(productMapper::toResponseDTO);
//    }

}
