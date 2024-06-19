package com.group6.swp391.service;

import com.group6.swp391.model.Points;

import java.util.List;

public interface PointsService {
    Points createPoints(int userId, int orderId, double usedPoint);

    List<Points> getAll();
    List<Points> getByUser(int userId);
    Points getById(int id);
    void deleteById(int id);
    void updatePoints(int userId, double point);
}
