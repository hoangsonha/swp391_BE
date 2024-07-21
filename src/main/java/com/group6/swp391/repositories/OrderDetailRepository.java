package com.group6.swp391.repositories;

import com.group6.swp391.pojos.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findByDiamondDiamondID(String diamondID);

    List<OrderDetail> findByOrderOrderID(int orderID);


    @Query("SELECT SUM(od.price * od.quantity) " +
            "FROM OrderDetail od " +
            "WHERE od.diamond IS NOT NULL " +
            "AND MONTH(od.order.orderDate) = :month " +
            "AND YEAR(od.order.orderDate) = :year " +
            "AND od.order.status = 'Đã giao'")
    Double findTotalDiamondRevenueInMonth(@Param("month") int month, @Param("year") int year);

    @Query("SELECT SUM(od.price * od.quantity) " +
            "FROM OrderDetail od " +
            "WHERE od.productCustomize IS NOT NULL " +
            "AND MONTH(od.order.orderDate) = :month " +
            "AND YEAR(od.order.orderDate) = :year " +
            "AND od.order.status = 'Đã giao'")
    Double findTotalProductCustomizeRevenueInMonth(@Param("month") int month, @Param("year") int year);
}
