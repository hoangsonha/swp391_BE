package com.group6.swp391.repository;

import com.group6.swp391.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Order getOrderByOrderID(int orderID);

    List<Order> findByUserUserID(int userID);

    @Query("SELECT o FROM order o WHERE o.status =:status ORDER BY o.orderDate DESC ")
    List<Order> findTopByOrderByOrderDateDesc(@Param("status") String status);

    List<Order> findByStatus(String status);

    @Query("SELECT o FROM order o WHERE o.user.userID =:userId AND o.status =:status")
    List<Order> findByUserAndStatus(@Param("userId") int userId, @Param("status") String status);

    @Query("SELECT SUM(o.price) FROM order o WHERE o.orderDate BETWEEN :startDate AND :endDate AND o.status ='Đã giao'")
    Double findTotalRevenueInMonth(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(o.price) FROM order o WHERE o.orderDate = :date AND o.status = 'Đã giao'")
    Double findTotalRevenueInDay(@Param("date") LocalDate date);

    @Query("SELECT o FROM order o WHERE o.orderDate BETWEEN :startDate AND :endDate AND o.status =:status")
    List<Order> findStatusInMonth(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("status") String status);

    @Query("SELECT o FROM order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    List<Order> findOrdersByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(o) FROM order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    Long countOrdersByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
