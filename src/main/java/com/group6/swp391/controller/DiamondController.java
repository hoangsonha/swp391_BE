package com.group6.swp391.controller;

import com.group6.swp391.model.Diamond;
import com.group6.swp391.service.DiamondService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/swp391/api/diamonds")
public class DiamondController {

    @Autowired
    DiamondService diamondService;

    @PostMapping("/create_diamond")
    public ResponseEntity<?> createDiamond(@RequestBody Diamond diamond) {
        Diamond newDiamond = null;
        try {
            if (diamond == null) {
                throw new Exception("Diamond is null");
            } else {
                if (diamondService.getDiamondByDiamondID(diamond.getDiamondID()) != null) {
                    throw new Exception("Diamond already exist");
                } else {

                    diamond.setStatus(true);
                    diamond.setTotalPrice(diamond.getOriginPrice() * (1 + diamond.getRatio()));
                    newDiamond = diamondService.creatDiamond(diamond);
                }
            }
            return ResponseEntity.ok("Diamond created successfully with id " + newDiamond.getDiamondID());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Create diamond failed");
        }
    }

    @GetMapping("/all_diamonds")
    public ResponseEntity<?> getAllDiamonds() {
        List<Diamond> diamonds;
        try {
            diamonds = diamondService.getAllDiamond();
            if (diamonds == null) {
                throw new RuntimeException("Diamond list is empty!");
            } else {
                return ResponseEntity.ok(diamonds);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Get all diamonds failed");
        }
    }

    @PutMapping("/update_diamonds/{id}")
    public ResponseEntity<?> updateDiamond(@RequestBody Diamond diamond, @PathVariable String id) {
        try {
            Diamond existingDiamond = diamondService.getDiamondByDiamondID(id);
            if (existingDiamond == null) {
                return ResponseEntity.badRequest().body("Diamond not found with ID: " + id);
            }
            existingDiamond.setDiamondName(diamond.getDiamondName());
            existingDiamond.setCarat(diamond.getCarat());
            existingDiamond.setCertificateNumber(diamond.getCertificateNumber());
            existingDiamond.setClarify(diamond.getClarify());
            existingDiamond.setColor(diamond.getColor());
            existingDiamond.setColorLevel(diamond.getColorLevel());
            existingDiamond.setCut(diamond.getCut());
            existingDiamond.setShape(diamond.getShape());
            existingDiamond.setDimensions(diamond.getDimensions());
            existingDiamond.setFlourescence(diamond.getFlourescence());
            existingDiamond.setImage(diamond.getImage());
            existingDiamond.setInputDate(diamond.getInputDate());
            existingDiamond.setOriginPrice(diamond.getOriginPrice());
            existingDiamond.setStatus(diamond.isStatus());
            existingDiamond.setTotalPrice(diamond.getTotalPrice());
            existingDiamond.setWarrantyCard(diamond.getWarrantyCard());
            existingDiamond.setRatio(diamond.getRatio());
            diamondService.updateDiamond(existingDiamond);
            return ResponseEntity.ok("Diamond updated successfully with ID: " + existingDiamond.getDiamondID());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Update diamond failed" + e.getMessage());
        }
    }


}
