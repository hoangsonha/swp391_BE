package com.group6.swp391.repository;

import com.group6.swp391.model.Collection;
import com.group6.swp391.model.CollectionProduct;
import com.group6.swp391.model.Diamond;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, String> {
    @Query("SELECT c FROM Collection c WHERE c.collecitonId =:id")
    Collection getById(@Param("id") String id);

    @Query("SELECT collecitonId FROM Collection  ORDER BY collecitonId DESC LIMIT 1")
    String getLastCollection();
}
