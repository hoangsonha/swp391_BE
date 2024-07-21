package com.group6.swp391.repositories;

import com.group6.swp391.pojos.Points;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PointsRepository extends JpaRepository<Points, Integer> {
    @Query("SELECT po FROM Points po WHERE po.user.userID =:userId")
    Points findByUser(@Param("userId") int user);

    @Query("SELECT po FROM Points  po WHERE po.order.orderID =:orderId")
    Points findByOrderId(@Param("orderId") int orderId);
}
