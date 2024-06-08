package com.group6.swp391.repository;

import com.group6.swp391.model.Thumnail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThumnailRepository extends JpaRepository<Thumnail, Integer> {
}
