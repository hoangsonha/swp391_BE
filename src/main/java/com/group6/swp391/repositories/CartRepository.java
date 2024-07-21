package com.group6.swp391.repositories;

import com.group6.swp391.pojos.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    @Query("SELECT c FROM Cart c WHERE c.user.userID =:userId")
    Cart findByUserId(@Param("userId") int userId);

}
