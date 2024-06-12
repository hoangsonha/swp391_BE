package com.group6.swp391.controller;

import com.group6.swp391.model.*;
import com.group6.swp391.request.WarrantyCardRequest;
import com.group6.swp391.service.DiamondServiceImp;
import com.group6.swp391.service.ProductCustomizeServiceImp;
import com.group6.swp391.service.UserServiceImp;
import com.group6.swp391.service.WarrantyCardServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("swp391/api/warrantycards")
public class WarrantyCardController {
    @Autowired
    WarrantyCardServiceImp warrantyCardServiceImp;
    @Autowired
    ProductCustomizeServiceImp productCustomizeServiceImp;
    @Autowired
    DiamondServiceImp diamondServiceImp;
    @Autowired
    UserServiceImp userServiceImp;

    public ResponseEntity<?> createWarrantyCard(@RequestBody WarrantyCardRequest warrantyCardRequest) {
        try {
            WarrantyCard warrantyCard = new WarrantyCard();
            User user = userServiceImp.getUserByID(warrantyCardRequest.getUserId());
            if(user == null) {
                return ResponseEntity.badRequest().body("User is not null!");
            }
            warrantyCard.setUser(user);
            ProductCustomize productCustomize = productCustomizeServiceImp.getProductCustomizeById(warrantyCardRequest.getProductCustomizeId());
            Diamond diamond = diamondServiceImp.getDiamondByDiamondID(warrantyCardRequest.getDiamondId());
            if(productCustomize == null && diamond == null) {
                return ResponseEntity.badRequest().body("Diamond or ProductCustomize is not null");
            } else if(productCustomize == null && diamond != null) {
                warrantyCard.setDiamond(diamond);
            } else if(productCustomize != null && diamond == null) {
                warrantyCard.setProductCustomize(productCustomize);
            }
            if(warrantyCardRequest.getExpirationDate() == null) {
                return ResponseEntity.badRequest().body("Expiration Date is not null");
            }
            warrantyCard.setExpirationDate(warrantyCardRequest.getExpirationDate());
            warrantyCardServiceImp.createNew(warrantyCard);
            return ResponseEntity.ok().body("Create warranty card successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
