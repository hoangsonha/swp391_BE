package com.group6.swp391.requests;

import com.group6.swp391.pojos.Product;
import com.group6.swp391.pojos.Size;
import com.group6.swp391.pojos.Thumnail;
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
