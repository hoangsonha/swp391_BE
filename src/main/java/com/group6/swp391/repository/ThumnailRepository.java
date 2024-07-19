package com.group6.swp391.repository;

import com.group6.swp391.model.Thumnail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThumnailRepository extends JpaRepository<Thumnail, Integer> {
    @Query("SELECT t FROM Thumnail t WHERE t.imageId=:id")
    Thumnail finThumnailById(@Param("id") int id);
}
