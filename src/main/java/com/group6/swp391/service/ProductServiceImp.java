package com.group6.swp391.service;

import com.group6.swp391.model.Product;
import com.group6.swp391.model.ProductSize;
import com.group6.swp391.model.Size;
import com.group6.swp391.model.Thumnail;
import com.group6.swp391.repository.ProductRepository;
import com.group6.swp391.request.ProductRequest;
import com.group6.swp391.request.SizeRequest;
import com.group6.swp391.request.ThumbnailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImp  implements  ProductService{
    @Autowired
    ProductRepository productRepository;

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<ProductRequest> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductRequest> requests = new ArrayList<>();

        for (Product product : products) {
            ProductRequest request = new ProductRequest();
            request.setProductID(product.getProductID());
            request.setProductName(product.getProductName());
            request.setBrand(product.getBrand());
            request.setCategory(product.getCategory());
            request.setProductType(product.getProductType());
            request.setBathStone(product.getBathStone());
            request.setCutDiamond(product.getCutDiamond());
            request.setGoldWeight(product.getGoldWeight());
            request.setBrand(product.getBrand());
            request.setDimensionsDiamond(product.getDimensionsDiamond());
            request.setGoleType(product.getGoleType());
            request.setOldGold(product.getOldGold());
            request.setRating(product.getRating());
            request.setQuantityStonesOfDiamond(product.getQuantityStonesOfDiamond());
            request.setTotalPrice(product.getOriginalPrice() * (1+ product.getRatio()));
            request.setOriginalPrice(product.getOriginalPrice());

            List<ThumbnailRequest> thumbnailRequests = new ArrayList<>();
            for (Thumnail thumnail : product.getProductImages()) {
                 ThumbnailRequest thumbnailRequest = ThumbnailRequest.builder().thumbnailId(thumnail.getImageId())
                                                                                .imageUrl(thumnail.getImageUrl())
                                                                                .build();
                 thumbnailRequests.add(thumbnailRequest);
            }
            request.setImageUrls(thumbnailRequests);

            List<SizeRequest> sizes = new ArrayList<>();
            for (ProductSize productSize : product.getProductSizes()){
                Size size = productSize.getSize();
                SizeRequest sizeRequest = SizeRequest.builder().sizeId(size.getSizeID())
                                                                .sizeValue(size.getSizeValue())
                                                                .quantity(productSize.getQuantiy())
                                                                .build();
                sizes.add(sizeRequest);
            }
            request.setSizes(sizes);
            requests.add(request);
        }
        return requests;
    }

    @Override
    public Product getProductById(String productId) {
        return null;
    }
}
