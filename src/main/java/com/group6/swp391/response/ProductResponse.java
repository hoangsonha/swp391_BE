package com.group6.swp391.response;

import com.group6.swp391.model.Product;
import com.group6.swp391.model.ProductSize;
import com.group6.swp391.model.Thumnail;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private Product product;
    private List<SizeRespone> sizes;
    private List<Thumnail> thumnails;
}
