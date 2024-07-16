package com.group6.swp391.controller;

import com.group6.swp391.model.Feedback;
import com.group6.swp391.model.Order;
import com.group6.swp391.model.OrderDetail;
import com.group6.swp391.request.FeedbackRequest;
import com.group6.swp391.response.ObjectResponse;
import com.group6.swp391.response.ProductFeedbackRespone;
import com.group6.swp391.service.FeedbackService;
import com.group6.swp391.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/swp391/api/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired OrderService orderService;

    // Method to get all feedbacks
    // Returns a list of all feedbacks or an appropriate error message
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

    // Method to get feedbacks by diamond ID
    // Accepts a diamond ID as a path variable and returns a list of feedbacks or an error message
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

    // Method to get feedbacks by user ID
    // Accepts a user ID as a path variable and returns a list of feedbacks or an error message
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

    // Method to get feedbacks by product ID
    // Accepts a product ID as a path variable and returns a list of feedbacks or an error message
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

    // Method to get feedbacks by collection ID
    // Accepts a collection ID as a path variable and returns a list of feedbacks or an error message
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

    // Method to get the newest feedbacks
    // Accepts an optional limit as a request parameter (default is 10) and returns the latest feedbacks
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

    // Method to delete a feedback by ID
    // Accepts a feedback ID as a path variable and returns a success message or an error message
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

    // Method to submit new feedback
    // Accepts a list of FeedbackRequest objects in the request body and
    // returns the saved feedbacks or an error message
    @PostMapping("/submit_feedback")
    public ResponseEntity<?> submitFeedback(@RequestBody @Valid List<FeedbackRequest> feedbackRequests) {
        try {
            List<Feedback> feedbacks = feedbackService.saveFeedback(feedbackRequests);
            if (!feedbacks.isEmpty()) {
                return ResponseEntity.ok(feedbacks);
            } else {
                return ResponseEntity.badRequest()
                        .body("Error saving feedback: Invalid product, diamond, or user ID");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving feedback: " + e.getMessage());
        }
    }

    // Method to get the average rating for a product by product ID
    // Accepts a product ID as a path variable and returns the average rating or an error message
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

    // Method to get the average rating for a diamond by diamond ID
    // Accepts a diamond ID as a path variable and returns the average rating or an error message
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

    // Method to get the average rating for a collection by collection ID
    // Accepts a collection ID as a path variable and returns the average rating or an error message
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

    /**
     * Method lay ra productcustomize voi trong mot order
     * danh gia vaof product
     * @param args orderId
     * @return list product
     */
    @GetMapping("/get_model_detail/{order_id}")
    public ResponseEntity<ObjectResponse> getProductFeedback(@PathVariable("order_id") int id) {
        try {
            Order orderExisting = orderService.getOrderByOrderID(id);
            if(orderExisting == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Order do not exist", "null"));
            }
            if(!orderExisting.getStatus().equalsIgnoreCase("Đã giao")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Status of order invalid", "null"));
            }
            List<ProductFeedbackRespone> list = new ArrayList<>();
            for (OrderDetail orderDetail : orderExisting.getOrderDetails()) {
                ProductFeedbackRespone product = new ProductFeedbackRespone();
                if(orderDetail.getProductCustomize() != null) {
                    product.setProductID(orderDetail.getProductCustomize().getProduct().getProductID());
                    product.setProductName(orderDetail.getProductCustomize().getProduct().getProductName());
                    list.add(product);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","List product need feedback", list));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Data exception", e.getMessage()));
        }
    }
}
