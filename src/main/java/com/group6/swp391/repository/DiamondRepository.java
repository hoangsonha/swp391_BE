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

    // @Query(value = "SELECT d FROM Diamond d WHERE d.shape =:shape AND d.dimensions LIKE :dimensions")
    // @Query("SELECT d FROM Diamond d WHERE d.shape = :shape AND CAST(d.dimensions AS string) LIKE CONCAT('%', :dimensions, '%')")
    @Query("SELECT d FROM Diamond d WHERE d.shape = :shape AND ABS(d.dimensions - :dimensions) < 0.1")
    List<Diamond> findBycondition (@Param("shape") String shape,@Param("dimensions") float dimensions);

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.carat <= ?3 and d.carat >= ?4 and d.dimensions <= ?5 and d.dimensions >= ?6 and d.colorLevel = ?7 and d.clarify = ?8 and d.shape = ?9")
    public List<Diamond> getDiamondBySearchAdvanced(double priceEnd, double priceBegin, float caratEnd, float caratBegin, float sizeEnd, float sizeBegin, char colorLevel, String clarify, String shape);


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
