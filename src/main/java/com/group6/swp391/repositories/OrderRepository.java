package com.group6.swp391.repositories;

import com.group6.swp391.pojos.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT o FROM order o WHERE o.orderID =:orderId")
    Order getOrderByOrderID(@Param("orderId") int orderID);

    @Query("SELECT o FROM order o WHERE o.user.userID =:id ORDER BY o.orderDate DESC ")
    List<Order> findByUserUserID(@Param("id") int userID);

    @Query("SELECT o FROM order o WHERE o.staffID.userID=:staffId AND o.status =:status ORDER BY o.orderDate DESC ")
    List<Order> findTopByOrderByOrderDateDesc(@Param("staffId") int staffId, @Param("status") String status);

    @Query("SELECT o FROM order o WHERE o.status =:status ORDER BY o.orderDate DESC ")
    List<Order> findTopByOrderByOrderDateDescv3(@Param("status") String status);

    @Query("SELECT o FROM order o WHERE o.deliveryID.userID=:deliveryId AND o.status =:status ORDER BY o.status DESC ")
    List<Order> findTopByOrderByOrderDateDescV2(@Param("deliveryId") int deliveryId, @Param("status") String status);

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

    @Query("SELECT o FROM order o WHERE  o.staffID.userID=:staffId")
    List<Order> findByStaff(@Param("staffId") int staffId);

    @Query("SELECT o FROM order o WHERE  o.deliveryID.userID=:deliveryId")
    List<Order> findByDelivery(@Param("deliveryId") int staffId);
}
