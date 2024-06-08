package com.group6.swp391.request;

import com.group6.swp391.model.Product;
import com.group6.swp391.model.Size;
import com.group6.swp391.model.Thumnail;
import lombok.*;


import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductRequest {
    private Product product;
    private List<Size> sizes;
    private List<Thumnail> productImages;
}
