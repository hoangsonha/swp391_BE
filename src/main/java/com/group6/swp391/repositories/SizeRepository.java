package com.group6.swp391.repositories;

import com.group6.swp391.pojos.Product;
import com.group6.swp391.pojos.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeRepository extends JpaRepository<Size, Integer> {

    @Query("SELECT s FROM Size s WHERE s.sizeValue =:value")
    Size findByValue(@Param("value") int value);

    Size findByProductAndSizeValue(Product product, int sizeValue);
}
