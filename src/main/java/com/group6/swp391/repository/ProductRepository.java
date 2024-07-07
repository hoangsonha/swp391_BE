package com.group6.swp391.repository;

import com.group6.swp391.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor {
    @Query("SELECT p FROM Product p WHERE p.productID =:id")
    Product findProductId(@Param("id") String id);

    @Query("SELECT p FROM Product p WHERE p.category.categoryName =:categoryName")
    List<Product> findByCategory(@Param("categoryName") String categoryName);

    @Query("SELECT p FROM Product p WHERE p.shapeDiamond = :shape AND ABS(p.dimensionsDiamond - :dimensions) < 0.1")
    List<Product> getByCondition(@Param("shape") String shapeDiamond,@Param("dimensions") float dimensionDiamond);

    @Query("SELECT p FROM Product p WHERE MONTH (p.createAt)=:month AND YEAR (p.createAt)=:year")
    List<Product> findByCreateInMOnth(@Param("month") int month, @Param("year") int year);
}
