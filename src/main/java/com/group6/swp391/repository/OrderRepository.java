package com.group6.swp391.repository;

import com.group6.swp391.model.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Order getOrderByOrderID(int orderID);

    List<Order> findByUserUserID(int userID);

    @Query("SELECT o FROM order o WHERE o.status =:status ORDER BY o.orderDate DESC ")
    List<Order> findTopByOrderByOrderDateDesc(@Param("status") String status);

    List<Order> findByStatus(String status);
}
