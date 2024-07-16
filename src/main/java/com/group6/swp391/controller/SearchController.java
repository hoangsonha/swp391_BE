package com.group6.swp391.controller;

import com.group6.swp391.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/swp391/api/search")
@CrossOrigin(origins = "*")
public class SearchController {

    @Autowired
    private SearchService searchService;

    // Method to handle search requests
    // Accepts a query as a request parameter, decodes it, and returns search results
    @GetMapping
    public ResponseEntity<?> search(@RequestParam String query) {
        String decodedQuery = URLDecoder.decode(query, StandardCharsets.UTF_8);
        List<Object> results = searchService.search(decodedQuery);
        return ResponseEntity.ok(results);
    }

}
