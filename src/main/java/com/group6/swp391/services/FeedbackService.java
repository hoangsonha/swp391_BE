package com.group6.swp391.services;

import com.group6.swp391.pojos.Feedback;
import com.group6.swp391.requests.FeedbackRequest;

import java.util.List;

public interface FeedbackService {

    List<Feedback> getAllFeedbacks();

    List<Feedback> getFeedbacksByDiamondID(String diamondID);

    List<Feedback> getFeedbacksByUserID(int userID);

    List<Feedback> getFeedbacksByProductID(String productID);

    boolean deleteFeedback(int feedbackID);

    List<Feedback> saveFeedback(List<FeedbackRequest> feedbackRequests);

    List<Feedback> getNewestFeedbacks(int limit);

    double getAverageRatingForProduct(String productID);

    double getAverageRatingForDiamond(String diamondID);
}
