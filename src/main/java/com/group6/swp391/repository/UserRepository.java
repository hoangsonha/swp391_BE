package com.group6.swp391.repository;

import com.group6.swp391.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    @Query("update user set nonLocked = true where userID = ?1")
    public void unLocked(int userID);

    @Modifying
    @Query("update user set quantityLoginFailed = ?1 where email = ?2")
    public void setQuantityFailedLogin(int quantity, String email);

    @Modifying
    @Query("update user set password = ?1 where email = ?2")
    public void setPasswordByEmail(String password, String email);

    @Modifying
    @Query("update user set password = ?1 where phone = ?2")
    public void setPasswordByPhone(String password, String phone);

    @Modifying
    @Query("update user set nonLocked = false where email = ?1")
    public void lockedByEmail(String email);

    @Modifying
    @Query("update user set timeLoginFailed = ?1 where email = ?2")
    public void setTimeLoginFailed(Date date, String email);

    @Modifying
    @Query("update user set offlineAt = ?1 where email = ?2")
    public void setTimeOfflineAt(Date date, String email);
    
    public User getUserByPhone(String phone);

//    @Query("SELECT COUNT(*) AS new_users\n" +
//            "FROM user\n" +
//            "WHERE MONTH(create_at) = 6 AND YEAR(create_at) = 2024")
    @Query("SELECT COUNT(*) AS new_users FROM user u WHERE MONTH(u.createAt) = :month AND YEAR(u.createAt)=:year")
    Integer newuser(@Param("month") int month, @Param("year") int year);
}
