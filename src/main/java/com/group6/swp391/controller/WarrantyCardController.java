package com.group6.swp391.controller;

import com.group6.swp391.model.*;
import com.group6.swp391.request.WarrantyCardRequest;
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

    @PostMapping("/create_warrantyCard")
    public ResponseEntity<?> createWarrantyCard(@RequestBody WarrantyCardRequest warrantyCardRequest) {
        try {
            User user = userServiceImp.getUserByID(warrantyCardRequest.getUserId());
            if (user == null) {
                return ResponseEntity.badRequest().body("User does not exist!");
            }
            if (warrantyCardRequest.getExpirationDate() == null) {
                return ResponseEntity.badRequest().body("Expiration Date is required!");
            }
            List<String> idList = warrantyCardRequest.getObjectId();
            List<String> errors = new ArrayList<>();
            for (String itemId : idList) {
                WarrantyCard warrantyCard = new WarrantyCard();
                warrantyCard.setUser(user);
                warrantyCard.setExpirationDate(warrantyCardRequest.getExpirationDate());
                if (itemId.startsWith("P") || itemId.startsWith("p")) {
                    ProductCustomize productCustomize = productCustomizeServiceImp.getProductCustomizeById(itemId);
                    if (productCustomize == null) {
                        errors.add("ProductCustomize with ID " + itemId + " does not exist.");
                    } else {
                        warrantyCard.setProductCustomize(productCustomize);
                        warrantyCardServiceImp.createNew(warrantyCard);
                    }
                } else {
                    Diamond diamond = diamondServiceImp.getDiamondByDiamondID(itemId);
                    if (diamond == null) {
                        errors.add("Diamond with ID " + itemId + " does not exist.");
                    } else {
                        warrantyCard.setDiamond(diamond);
                        warrantyCardServiceImp.createNew(warrantyCard);
                    }
                }
            }
            if (!errors.isEmpty()) {
                return ResponseEntity.badRequest().body(String.join(", ", errors));
            }
            return ResponseEntity.ok().body("Warranty cards created successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating warranty cards: " + e.getMessage());
        }
    }

    @GetMapping("/all_warrantyCard")
    public ResponseEntity<List<WarrantyCard>> getAll() {
        try {
            List<WarrantyCard> warrantyCards = warrantyCardServiceImp.getAll();
            if (warrantyCards == null || warrantyCards.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(warrantyCards);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/warrantyCard_id/{warrantyCard_id}")
    public ResponseEntity<?> getById(@PathVariable("warrantyCard_id") int id) {
        WarrantyCard warrantyCard = null;
        try {
            warrantyCard = warrantyCardServiceImp.getById(id);
            if(warrantyCard == null) {
                return ResponseEntity.badRequest().body("Warranty Card not found");
            }
            return ResponseEntity.ok(warrantyCard);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<List<WarrantyCard>> getByUserId(@PathVariable("user_id") int id) {
        List<WarrantyCard> warrantyCards = null;
        try {
            warrantyCards = warrantyCardServiceImp.getByUser(id);
            if(warrantyCards == null) {
                return ResponseEntity.badRequest().body(null);
            }
            return ResponseEntity.ok(warrantyCards);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

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
