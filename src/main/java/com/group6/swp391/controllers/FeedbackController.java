package com.group6.swp391.controllers;

import com.group6.swp391.pojos.Feedback;
import com.group6.swp391.pojos.Order;
import com.group6.swp391.pojos.OrderDetail;
import com.group6.swp391.requests.FeedbackRequest;
import com.group6.swp391.responses.ObjectResponse;
import com.group6.swp391.responses.ProductFeedbackRespone;
import com.group6.swp391.services.FeedbackService;
import com.group6.swp391.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('STAFF') ")
    @GetMapping("/all_feedbacks")
    public ResponseEntity<ObjectResponse> getAllFeedbacks() {
        List<Feedback> feedbacks;
        try {
            feedbacks = feedbackService.getAllFeedbacks();
            if (feedbacks.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ObjectResponse("Failed", "Danh sách đánh giá rỗng", null));
            } else {
                return ResponseEntity.ok(new ObjectResponse("Success", "Lấy danh sách đánh giá thành công", feedbacks));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ObjectResponse("Failed", "Lấy danh sách đánh giá không thành công", null));
        }
    }

    // Method to get feedbacks by diamond ID
    // Accepts a diamond ID as a path variable and returns a list of feedbacks or an error message
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('STAFF') ")
    @GetMapping("/feedbacks_by_diamond/{id}")
    public ResponseEntity<ObjectResponse> getFeedbacksByDiamondID(@PathVariable String id) {
        List<Feedback> feedbacks;
        try {
            feedbacks = feedbackService.getFeedbacksByDiamondID(id);
            if (feedbacks.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ObjectResponse("Failed", "Không tìm thấy đánh giá nào với diamondID: " + id, null));
            } else {
                return ResponseEntity.ok(new ObjectResponse("Success", "Lấy danh sách đánh giá thành công với diamondID: " + id, feedbacks));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ObjectResponse("Failed", "Lấy danh sách đánh giá không thành công với diamondID: " + id, null));
        }
    }

    // Method to get feedbacks by user ID
    // Accepts a user ID as a path variable and returns a list of feedbacks or an error message
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('STAFF') ")
    @GetMapping("/feedbacks_by_user/{id}")
    public ResponseEntity<ObjectResponse> getFeedbacksByUserID(@PathVariable int id) {
        List<Feedback> feedbacks;
        try {
            feedbacks = feedbackService.getFeedbacksByUserID(id);
            if (feedbacks.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ObjectResponse("Failed", "Không tìm thấy đánh giá nào với user ID: " + id, null));
            } else {
                return ResponseEntity.ok(new ObjectResponse("Success", "Lấy danh sách đánh giá thành công với user ID: " + id, feedbacks));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ObjectResponse("Failed", "Lấy danh sách đánh giá không thành công với user ID: " + id, null));
        }
    }

    // Method to get feedbacks by product ID
    // Accepts a product ID as a path variable and returns a list of feedbacks or an error message
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('STAFF')")
    @GetMapping("/feedbacks_by_product/{id}")
    public ResponseEntity<ObjectResponse> getFeedbacksByProductID(@PathVariable String id) {
        List<Feedback> feedbacks;
        try {
            feedbacks = feedbackService.getFeedbacksByProductID(id);
            if (feedbacks.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ObjectResponse("Failed", "Không tìm thấy đánh giá nào với product ID: " + id, null));
            } else {
                return ResponseEntity.ok(new ObjectResponse("Success", "Lấy danh sách đánh giá thành công với product ID: " + id, feedbacks));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ObjectResponse("Failed", "Lấy danh sách đánh giá không thành công với product ID: " + id, null));
        }
    }



    // Method to get the newest feedbacks
    // Accepts an optional limit as a request parameter (default is 10) and returns the latest feedbacks
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('STAFF') ")
    @GetMapping("/newest_feedbacks")
    public ResponseEntity<ObjectResponse> getNewestFeedbacks(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<Feedback> feedbacks = feedbackService.getNewestFeedbacks(limit);
            if (feedbacks.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Failed", "Không tìm thấy đánh giá nào", null));
            }
            return ResponseEntity.ok(new ObjectResponse("Success", "Lấy danh sách đánh giá mới nhất thành công", feedbacks));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ObjectResponse("Failed", "Lấy danh sách đánh giá mới nhất không thành công", null));
        }
    }

    // Method to delete a feedback by ID
    // Accepts a feedback ID as a path variable and returns a success message or an error message
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('STAFF') ")
    @DeleteMapping("/delete_feedback/{id}")
    public ResponseEntity<ObjectResponse> deleteFeedback(@PathVariable int id) {
        try {
            boolean isDeleted = feedbackService.deleteFeedback(id);
            if (isDeleted) {
                return ResponseEntity.ok(new ObjectResponse("Success", "Xóa đánh giá thành công", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ObjectResponse("Failed", "Không tìm thấy đánh giá với ID: " + id, null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ObjectResponse("Failed", "Xóa đánh giá không thành công", null));
        }
    }

    // Method to submit new feedback
    // Accepts a list of FeedbackRequest objects in the request body and
    // returns the saved feedbacks or an error message
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('STAFF') ")
    @PostMapping("/submit_feedback")
    public ResponseEntity<ObjectResponse> submitFeedback(@RequestBody @Valid List<FeedbackRequest> feedbackRequests) {
        try {
            List<Feedback> feedbacks = feedbackService.saveFeedback(feedbackRequests);
            if (!feedbacks.isEmpty()) {
                return ResponseEntity.ok(new ObjectResponse("Success", "Lưu đánh giá thành công", feedbacks));
            } else {
                return ResponseEntity.badRequest()
                        .body(new ObjectResponse("Failed", "Lưu đánh giá không thành công: ID sản phẩm hoặc người dùng không hợp lệ", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ObjectResponse("Failed", "Lưu đánh giá không thành công", null));
        }
    }

    // Method to get the average rating for a product by product ID
    // Accepts a product ID as a path variable and returns the average rating or an error message
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('STAFF') ")
    @GetMapping("/average_rating_product/{productID}")
    public ResponseEntity<ObjectResponse> getAverageRatingForProduct(@PathVariable String productID) {
        try {
            double averageRating = feedbackService.getAverageRatingForProduct(productID);
            return ResponseEntity.ok(new ObjectResponse("Success", "Lấy đánh giá trung bình thành công cho sản phẩm ID: " + productID, averageRating));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ObjectResponse("Failed", "Lấy đánh giá trung bình không thành công cho sản phẩm ID: " + productID, null));
        }
    }

    // Method to get the average rating for a diamond by diamond ID
    // Accepts a diamond ID as a path variable and returns the average rating or an error message
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('STAFF') ")
    @GetMapping("/average_rating_diamond/{diamondID}")
    public ResponseEntity<ObjectResponse> getAverageRatingForDiamond(@PathVariable String diamondID) {
        try {
            double averageRating = feedbackService.getAverageRatingForDiamond(diamondID);
            return ResponseEntity.ok(new ObjectResponse("Success", "Lấy đánh giá trung bình thành công cho kim cương ID: " + diamondID, averageRating));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ObjectResponse("Failed", "Lấy đánh giá trung bình không thành công cho kim cương ID: " + diamondID, null));
        }
    }


    /**
     * Method lay ra productcustomize voi trong mot order
     * danh gia vaof product
     * @param id orderId
     * @return list product
     */
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('STAFF') ")
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
