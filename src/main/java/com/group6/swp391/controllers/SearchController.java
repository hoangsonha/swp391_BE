package com.group6.swp391.controllers;

import com.group6.swp391.responses.ObjectResponse;
import com.group6.swp391.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'STAFF', 'DELIVERY')")
    public ResponseEntity<ObjectResponse> search(@RequestParam String query) {
        try {
            String decodedQuery = URLDecoder.decode(query, StandardCharsets.UTF_8);
            List<Object> results = searchService.search(decodedQuery);
            if (results.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ObjectResponse("Failed", "Không tìm thấy kết quả nào", null));
            } else {
                return ResponseEntity.ok(new ObjectResponse("Success", "Lấy kết quả tìm kiếm thành công", results));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ObjectResponse("Failed", "Lấy kết quả tìm kiếm không thành công", e.getMessage()));
        }
    }

}
