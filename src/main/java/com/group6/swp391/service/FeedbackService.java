package com.group6.swp391.service;

import com.group6.swp391.model.Feedback;
import com.group6.swp391.request.FeedbackRequest;

import java.util.List;

public interface FeedbackService {
    List<Feedback> getAllFeedbacks();

    List<Feedback> getFeedbacksByDiamondID(String diamondID);

    List<Feedback> getFeedbacksByUserID(int userID);

    List<Feedback> getFeedbacksByProductID(String productID);

    List<Feedback> getFeedbacksByCollectionID(String collectionID);

    Feedback saveFeedback(FeedbackRequest feedbackRequest);

    List<Feedback> getNewestFeedbacks(int limit);
}
