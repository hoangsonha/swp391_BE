package com.group6.swp391.service;

import com.group6.swp391.model.CustomerInquiry;

import java.util.List;

public interface CustomerInquiryService {
    CustomerInquiry createCustomerInquiry(CustomerInquiry customerInquiry);
    CustomerInquiry getCustomerInquiry(int customerInquiryId);
    List<CustomerInquiry> getNewConsulting();
    void updateStatus(int customerInquiryId);
}
