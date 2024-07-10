package com.group6.swp391.service;

import com.group6.swp391.model.CustomerInquiry;
import com.group6.swp391.repository.CustomerInquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Service
public class CustomerInquiryServiceImp implements CustomerInquiryService{
    @Autowired CustomerInquiryRepository customerInquiryRepository;

    @Override
    public CustomerInquiry createCustomerInquiry(CustomerInquiry customerInquiry) {
        return customerInquiryRepository.save(customerInquiry);
    }

    @Override
    public CustomerInquiry getCustomerInquiry(int customerInquiryId) {
        return customerInquiryRepository.findById(customerInquiryId);
    }

    @Override
    public List<CustomerInquiry> getNewConsulting() {
        return customerInquiryRepository.findByNew();
    }

    @Override
    public void updateStatus(int customerInquiryId) {
        CustomerInquiry ci = customerInquiryRepository.findById(customerInquiryId);
        if(ci == null) {
            throw new RuntimeException("Customer Inquiry Not Found");
        }
        ci.setStatus("Đã tư vấn");
        customerInquiryRepository.save(ci);
    }
}
