package com.group6.swp391.service;

import com.group6.swp391.model.Points;

import java.util.List;

public interface PointsService {
    Points createPoints(int userId, int orderId);
    Points getUserPoints(int userId, int orderId, double userPoint);
    Points getPointByUser(int userId);


}
