package com.group6.swp391.repository;

import com.group6.swp391.model.Diamond;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiamondRepository extends JpaRepository<Diamond, Integer> {
    public Diamond getDiamondByDiamondName(String diamondName);
}
