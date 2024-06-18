package com.group6.swp391.service;

import com.group6.swp391.model.Diamond;
import com.group6.swp391.model.Feedback;
import com.group6.swp391.model.Product;
import com.group6.swp391.model.User;
import com.group6.swp391.repository.DiamondRepository;
import com.group6.swp391.repository.FeedbackRepository;
import com.group6.swp391.repository.ProductRepository;
import com.group6.swp391.repository.UserRepository;
import com.group6.swp391.request.FeedbackRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImp implements FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private DiamondRepository diamondRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    @Override
    public List<Feedback> getFeedbacksByDiamondID(String diamondID) {
        return feedbackRepository.findByDiamondDiamondID(diamondID);
    }

    @Override
    public List<Feedback> getFeedbacksByUserID(int userID) {
        return feedbackRepository.findByUserUserID(userID);
    }

    @Override
    public Feedback saveFeedback(FeedbackRequest feedbackRequest) {
        try {
            Diamond diamond = null;
            Product product = null;
            User user = userRepository.findById(feedbackRequest.getUserID()).orElse(null);
            if (user == null) {
                return null;
            }

            if (feedbackRequest.getDiamondID() != null) {
                diamond = diamondRepository.findById(feedbackRequest.getDiamondID()).orElse(null);
                if (diamond == null) {
                    return null;
                }
            } else if (feedbackRequest.getProductID() != null) {
                product = productRepository.findById(feedbackRequest.getProductID()).orElse(null);
                if (product == null) {
                    return null;
                }
            } else {
                return null;
            }

            Feedback feedback = Feedback.builder()
                    .comment(feedbackRequest.getComment())
                    .rating(feedbackRequest.getRating())
                    .diamond(diamond)
                    .product(product)
                    .user(user)
                    .build();

            return feedbackRepository.save(feedback);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
