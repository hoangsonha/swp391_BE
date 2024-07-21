package com.group6.swp391.specifications;

import com.group6.swp391.pojos.Diamond;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class DiamondSpecification {


//    public static Specification<Diamond> hasName(String name) {
//        return new Specification<Diamond>() {
//            @Override
//            public Predicate toPredicate(Root<Diamond> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                return criteriaBuilder.like(root.get("diamondName"), "%" + name + "%");
//            }
//        };
//    }
//
//    public static Specification<Diamond> hasColorLevel(String colorLevel) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("colorLevel"), colorLevel);
//    }


    public static Specification<Diamond> searchByField(String field, String value) {
        switch (field) {
            case "caratBegin":
                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("carat"), Float.parseFloat(value));
            case "caratEnd":
                return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("carat"), Float.parseFloat(value));
            case "priceBegin":
                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("totalPrice"), Double.parseDouble(value));
            case "priceEnd":
                return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("totalPrice"), Double.parseDouble(value));
            case "sizeBegin":
                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("dimensions"), Float.parseFloat(value));
            case "sizeEnd":
                return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("dimensions"), Float.parseFloat(value));
            case "colorLevel":
                return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("colorLevel"), value);
            case "clarify":
                return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("clarify"), value);
            case "shape":
                return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("shape"), value);
            default:
                return null;
        }
    }

}
