package com.group6.swp391.controller;

import com.group6.swp391.model.Collection;
import com.group6.swp391.model.CollectionProduct;
import com.group6.swp391.model.CustomerInquiry;
import com.group6.swp391.model.User;
import com.group6.swp391.request.CustomerInquiryRequest;
import com.group6.swp391.response.CollectionProductRespone;
import com.group6.swp391.response.CustomerInquiryDetailRespone;
import com.group6.swp391.response.CustomerInquiryRespone;
import com.group6.swp391.response.ObjectResponse;
import com.group6.swp391.service.CollectionService;
import com.group6.swp391.service.CustomerInquiryService;
import com.group6.swp391.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/swp391/api/customer_inquiries")
public class CustomerInquiryController {
    @Autowired CustomerInquiryService customerInquiryService;
    @Autowired CollectionService collectionService;
    @Autowired UserService userService;

    @PostMapping("/create_customer_inquiry")
    public ResponseEntity<ObjectResponse> createCustomerInquiry(@RequestBody CustomerInquiryRequest customerInquiryRequest) {
        try {
            Collection collectionExisting = collectionService.getCollection(customerInquiryRequest.getCollectionid());
            if(collectionExisting == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Collection not exist", null));
            }
            CustomerInquiry customerInquiry = new CustomerInquiry();
            customerInquiry.setFullName(customerInquiryRequest.getFullName());
            customerInquiry.setEmail(customerInquiryRequest.getEmail());
            customerInquiry.setPhone(customerInquiryRequest.getPhone());
            customerInquiry.setAddress(customerInquiryRequest.getAddress());
            customerInquiry.setNote(customerInquiryRequest.getNote());
            customerInquiry.setStatus("Chờ tư vấn");
            customerInquiry.setCollection(collectionExisting);
            customerInquiryService.createCustomerInquiry(customerInquiry);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Customer Inquiry created successfully", customerInquiry));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Data exception", e.getMessage()));
        }
    }

    @GetMapping("/count_new_consulting")
    public ResponseEntity<ObjectResponse> countNewConsulting() {
        try {
            Integer count = 0;
            List<CustomerInquiry> allCustomerInquiry = customerInquiryService.getNewConsulting();
            if(allCustomerInquiry == null || allCustomerInquiry.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Customer Inquiry null", null));
            }
            count = allCustomerInquiry.size();
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Quantity Customer Inquiry", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Data exception", e.getMessage()));
        }
    }

    @GetMapping("/all_new_consulting")
    public ResponseEntity<ObjectResponse> getAllCustomerInquiry() {
        try {
            List<CustomerInquiry> allCustomerInquiry = customerInquiryService.getNewConsulting();
            if(allCustomerInquiry == null || allCustomerInquiry.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "List New Customer Inquiry null", null));
            }
            List<CustomerInquiryRespone> list = new ArrayList<>();
            for(CustomerInquiry customerInquiry : allCustomerInquiry) {
                CustomerInquiryRespone ci = new CustomerInquiryRespone();
                ci.setId(customerInquiry.getId());
                ci.setFullName(customerInquiry.getFullName());
                ci.setEmail(customerInquiry.getEmail());
                ci.setPhone(customerInquiry.getPhone());
                ci.setAddress(customerInquiry.getAddress());
                ci.setNote(customerInquiry.getNote());
                ci.setCollectionId(customerInquiry.getCollection().getCollecitonId());
                ci.setCollectionName(customerInquiry.getCollection().getCollectionName());
                ci.setConllectionImage(customerInquiry.getCollection().getThumnails().get(0).getImageUrl());
                list.add(ci);
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", " List new Customer Inquiry", list));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Data exception", e.getMessage()));
        }
    }

    @GetMapping("/get_detail/{customer_inquiry_id}")
    public ResponseEntity<ObjectResponse> getDetail(@PathVariable("customer_inquiry_id") int id) {
        try {
            CustomerInquiry customerInquiry = customerInquiryService.getCustomerInquiry(id);
            if(customerInquiry == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Success", "Customer Inquiry Do Not Exist", null));
            }
            CustomerInquiryDetailRespone cid = new CustomerInquiryDetailRespone();
            cid.setId(customerInquiry.getId());
            cid.setFullName(customerInquiry.getFullName());
            cid.setEmail(customerInquiry.getEmail());
            cid.setPhone(customerInquiry.getPhone());
            cid.setAddress(customerInquiry.getAddress());
            cid.setCollectionId(customerInquiry.getCollection().getCollecitonId());
            cid.setCollectionName(customerInquiry.getCollection().getCollectionName());
            cid.setCollectionImage(customerInquiry.getCollection().getThumnails().get(0).getImageUrl());
            List<CollectionProductRespone> collectionProductRespones = new ArrayList<>();
            for (CollectionProduct collectionProduct : customerInquiry.getCollection().getCollectionProduct()) {
                CollectionProductRespone cpr = new CollectionProductRespone();
                cpr.setProductId(collectionProduct.getProduct().getProductID());
                cpr.setProductName(collectionProduct.getProduct().getProductName());
                collectionProductRespones.add(cpr);
            }
            cid.setCollectionProductResponeList(collectionProductRespones);
            customerInquiryService.updateStatus(customerInquiry.getId());
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Customer Inquiry Detail", cid));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Data exception", e.getMessage()));
        }
    }

}
