package com.group6.swp391.repository;

import com.group6.swp391.model.Diamond;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
