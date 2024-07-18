package com.group6.swp391.repository;

import com.group6.swp391.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    List<Feedback> findByDiamondDiamondID(String diamondID);

    List<Feedback> findByUserUserID(int userID);

    List<Feedback> findByProductProductID(String productID);

    @Query(value = "SELECT f FROM Feedback f ORDER BY f.createAt DESC")
    List<Feedback> findTopByOrderByCreateAtDesc(@Param("limit") int limit);
}
