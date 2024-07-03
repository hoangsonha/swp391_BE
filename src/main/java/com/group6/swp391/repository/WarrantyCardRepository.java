package com.group6.swp391.repository;

import com.group6.swp391.model.WarrantyCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WarrantyCardRepository extends JpaRepository<WarrantyCard, Integer> {
    @Query("SELECT wc FROM WarrantyCard wc WHERE wc.expirationDate BETWEEN :now AND :futureDate")
    List<WarrantyCard> findWarrantyCardsExpiringSoon(@Param("now") Date now, @Param("futureDate") Date futureDate);

    @Query("SELECT wc FROM  WarrantyCard wc WHERE wc.user.userID =:userId")
    List<WarrantyCard> findByUser(@Param("userId") int userId);

    @Query("SELECT wc FROM WarrantyCard wc " +
            "LEFT JOIN  wc.productCustomize pc " +
            "LEFT JOIN wc.diamond d " +
            "WHERE pc.prodcutCustomId = :productCustomizeID " +
            "OR d.diamondID = :diamondID")
    List<WarrantyCard> findByProductCustomize_ProdcutCustomIdOrDiamond_DiamondID(@Param("productCustomizeID") String productCustomizeId,
                                                                                 @Param("diamondID") String diamondId);

    @Query("SELECT wc FROM WarrantyCard wc " +
            "LEFT JOIN wc.productCustomize pc " +
            "LEFT JOIN wc.diamond d " +
            "WHERE pc.prodcutCustomId LIKE %:query% " +
            "OR d.diamondID LIKE %:query% ")
    List<WarrantyCard> searchWarrantyCards(@Param("query") String query);

    List<WarrantyCard> findByWarrantyCardID(int warrantyCardId);
}
