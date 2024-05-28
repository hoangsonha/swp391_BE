package com.group6.swp391.repository;

import com.group6.swp391.model.Diamond;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiamondRepository extends JpaRepository<Diamond, Character> {

    @Query("SELECT d FROM Diamond d WHERE d.diamondName LIKE %:diamondName%")
    List<Diamond> getDiamondByName(@Param("diamondName") String dimondName);
}
