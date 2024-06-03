package com.group6.swp391.controller;

import com.group6.swp391.cart.Cart;
import com.group6.swp391.model.Diamond;
import com.group6.swp391.model.Product;
import com.group6.swp391.service.DiamondService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/swp391/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired private ProductService productService;
    @Autowired private DiamondService diamondService;

    @GetMapping("/add_cart/{id}")
    public ResponseEntity<String> addCart(HttpSession session, @PathVariable String id) {
        int check_id = 0;
        Product product = null;
        Diamond diamond = null;
        if(id.startsWith("P") || id.startsWith("p")) {
            product = productService.getProductByProductID(id);
            check_id = 1;
        } else if(id.startsWith("D") || id.startsWith("d")) {
            diamond = diamondService.getDiamondByDiamondID(id);
            check_id = 2;
        }
        boolean check = false;
        Cart cart = (Cart) session.getAttribute("CART");
         if(cart == null) {
             cart = new Cart();
             if(check_id == 1) {
                 if(product != null) {
                     boolean add_cart = cart.add(product);
                     if(add_cart) {
                         session.setAttribute("CART", cart);
                         check = true;
                     }
                 }
             } else if(check_id == 2) {
                 if(diamond != null) {
                     boolean add_cart = cart.add(diamond);
                     if(add_cart) {
                         session.setAttribute("CART", cart);
                         check = true;
                     }
                 }
             }
         } else {
            if(check_id == 1) {
                if(product != null) {
                    boolean add_cart = cart.add(product);
                    if(add_cart) {
                        check = true;
                    }
                }
            } else if(check_id == 2) {
                if(diamond != null) {
                    boolean add_cart = cart.add(diamond);
                    if(add_cart) {
                        check = true;
                    }
                }
            }
         }
         return check ? ResponseEntity.status(HttpStatus.OK).body("Added cart successfully")
              : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Added cart failed");
    }

//    @GetMapping("/view_cart")
//    public ResponseEntity<CartResponse> viewCart(HttpSession session) {
//        Cart cart = (Cart) session.getAttribute("CART");
//        List<Diamond> listsDiamond = new ArrayList<>();
//        int totalQuantity = 0;
//        double totalPrice = 0;
//        if(cart != null) {
//            totalQuantity = cart.getTotalQuantity();
//            totalPrice = cart.getTotalPrice();
//            listsDiamond = cart.getTotalDiamond();
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(new CartResponse("Success", "View Cart successfully", cart, listsDiamond, totalQuantity, totalPrice));
//    }

    @GetMapping("/remove_diamond/{id}")
    public ResponseEntity<String> removeGoods(HttpSession session, @PathVariable String id) {
        Cart cart = (Cart) session.getAttribute("CART");
        String message = "";
        if(cart != null) {
            if(id.startsWith("P") || id.startsWith("p")) {
                Product product = productService.getProductByProductID(id);
                cart.remove(product);
                message = "Delete product successfully!!!";
            } else if(id.startsWith("D") || id.startsWith("d")) {
                Diamond diamond = diamondService.getDiamondByDiamondID(id);
                cart.remove(diamond);
                message = "Delete diamond successfully!!!";
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @GetMapping("/remove_diamond_selected_in_product/{productID}/{diamondID}")
    public ResponseEntity<String> removeDiamondSelectedInProduct(HttpSession session, @PathVariable("productID") String productID, @PathVariable("diamondID") String diamondID) {
        Product product = productService.getProductByProductID(productID);
        Cart cart = (Cart) session.getAttribute("CART");
        if(cart != null) {
            product.setDiamond(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Delete diamond successfully!!!");
    }

}
