package com.group6.swp391.repository;

import com.group6.swp391.model.Diamond;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiamondRepository extends JpaRepository<Diamond, Character> {

    public Diamond getDiamondByDiamondID(String diamondID);
}
