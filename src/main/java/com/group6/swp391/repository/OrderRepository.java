package com.group6.swp391.repository;

import com.group6.swp391.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Order getOrderByOrderID(int orderID);

    @Query("SELECT o FROM order o WHERE o.user.userID =:id ORDER BY o.orderDate DESC ")
    List<Order> findByUserUserID(@Param("id") int userID);

    @Query("SELECT o FROM order o WHERE o.status =:status ORDER BY o.orderDate DESC ")
    List<Order> findTopByOrderByOrderDateDesc(@Param("status") String status);

    List<Order> findByStatus(String status);

    @Query("SELECT o FROM order o WHERE o.user.userID =:userId AND o.status =:status")
    List<Order> findByUserAndStatus(@Param("userId") int userId, @Param("status") String status);

    //code dashBoard

    @Query("SELECT SUM(o.price) FROM order o " +
            "WHERE MONTH(o.orderDate) = :month AND YEAR(o.orderDate) = :year AND o.status = 'Đã giao'")
    Double findTotalRevenueInMonth(@Param("month") int month, @Param("year") int year);

    @Query("SELECT o FROM order o WHERE o.status =:status AND MONTH(o.orderDate) =:month")
    List<Order> findByStatusMonth(@Param("month") int month, @Param("status") String status);

    @Query("SELECT SUM(o.price) FROM order o WHERE o.orderDate >= :startDate AND o.orderDate < :endDate AND o.status = 'Đã giao'")
    Double totalRevenueInDate(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT o FROM order o WHERE o.status =:status AND o.orderDate >= :startDate AND o.orderDate <= :endDate")
    List<Order> getStatusIntdate(@Param("status") String status, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
