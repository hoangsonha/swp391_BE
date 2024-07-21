package com.group6.swp391.services;

import com.group6.swp391.pojos.Points;

public interface PointsService {
    Points createPoints(int userId, int orderId);
    Points getUserPoints(int userId, int orderId, double userPoint);
    Points getPointByUser(int userId);


}
