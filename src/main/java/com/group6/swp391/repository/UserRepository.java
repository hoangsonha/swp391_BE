package com.group6.swp391.repository;

import com.group6.swp391.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {
    public User getUserByCodeVerify(String code);

    public User getUserByEmail(String email);

    public User getUserByUserID(int userID);

    @Modifying
    @Query("update user set enabled = true where userID = ?1")
    public void enabled(int userID);

    @Modifying
    @Query("update user set looked = false where userID = ?1")
    public void locked(int userID);

    

    public User getUserByPhone(String phone);
}
