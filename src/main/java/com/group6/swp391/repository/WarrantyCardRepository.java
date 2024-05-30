package com.group6.swp391.repository;

import com.group6.swp391.model.WarrantyCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarrantyCardRepository extends JpaRepository<WarrantyCard, Integer> {

    public WarrantyCard getWarrantyCardByWarrantyCardID(int warrantyCardID);

}
