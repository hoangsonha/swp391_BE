package com.group6.swp391.controller;

import com.group6.swp391.model.Order;
import lombok.Getter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/swp391/api/delivery")
@CrossOrigin(origins = "*")
public class DeliveryController {

    @GetMapping("/order")
    public List<Order> getListsOrder() {
        List<Order> lists = new ArrayList<>();
        return lists;
    }
}
