package com.group6.swp391.repositories;

import com.group6.swp391.pojos.WarrantyCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface WarrantyCardRepository extends JpaRepository<WarrantyCard, Integer> {
    @Query("SELECT wc FROM WarrantyCard wc WHERE wc.expirationDate BETWEEN :now AND :futureDate")
    List<WarrantyCard> findWarrantyCardsExpiringSoon(@Param("now") Date now, @Param("futureDate") Date futureDate);

    @Query("SELECT wc FROM  WarrantyCard wc WHERE wc.user.userID =:userId")
    List<WarrantyCard> findByUser(@Param("userId") int userId);

//    @Query("SELECT wc FROM WarrantyCard wc " +
//            "LEFT JOIN wc.productCustomize pc " +
//            "LEFT JOIN wc.diamond d " +
//            "WHERE pc.prodcutCustomId LIKE %:query% " +
//            "OR d.diamondID LIKE %:query% ")
//    List<WarrantyCard> searchWarrantyCards(@Param("query") String query);

    @Query("SELECT wc FROM WarrantyCard wc " +
            "LEFT JOIN wc.productCustomize pc " +
            "LEFT JOIN wc.diamond d " +
            "WHERE CAST(wc.warrantyCardID AS string) LIKE %:query% " +
            "OR pc.prodcutCustomId LIKE %:query% " +
            "OR d.diamondID LIKE %:query% " +
            "ORDER BY CASE WHEN CAST(wc.warrantyCardID AS string) = :query THEN 1 " +
            "WHEN pc.prodcutCustomId LIKE %:query% THEN 2 " +
            "WHEN d.diamondID LIKE %:query% THEN 3 END")
    Optional<WarrantyCard> searchWarrantyCard(@Param("query") String query);

}
