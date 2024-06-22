package com.group6.swp391.repository;

import com.group6.swp391.model.Cart;
import com.group6.swp391.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

   // @Query("SELECT cI FROM CartItem  cI WHERE cI.productCustomize.prodcutCustomId =:ID OR cI.diamondAdd.diamondID =:ID")
   @Query("SELECT CASE WHEN COUNT(cI) > 0 THEN true ELSE false END FROM CartItem cI JOIN Cart c ON  cI.cart.cartId = c.cartId WHERE c.user.userID = :userID AND (cI.productCustomize.prodcutCustomId = :itemID OR cI.diamondAdd.diamondID = :itemID)")
   boolean existsByUserIdAndProductCustomizeIdOrDiamondId(@Param("userID") int userId, @Param("itemID") String itemId);

   List<CartItem> findByCartCartId(int cartID);

   @Query("SELECT ci FROM CartItem ci WHERE ci.cart.cartId =:cartId")
   List<CartItem> findByCart(@Param("cartId") int cartId);
}
