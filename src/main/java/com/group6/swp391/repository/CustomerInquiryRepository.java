package com.group6.swp391.repository;

import com.group6.swp391.model.CustomerInquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerInquiryRepository extends JpaRepository<CustomerInquiry, Integer> {

    @Query("SELECT ci FROM CustomerInquiry ci WHERE ci.status='Chờ tư vấn' ORDER BY ci.createAt DESC ")
    List<CustomerInquiry> findByNew();

    CustomerInquiry findById(int id);
}
