package com.group6.swp391.controller;

import com.group6.swp391.model.Feedback;
import com.group6.swp391.request.FeedbackRequest;
import com.group6.swp391.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/swp391/api/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/all_feedbacks")
    public ResponseEntity<?> getAllFeedbacks() {
        List<Feedback> feedbacks;
        try {
            feedbacks = feedbackService.getAllFeedbacks();
            if (feedbacks.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Feedback list is empty");
            } else {
                return ResponseEntity.ok(feedbacks);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Get all feedbacks failed");
        }
    }

    @GetMapping("/feedbacks_by_diamond/{id}")
    public ResponseEntity<?> getFeedbacksByDiamondID(@PathVariable String id) {
        List<Feedback> feedbacks;
        try {
            feedbacks = feedbackService.getFeedbacksByDiamondID(id);
            if (feedbacks.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No feedback found for diamond ID: " + id);
            } else {
                return ResponseEntity.ok(feedbacks);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving feedback for diamond ID: " + id);
        }
    }

    @GetMapping("/feedbacks_by_user/{id}")
    public ResponseEntity<?> getFeedbacksByUserID(@PathVariable int id) {
        List<Feedback> feedbacks;
        try {
            feedbacks = feedbackService.getFeedbacksByUserID(id);
            if (feedbacks.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No feedback for user ID: " + id);
            } else {
                return ResponseEntity.ok(feedbacks);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving feedback for user ID: " + id);
        }
    }

    @GetMapping("/feedbacks_by_product/{id}")
    public ResponseEntity<?> getFeedbacksByProductID(@PathVariable String id) {
        List<Feedback> feedbacks;
        try {
            feedbacks = feedbackService.getFeedbacksByProductID(id);
            if (feedbacks.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No feedback for product ID: " + id);
            } else {
                return ResponseEntity.ok(feedbacks);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving feedback for product ID: " + id);
        }
    }

    @GetMapping("/feedbacks_by_collection/{id}")
    public ResponseEntity<?> getFeedbacksByCollectionID(@PathVariable String id) {
        List<Feedback> feedbacks;
        try {
            feedbacks = feedbackService.getFeedbacksByCollectionID(id);
            if (feedbacks.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No feedback for collection ID: " + id);
            } else {
                return ResponseEntity.ok(feedbacks);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving feedback for collection ID: " + id);
        }
    }

    @GetMapping("/newest_feedbacks")
    public ResponseEntity<?> getNewestFeedbacks(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<Feedback> feedbacks = feedbackService.getNewestFeedbacks(limit);
            if (feedbacks.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No feedbacks found");
            }
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving newest feedbacks: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete_feedback/{id}")
    public ResponseEntity<?> deleteFeedback(@PathVariable int id) {
        try {
            boolean isDeleted = feedbackService.deleteFeedback(id);
            if (isDeleted) {
                return ResponseEntity.ok("Feedback deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Feedback not found with ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting feedback: " + e.getMessage());
        }
    }

    @PostMapping("/submit_feedback")
    public ResponseEntity<?> submitFeedback(@RequestBody @Valid FeedbackRequest feedbackRequest) {
        try {
            Feedback feedback = feedbackService.saveFeedback(feedbackRequest);
            if (feedback != null) {
                return ResponseEntity.ok(feedback);
            } else {
                return ResponseEntity.badRequest()
                        .body("Error saving feedback: Invalid product, diamond, or user ID");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving feedback: " + e.getMessage());
        }
    }

    @GetMapping("/average_rating_product/{productID}")
    public ResponseEntity<?> getAverageRatingForProduct(@PathVariable String productID) {
        try {
            double averageRating = feedbackService.getAverageRatingForProduct(productID);
            return ResponseEntity.ok("Average rating for product ID " + productID
                    + " is: " + averageRating);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error calculating average rating for product ID: " + productID);
        }
    }

    @GetMapping("/average_rating_diamond/{diamondID}")
    public ResponseEntity<?> getAverageRatingForDiamond(@PathVariable String diamondID) {
        try {
            double averageRating = feedbackService.getAverageRatingForDiamond(diamondID);
            return ResponseEntity.ok("Average rating for product ID " + diamondID
                    + " is: " + averageRating);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error calculating average rating for product ID: " + diamondID);
        }
    }

    @GetMapping("/average_rating_collection/{collectionID}")
    public ResponseEntity<?> getAverageRatingForCollection(@PathVariable String collectionID) {
        try {
            double averageRating = feedbackService.getAverageRatingForCollection(collectionID);
            return ResponseEntity.ok("Average rating for product ID " + collectionID
                    + " is: " + averageRating);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error calculating average rating for product ID: " + collectionID);
        }
    }
}
