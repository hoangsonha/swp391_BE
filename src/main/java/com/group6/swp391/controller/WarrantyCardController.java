package com.group6.swp391.controller;

import com.group6.swp391.model.*;
import com.group6.swp391.response.WarrantyCardRespone;
import com.group6.swp391.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/swp391/api/warrantycards")
@CrossOrigin(origins = "*")
public class WarrantyCardController {
    @Autowired
    WarrantyCardServiceImp warrantyCardServiceImp;
    @Autowired
    ProductCustomizeServiceImp productCustomizeServiceImp;
    @Autowired
    DiamondServiceImp diamondServiceImp;
    @Autowired
    UserServiceImp userServiceImp;

    @GetMapping("/search")
    public ResponseEntity<?> searchWarrantyCards(@RequestParam("query") String query) {
        try {
            List<WarrantyCard> warrantyCards = warrantyCardServiceImp.searchWarrantyCards(query);
            if (warrantyCards == null || warrantyCards.isEmpty()) {
                return ResponseEntity.ok().body("No warranty card found");
            }
            List<WarrantyCardRespone> warrantyCardRespones = new ArrayList<>();
            for (WarrantyCard warrantyCard: warrantyCards) {
                WarrantyCardRespone warrantyCardRespone = new WarrantyCardRespone();
                warrantyCardRespone.setWarrantyCardID(warrantyCard.getWarrantyCardID());
                if (warrantyCard.getProductCustomize() != null) {
                    warrantyCardRespone.setObjectId(warrantyCard.getProductCustomize().getProdcutCustomId());
                } else if (warrantyCard.getDiamond() != null) {
                    warrantyCardRespone.setObjectId(warrantyCard.getDiamond().getDiamondID());
                }
                warrantyCardRespone.setPurchaseDate(warrantyCard.getPurchaseDate());
                warrantyCardRespone.setExpirationDate(warrantyCard.getExpirationDate());
                warrantyCardRespones.add(warrantyCardRespone);
            }
            return ResponseEntity.ok(warrantyCardRespones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/all_warranty_card")
    public ResponseEntity<?> getAll() {
        try {
            List<WarrantyCard> warrantyCards = warrantyCardServiceImp.getAll();
            if (warrantyCards == null || warrantyCards.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            List<WarrantyCardRespone> warrantyCardRespones = new ArrayList<>();
            for (WarrantyCard warrantyCard : warrantyCards) {
                WarrantyCardRespone warrantyCardRespone = new WarrantyCardRespone();
                warrantyCardRespone.setWarrantyCardID(warrantyCard.getWarrantyCardID());
                if (warrantyCard.getProductCustomize() != null) {
                    warrantyCardRespone.setObjectId(warrantyCard.getProductCustomize().getProdcutCustomId());
                } else if (warrantyCard.getDiamond() != null) {
                    warrantyCardRespone.setObjectId(warrantyCard.getDiamond().getDiamondID());
                }
                warrantyCardRespone.setPurchaseDate(warrantyCard.getPurchaseDate());
                warrantyCardRespone.setExpirationDate(warrantyCard.getExpirationDate());
                warrantyCardRespones.add(warrantyCardRespone);
            }
            return ResponseEntity.ok(warrantyCardRespones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

//    @GetMapping("/user/{user_id}")
//    public ResponseEntity<?> getByUserId(@PathVariable("user_id") int id) {
//        try {
//            List<WarrantyCardRespone> warrantyCardRespones = new ArrayList<>();
//            List<WarrantyCard> warrantyCards = warrantyCardServiceImp.getByUser(id);
//            if(warrantyCards == null || warrantyCards.isEmpty()) {
//                return ResponseEntity.ok().body("Not warranty card");
//            }
//            for(WarrantyCard warrantyCard : warrantyCards) {
//                WarrantyCardRespone warrantyCardRespone = new WarrantyCardRespone();
//                warrantyCardRespone.setWarrantyCardID(warrantyCard.getWarrantyCardID());
//                if(warrantyCard.getProductCustomize() != null) {
//                    warrantyCardRespone.setObjectId(warrantyCard.getProductCustomize().getProdcutCustomId());
//                } else if(warrantyCard.getDiamond() != null) {
//                    warrantyCardRespone.setObjectId(warrantyCard.getDiamond().getDiamondID());
//                }
//                warrantyCardRespone.setPurchaseDate(warrantyCard.getPurchaseDate());
//                warrantyCardRespone.setExpirationDate(warrantyCard.getExpirationDate());
//                warrantyCardRespones.add(warrantyCardRespone);
//            }
//            return ResponseEntity.ok(warrantyCardRespones);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//

    @GetMapping("/getExpiringSoon")
    public ResponseEntity<List<WarrantyCard>> getExpiringSoon() {
        List<WarrantyCard> warrantyCards = null;
        try {
            warrantyCards = warrantyCardServiceImp.findWarrantyCardsExpiringSoon();
            if(warrantyCards == null) {
                return ResponseEntity.badRequest().body(null);
            }
            return ResponseEntity.ok(warrantyCards);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/warrantyCard_id/{warrantyCard_id}")
    public ResponseEntity<String> deleteWarrantyCard(@PathVariable("warrantyCard_id") int id) {
        WarrantyCard warrantyCard = null;
        try {
            warrantyCard = warrantyCardServiceImp.getById(id);
            if(warrantyCard == null) {
                return ResponseEntity.badRequest().body("WarrantyCard not found");
            }
            warrantyCardServiceImp.deleteWarrantyCard(id);
            return ResponseEntity.ok().body("Delete warrantyCard successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
