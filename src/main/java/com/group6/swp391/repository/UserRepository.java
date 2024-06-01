package com.group6.swp391.repository;

import com.group6.swp391.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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
    @Query("update user set nonLocked = false where userID = ?1")
    public void locked(int userID);

    @Modifying
    @Query("update user set quantityLoginFailed = ?1 where email = ?2")
    public void setQuantityFailedLogin(int quantity, String email);

    @Modifying
    @Query("update user set nonLocked = false where email = ?1")
    public void lockedByEmail(String email);

    @Modifying
    @Query("update user set timeLoginFailed = ?1 where email = ?2")
    public void setTimeLoginFailed(Date date, String email);
    
    public User getUserByPhone(String phone);
}
