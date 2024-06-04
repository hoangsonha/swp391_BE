package com.group6.swp391.repository;

import com.group6.swp391.model.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSizeReposity extends JpaRepository<ProductSize, Integer> {

    @Query("SELECT ps FROM ProductSize ps WHERE ps.product.productID =:productID")
    List<ProductSize> findByProduct(@Param("productID") String productId);

    @Query("SELECT ps FROM ProductSize ps WHERE ps.size.sizeID =:sizeID")
    List<ProductSize> findBySize(@Param("sizeID") int sizeId);

    @Query("SELECT ps FROM ProductSize ps WHERE ps.size.sizeID =:sizeID AND ps.product.productID =:productID")
    ProductSize getByPS(@Param("productID") String product, @Param("sizeID") int sizeID);
}
