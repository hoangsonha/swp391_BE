package com.group6.swp391.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.group6.swp391.pojos.*;
import com.group6.swp391.responses.ObjectResponse;
import com.group6.swp391.responses.WarrantyCardDetailRespone;
import com.group6.swp391.responses.WarrantyCardRespone;
import com.group6.swp391.services.*;
import com.group6.swp391.view.Views;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/swp391/api/warrantycards")
@CrossOrigin(origins = "*")
public class WarrantyCardController {

    @Autowired private WarrantyCardService warrantyCardService;

    // Get a warranty card by its ID
    // Input: warrantyCard_id (int)
    // Output: WarrantyCardDetailRespone or error message
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/warrantyCard_id/{warrantyCard_id}")
    public ResponseEntity<ObjectResponse> getById(@PathVariable("warrantyCard_id") int id) {
        try {
            WarrantyCard wc = warrantyCardService.getById(id);
            if (wc == null) {
                return ResponseEntity.badRequest().body(new ObjectResponse("Failed", "Không tìm thấy thẻ bảo hành với id " + id, null));
            }
            WarrantyCardDetailRespone wcd = new WarrantyCardDetailRespone();
            wcd.setUserId(wc.getUserId());
            wcd.setFullName(wc.getOrder().getFullName());
            wcd.setEmail(wc.getOrder().getEmail());
            wcd.setAddress(wc.getOrder().getAddressShipping());
            wcd.setPhone(wc.getOrder().getPhoneShipping());
            wcd.setOrderId(wc.getOrder().getOrderID());
            wcd.setPurchaseDate(wc.getPurchaseDate());
            wcd.setExpirationDate(wc.getExpirationDate());
            if(wc.getProductCustomize() != null) {
                wcd.setObjectId(wc.getProductCustomize().getProdcutCustomId());
                wcd.setObjectType("ProductCustomize");
                wcd.setPrice(wc.getProductCustomize().getTotalPrice());
            } else if(wc.getDiamond() != null) {
                wcd.setObjectId(wc.getDiamond().getDiamondID());
                wcd.setObjectType("Diamond");
                wcd.setPrice(wc.getDiamond().getTotalPrice());
            }
            return ResponseEntity.ok(new ObjectResponse("Success", "Lấy thông tin thẻ bảo hành thành công", wcd));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ObjectResponse("Failed", "Lỗi hệ thống", e.getMessage()));
        }
    }

    // Searches for warranty cards based on a query
    @GetMapping("/search")
    @JsonView(Views.Public.class)
    public ResponseEntity<ObjectResponse> searchWarrantyCards(@RequestParam("query") String query) {
        try {
            Optional<WarrantyCard> optionalWarrantyCard = warrantyCardService.searchWarrantyCards(query);
            if (!optionalWarrantyCard.isPresent()) {
                return ResponseEntity.ok(new ObjectResponse("Failed", "Không tìm thấy thẻ bảo hành", null));
            }
            WarrantyCard warrantyCard = optionalWarrantyCard.get();
            WarrantyCardRespone warrantyCardRespone = new WarrantyCardRespone();
            warrantyCardRespone.setWarrantyCardID(warrantyCard.getWarrantyCardID());
            if (warrantyCard.getProductCustomize() != null) {
                warrantyCardRespone.setObjectId(warrantyCard.getProductCustomize().getProdcutCustomId());
            } else if (warrantyCard.getDiamond() != null) {
                warrantyCardRespone.setObjectId(warrantyCard.getDiamond().getDiamondID());
            }
            warrantyCardRespone.setPurchaseDate(warrantyCard.getPurchaseDate());
            warrantyCardRespone.setExpirationDate(warrantyCard.getExpirationDate());
            return ResponseEntity.ok(new ObjectResponse("Success", "Lấy thẻ bảo hành thành công", warrantyCardRespone));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ObjectResponse("Failed", "Lỗi hệ thống", e.getMessage()));
        }
    }

    // Retrieves all warranty cards
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/all_warranty_card")
    @JsonView(Views.Internal.class)
    public ResponseEntity<ObjectResponse> getAll() {
        try {
            List<WarrantyCard> warrantyCards = warrantyCardService.getAll();
            if (warrantyCards == null || warrantyCards.isEmpty()) {
                return ResponseEntity.ok(new ObjectResponse("Failed", "Không có thẻ bảo hành nào", null));
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
                warrantyCardRespone.setUserId(warrantyCard.getUserId());
                warrantyCardRespones.add(warrantyCardRespone);
            }
            return ResponseEntity.ok(new ObjectResponse("Success", "Lấy tất cả thẻ bảo hành thành công", warrantyCardRespones));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ObjectResponse("Failed", "Lỗi hệ thống", e.getMessage()));
        }
    }

    // Retrieves warranty cards by user ID
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/user/{user_id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<ObjectResponse> getByUserId(@PathVariable("user_id") int id) {
        try {
            List<WarrantyCardRespone> warrantyCardRespones = new ArrayList<>();
            List<WarrantyCard> warrantyCards = warrantyCardService.getByUser(id);
            if(warrantyCards == null || warrantyCards.isEmpty()) {
                return ResponseEntity.ok(new ObjectResponse("Success", "Không có thẻ bảo hành nào", null));
            }
            for(WarrantyCard warrantyCard : warrantyCards) {
                WarrantyCardRespone warrantyCardRespone = new WarrantyCardRespone();
                warrantyCardRespone.setWarrantyCardID(warrantyCard.getWarrantyCardID());
                if(warrantyCard.getProductCustomize() != null) {
                    warrantyCardRespone.setObjectId(warrantyCard.getProductCustomize().getProdcutCustomId());
                } else if(warrantyCard.getDiamond() != null) {
                    warrantyCardRespone.setObjectId(warrantyCard.getDiamond().getDiamondID());
                }
                warrantyCardRespone.setPurchaseDate(warrantyCard.getPurchaseDate());
                warrantyCardRespone.setExpirationDate(warrantyCard.getExpirationDate());
                warrantyCardRespones.add(warrantyCardRespone);
            }
            return ResponseEntity.ok(new ObjectResponse("Success", "Lấy thẻ bảo hành theo ID người dùng thành công", warrantyCardRespones));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ObjectResponse("Failed", "Lỗi hệ thống", e.getMessage()));
        }
    }

    // Retrieves warranty cards that are expiring soon
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/getExpiringSoon")
    public ResponseEntity<ObjectResponse> getExpiringSoon() {
        try {
            List<WarrantyCard> warrantyCards = warrantyCardService.findWarrantyCardsExpiringSoon();
            if(warrantyCards == null || warrantyCards.isEmpty()) {
                return ResponseEntity.ok(new ObjectResponse("Failed", "Không có thẻ bảo hành nào sắp hết hạn", null));
            }
            return ResponseEntity.ok(new ObjectResponse("Success", "Lấy thẻ bảo hành sắp hết hạn thành công", warrantyCards));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ObjectResponse("Failed", "Lỗi hệ thống", e.getMessage()));
        }
    }

    // Deletes a warranty card by its ID
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/warrantyCard_id/{warrantyCard_id}")
    public ResponseEntity<ObjectResponse> deleteWarrantyCard(@PathVariable("warrantyCard_id") int id) {
        try {
            WarrantyCard warrantyCard = warrantyCardService.getById(id);
            if(warrantyCard == null) {
                return ResponseEntity.badRequest().body(new ObjectResponse("Failed", "Không tìm thấy thẻ bảo hành", null));
            }
            warrantyCardService.deleteWarrantyCard(id);
            return ResponseEntity.ok(new ObjectResponse("Success", "Xóa thẻ bảo hành thành công", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ObjectResponse("Failed", "Lỗi hệ thống", e.getMessage()));
        }
    }


}
