package com.group6.swp391.services;

import com.group6.swp391.pojos.*;
import com.group6.swp391.repositories.*;
import com.group6.swp391.requests.FeedbackRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public List<Feedback> getFeedbacksByUserID(int userID) {
        return feedbackRepository.findByUserUserID(userID);
    }

    @Override
    public List<Feedback> getFeedbacksByProductID(String productID) {
        return feedbackRepository.findByProductProductID(productID);
    }

    @Override
    public boolean deleteFeedback(int feedbackID) {
        try {
            if (feedbackRepository.existsById(feedbackID)) {
                feedbackRepository.deleteById(feedbackID);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Feedback> saveFeedback(List<FeedbackRequest> feedbackRequests) {
        List<Feedback> savedFeedbacks = new ArrayList<>();

        for (FeedbackRequest feedbackRequest : feedbackRequests) {
            try {
                Diamond diamond = null;
                Product product = null;

                User user = userRepository.findById(feedbackRequest.getUserID()).orElse(null);
                if (user == null) {
                    continue;
                }
                if (feedbackRequest.getProductID() != null) {
                    product = productRepository.findById(feedbackRequest.getProductID()).orElse(null);
                    if (product == null) {
                        continue;
                    }
                } else {
                    continue;
                }

                Feedback feedback = Feedback.builder()
                        .comment(feedbackRequest.getComment())
                        .rating(feedbackRequest.getRating())
                        .product(product)
                        .user(user)
                        .build();

                Feedback savedFeedback = feedbackRepository.save(feedback);
                savedFeedbacks.add(savedFeedback);

                if (product != null) {
                    updateProductAverageRating(product);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return savedFeedbacks;
    }

    private void updateProductAverageRating(Product product) {
        List<Feedback> productFeedbacks = feedbackRepository.findByProduct(product);
        double averageRating = productFeedbacks.stream()
                .mapToDouble(Feedback::getRating)
                .average()
                .orElse(0.0);
        product.setRating(averageRating);
        productRepository.save(product);
    }

    @Override
    public List<Feedback> getNewestFeedbacks(int limit) {
        return feedbackRepository.findTopByOrderByCreateAtDesc(limit);
    }

    @Override
    public double getAverageRatingForProduct(String productID) {
        try {
            List<Feedback> feedbacks = feedbackRepository.findByProductProductID(productID);
            if (feedbacks.isEmpty()) {
                return 0.0;
            }
            double totalRating = feedbacks.stream().mapToDouble(Feedback::getRating).sum();
            return totalRating / feedbacks.size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
