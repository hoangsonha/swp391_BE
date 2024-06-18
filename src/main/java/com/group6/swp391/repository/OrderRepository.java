package com.group6.swp391.repository;

import com.group6.swp391.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Order getOrderByOrderID(int orderID);
    List<Order> findByUserUserID(int userID);

}
