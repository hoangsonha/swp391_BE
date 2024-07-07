package com.group6.swp391.service;

import com.group6.swp391.model.Diamond;
import com.group6.swp391.model.Product;
import com.group6.swp391.repository.DiamondRepository;
import com.group6.swp391.repository.ProductRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SearchServiceImp implements SearchService {

    @Autowired
    private DiamondRepository diamondRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Object> search(String query) {
        List<Object> results = new ArrayList<>();

        List<String> keywords = Stream.of(query.trim().toLowerCase().split("\\s+"))
                .filter(word -> !word.isEmpty())
                .collect(Collectors.toList());

        // Tìm kiếm diamonds
        Specification<Diamond> diamondSpecification = (root, query1, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (String keyword : keywords) {
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("diamondName")), "%" + keyword + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("diamondID")), "%" + keyword + "%")
                ));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        List<Diamond> diamonds = diamondRepository.findAll(diamondSpecification);
        results.addAll(diamonds);

        // Tìm kiếm products
        Specification<Product> productSpecification = (root, query1, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (String keyword : keywords) {
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("productName")), "%" + keyword + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("productID")), "%" + keyword + "%")
                ));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        List<Product> products = productRepository.findAll(productSpecification);
        results.addAll(products);

        return results;
    }


//    @Override
//    public List<Object> search(String query) {
//        List<Object> results = new ArrayList<>();
//
//        List<Diamond> diamonds = diamondRepository.searchByNameOrID(query);
//        results.addAll(diamonds);
//
//        List<Product> products = productRepository.searchByNameOrID(query);
//        results.addAll(products);
//
//        return results;
//    }

//    @Override
//    public List<Object> search(String query) {
//        List<Object> results = new ArrayList<>();
//
//        List<Diamond> diamondsByName = diamondRepository.findByDiamondNameContaining(query);
//        List<Diamond> diamondsByID = diamondRepository.findByDiamondIDContaining(query);
//
//        for (Diamond diamond : diamondsByID) {
//            if (!diamondsByName.contains(diamond)) {
//                diamondsByName.add(diamond);
//            }
//        }
//        results.addAll(diamondsByName);
//
//        List<Product> products = productRepository.findByProductNameContaining(query);
//        results.addAll(products);
//
//        return results;
//    }
}
