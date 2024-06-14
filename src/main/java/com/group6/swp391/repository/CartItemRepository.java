package com.group6.swp391.repository;

import com.group6.swp391.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    @Query("SELECT cI FROM CartItem  cI WHERE cI.productCustomize.prodcutCustomId =:ID OR cI.diamond.diamondID =:ID")
    CartItem findByProductId(@Param(("ID")) String productId);
}
