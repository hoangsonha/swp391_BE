package com.group6.swp391.service;

import com.group6.swp391.model.Feedback;
import com.group6.swp391.request.FeedbackRequest;

import java.util.List;

public interface FeedbackService {
    List<Feedback> getAllFeedbacks();

    List<Feedback> getFeedbacksByDiamondID(String productID);

    List<Feedback> getFeedbacksByUserID(int userID);

    Feedback saveFeedback(FeedbackRequest feedbackRequest);
}
