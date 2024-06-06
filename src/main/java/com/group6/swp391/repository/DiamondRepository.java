package com.group6.swp391.repository;

import com.group6.swp391.model.Diamond;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiamondRepository extends JpaRepository<Diamond, String> {

    public Diamond getDiamondByDiamondID(String diamondID);

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.carat <= ?3 and d.carat >= ?4 and d.dimensions <= ?5 and d.dimensions >= ?6 and d.colorLevel = ?7 and d.clarify = ?8 and d.shape = ?9 order by d.totalPrice asc")
    public List<Diamond> getDiamondBySearchAdvancedByASC(double priceEnd, double priceBegin, float caratEnd, float caratBegin, float sizeEnd, float sizeBegin, char colorLevel, String clarify, String shape);

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.carat <= ?3 and d.carat >= ?4 and d.dimensions <= ?5 and d.dimensions >= ?6 and d.colorLevel = ?7 and d.clarify = ?8 and d.shape = ?9 order by d.totalPrice desc")
    public List<Diamond> getDiamondBySearchAdvancedByDESC(double priceEnd, double priceBegin, float caratEnd, float caratBegin, float sizeEnd, float sizeBegin, char colorLevel, String clarify, String shape);

    // situation all is not null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.carat <= ?3 and d.carat >= ?4 and d.dimensions <= ?5 and d.dimensions >= ?6 and d.colorLevel = ?7 and d.clarify = ?8 and d.shape = ?9")
    public List<Diamond> getDiamondBySearchAdvanced(double priceEnd, double priceBegin, float caratEnd, float caratBegin, float sizeEnd, float sizeBegin, char colorLevel, String clarify, String shape);

    // situation shape is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.carat <= ?3 and d.carat >= ?4 and d.dimensions <= ?5 and d.dimensions >= ?6 and d.colorLevel = ?7 and d.clarify = ?8")
    public List<Diamond> getDiamondBySearchAdvanced(double priceEnd, double priceBegin, float caratEnd, float caratBegin, float sizeEnd, float sizeBegin, char colorLevel, String clarify);

    // situation clarify is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.carat <= ?3 and d.carat >= ?4 and d.dimensions <= ?5 and d.dimensions >= ?6 and d.colorLevel = ?7 and d.shape = ?8")
    public List<Diamond> getDiamondBySearchAdvancedExcludeClarify(double priceEnd, double priceBegin, float caratEnd, float caratBegin, float sizeEnd, float sizeBegin, char colorLevel, String shape);

    // situation color is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.carat <= ?3 and d.carat >= ?4 and d.dimensions <= ?5 and d.dimensions >= ?6 and d.clarify = ?7 and d.shape = ?8")
    public List<Diamond> getDiamondBySearchAdvancedExcludeColor(double priceEnd, double priceBegin, float caratEnd, float caratBegin, float sizeEnd, float sizeBegin, String clarify, String shape);

    // situation dimensions is not null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.carat <= ?3 and d.carat >= ?4 and d.colorLevel = ?5 and d.clarify = ?6 and d.shape = ?7")
    public List<Diamond> getDiamondBySearchAdvancedExcludeDimensions(double priceEnd, double priceBegin, float caratEnd, float caratBegin, char colorLevel, String clarify, String shape);

    // situation carat is not null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.dimensions <= ?3 and d.dimensions >= ?4 and d.colorLevel = ?5 and d.clarify = ?6 and d.shape = ?7")
    public List<Diamond> getDiamondBySearchAdvancedExcludeCarat(double priceEnd, double priceBegin, float sizeEnd, float sizeBegin, char colorLevel, String clarify, String shape);

    // situation totalPrice is not null

    @Modifying
    @Query("select d from Diamond d where d.carat <= ?1 and d.carat >= ?2 and d.dimensions <= ?3 and d.dimensions >= ?4 and d.colorLevel = ?5 and d.clarify = ?6 and d.shape = ?7")
    public List<Diamond> getDiamondBySearchAdvancedExcludeTotalPrice(float caratEnd, float caratBegin, float sizeEnd, float sizeBegin, char colorLevel, String clarify, String shape);

    // situation shape and color is not null

//    select * from railway.diamond where carat <= 1.15 and carat >= 0.75;
//    select * from railway.diamond where dimensions <= 6 and dimensions >= 5.8;
//    select * from railway.diamond where color_level = 'E';
//    select * from railway.diamond where clarify = 'VS2';
//    select * from railway.diamond where shape = 'Round';
//    select * from railway.diamond where total_price <= 1000000000 and total_price >= 500000000;
//
//    select * from railway.diamond where total_price <= 1000000000 and total_price >= 500000000 and carat <= 1.15 and carat >= 0.75
//    and dimensions <= 6 and dimensions >= 5.8 and color_level = 'E' and clarify = 'VS2' and shape = 'Round';
}
