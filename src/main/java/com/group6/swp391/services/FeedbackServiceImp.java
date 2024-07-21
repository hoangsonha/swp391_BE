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
    public List<Feedback> getFeedbacksByDiamondID(String diamondID) {
        return feedbackRepository.findByDiamondDiamondID(diamondID);
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

                if (feedbackRequest.getDiamondID() !=  null) {
                    diamond = diamondRepository.findById(feedbackRequest.getDiamondID()).orElse(null);
                    if (diamond == null) {
                        continue;
                    }
                } else if (feedbackRequest.getProductID() != null) {
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
                        .diamond(diamond)
                        .product(product)
                        .user(user)
                        .build();

                Feedback savedFeedback = feedbackRepository.save(feedback);
                savedFeedbacks.add(savedFeedback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return savedFeedbacks;
    }

//    @Override
//    public Feedback saveFeedback(FeedbackRequest feedbackRequest) {
//        try {
//            Diamond diamond = null;
//            Product product = null;
//            Collection collection = null;
//
//            User user = userRepository.findById(feedbackRequest.getUserID()).orElse(null);
//            if (user == null) {
//                return null;
//            }
//
//            if (feedbackRequest.getDiamondID() != null) {
//                diamond = diamondRepository.findById(feedbackRequest.getDiamondID()).orElse(null);
//                if (diamond == null) {
//                    return null;
//                }
//            } else if (feedbackRequest.getProductID() != null) {
//                product = productRepository.findById(feedbackRequest.getProductID()).orElse(null);
//                if (product == null) {
//                    return null;
//                }
//            } else if (feedbackRequest.getCollectionID() != null) {
//                collection = collectionRepository.findById(feedbackRequest
//                        .getCollectionID()).orElse(null);
//                if (collection == null) {
//                    return null;
//                }
//            } else {
//                return null;
//            }
//
//            Feedback feedback = Feedback.builder()
//                    .comment(feedbackRequest.getComment())
//                    .rating(feedbackRequest.getRating())
//                    .diamond(diamond)
//                    .product(product)
//                    .collection(collection)
//                    .user(user)
//                    .build();
//
//            return feedbackRepository.save(feedback);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

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

    @Override
    public double getAverageRatingForDiamond(String diamondID) {
        try {
            List<Feedback> feedbacks = feedbackRepository.findByDiamondDiamondID(diamondID);
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
