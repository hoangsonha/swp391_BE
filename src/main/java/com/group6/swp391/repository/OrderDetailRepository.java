package com.group6.swp391.repository;

import com.group6.swp391.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findByDiamondDiamondID(String diamondID);

    List<OrderDetail> findByOrderOrderID(int orderID);

    @Query("SELECT SUM(od.price * od.quantity) " +
            "FROM OrderDetail od " +
            "WHERE od.diamond IS NOT NULL " +
            "AND od.order.orderDate BETWEEN :startDate AND :endDate " +
            "AND od.order.status = 'Đã giao'")
    Double findTotalDiamondRevenueInMonth(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(od.price * od.quantity) " +
            "FROM OrderDetail od " +
            "WHERE od.productCustomize IS NOT NULL " +
            "AND od.order.orderDate BETWEEN :startDate AND :endDate " +
            "AND od.order.status = 'Đã giao'")
    Double findTotalProductCustomizeRevenueInMonth(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
