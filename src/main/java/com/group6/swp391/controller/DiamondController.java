package com.group6.swp391.controller;

import com.group6.swp391.model.Diamond;
import com.group6.swp391.service.DiamondService;
import com.group6.swp391.service.DiamondServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/swp391/api/diamonds")
@CrossOrigin(origins = "*")
public class DiamondController {

    @Autowired
    DiamondService diamondService;
    @Autowired
    private DiamondServiceImp diamondServiceImp;

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

    @GetMapping("/diamond_id/{diamond_id}")
    public ResponseEntity<Diamond> getDiamondByDiamondID(@PathVariable("diamond_id") String diamondID) {
        try {
            Diamond existingDiamond = diamondServiceImp.getDiamondByDiamondID(diamondID);
            if( existingDiamond == null) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(existingDiamond);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/all_diamonds")
    public ResponseEntity<?> getAllDiamonds() {
        List<Diamond> diamonds;
        try {
            diamonds = diamondService.getAllDiamond();
            if (diamonds == null || diamonds.isEmpty()) {
                throw new RuntimeException("Diamond list is empty!");
            } else {
                return ResponseEntity.ok(diamonds);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Get all diamonds failed");
        }
    }

    @PutMapping("/update_diamond/{id}")
    public ResponseEntity<?> updateDiamond(@RequestBody Diamond diamond, @PathVariable String id) {
        try {
            Diamond existingDiamond = diamondService.getDiamondByDiamondID(id);
            if (existingDiamond == null) {
                return ResponseEntity.badRequest().body("Diamond not found with ID: " + id);
            }
            existingDiamond.setDiamondName(diamond.getDiamondName());
            existingDiamond.setOriginPrice(diamond.getOriginPrice());
            existingDiamond.setTotalPrice(diamond.getOriginPrice()*(1+ diamond.getRatio()));
            existingDiamond.setRatio(diamond.getRatio());
            diamondService.updateDiamond(existingDiamond);
            return ResponseEntity.ok("Diamond updated successfully with ID: " + existingDiamond.getDiamondID());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Update diamond failed" + e.getMessage());
        }
    }

//    @DeleteMapping("/delete_diamond/{id}")
//    public ResponseEntity<?> deleteDiamond(@PathVariable String id) {
//        try {
//            Diamond existingDiamond = diamondService.getDiamondByDiamondID(id);
//            if (existingDiamond == null) {
//                return ResponseEntity.badRequest().body("Diamond not found with ID: " + id);
//            }
//            diamondService.deleteDiamond(id);
//            return ResponseEntity.ok("Diamond deleted successfully with ID: " + id);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Delete diamond failed: " + e.getMessage());
//        }
//    }

    @DeleteMapping("/delete_diamond/{id}")
    public ResponseEntity<?> deleteDianmond(@PathVariable("id") String id) {
        try {
            diamondService.markDiamondAsDeleted(id);
            return ResponseEntity.ok("Deleted Diamond with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/get_condition")
    public ResponseEntity<?> getCondition (
            @RequestParam("shape") String shape,
            @RequestParam("dimensions") float dimensions) {
        List<Diamond> list = diamondServiceImp.getByCondition(shape, dimensions);

        if (list == null || list.isEmpty()) {
            return ResponseEntity.badRequest().body("Diamond list is empty");
        } else {
            return ResponseEntity.ok(list);
        }
    }

    @PostMapping("/delete_diamonds")
    public ResponseEntity<String> deleteDiamonds(@RequestBody List<String> diamondIds) {
        try {
            if (diamondIds == null || diamondIds.isEmpty()) {
                return ResponseEntity.badRequest().body("Diamond list is empty");
            }
            for (String diamondId : diamondIds) {
                Diamond diamond = diamondServiceImp.getDiamondByDiamondID(diamondId);
                if (diamond == null) {
                    return ResponseEntity.badRequest().body("Diamond not found with ID: " + diamondId);
                } else {
                    diamond.setStatus(false);
                }
            }
            diamondServiceImp.deleteDiamonds(diamondIds);
            return ResponseEntity.ok("Diamond deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Delete diamond failed");
        }
    }

}
