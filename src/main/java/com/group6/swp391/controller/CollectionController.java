package com.group6.swp391.controller;

import com.group6.swp391.model.Collection;
import com.group6.swp391.model.CollectionProduct;
import com.group6.swp391.model.Product;
import com.group6.swp391.model.Thumnail;
import com.group6.swp391.request.CollectionRequest;
import com.group6.swp391.request.ThumnailRequest;
import com.group6.swp391.response.AllColelctionRespone;
import com.group6.swp391.response.CollectionDetailRespone;
import com.group6.swp391.response.CollectionProductRespone;
import com.group6.swp391.response.ObjectResponse;
import com.group6.swp391.service.CollectionProductService;
import com.group6.swp391.service.CollectionService;
import com.group6.swp391.service.ProductService;
import com.group6.swp391.service.ThumnailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/swp391/api/collections")
@CrossOrigin(origins = "*")
public class CollectionController {
    @Autowired CollectionService collectionService;
    @Autowired CollectionProductService collectionProductService;
    @Autowired ThumnailService thumnailService;
    @Autowired ProductService productService;

    @PostMapping("/create_collection")
    public ResponseEntity<?> createCollection(@RequestBody CollectionRequest collectionRequest) {
        try {
            Collection newCollection = new Collection();
            if(collectionService.getCollection(collectionRequest.getCollectionId()) != null) {
                return ResponseEntity.badRequest().body("Collection already exists");
            }
            newCollection.setCollecitonId(collectionRequest.getCollectionId());
            newCollection.setCollectionTitle(collectionRequest.getCollectionTitle());
            newCollection.setCollectionName(collectionRequest.getCollectionName());
            newCollection.setGemStone(collectionRequest.getGemStone());
            newCollection.setGoldOld(collectionRequest.getGoldOld());
            newCollection.setGoldType(collectionRequest.getGoldType());
            newCollection.setStatus(true);
            newCollection.setPrice(collectionRequest.getPrice());
            collectionService.createCollection(newCollection);
            List<Thumnail> thumnailList = new ArrayList<>();
            for(Thumnail thumnail  : collectionRequest.getThumnails()) {
                Thumnail newThumnail = new Thumnail();
                newThumnail.setImageUrl(thumnail.getImageUrl());
                newThumnail.setCollection(newCollection);
                thumnailList.add(newThumnail);
                thumnailService.createThumnailV2(newCollection.getCollecitonId(), newThumnail.getImageUrl());
            }
            newCollection.setThumnails(thumnailList);
            List<CollectionProduct> collectionProducts = new ArrayList<>();
            for(String productId : collectionRequest.getProductId()) {
                CollectionProduct newCollectionProduct = new CollectionProduct();
                newCollectionProduct.setCollection(newCollection);
                Product productExisting = productService.getProductById(productId);
                if(productExisting == null) {
                    return ResponseEntity.badRequest().body("product not found");
                }
                newCollectionProduct.setProduct(productExisting);
                newCollectionProduct.setDiamond(null);
                collectionProducts.add(newCollectionProduct);
                collectionProductService.createCollectionProduct(newCollectionProduct);
            }
            newCollection.setCollectionProduct(collectionProducts);
            return ResponseEntity.ok("Create Collection Successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getall_collection")
    public ResponseEntity<ObjectResponse> getAllCollection() {
        try {
            List<Collection> collections = collectionService.getAllCollections();
            if(collections.isEmpty() || collections == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "no data", null));
            }
            List<AllColelctionRespone> allColelctionResponeList = new ArrayList<>();
            for (Collection collection : collections) {
                AllColelctionRespone colelctionRespone = new AllColelctionRespone();
                colelctionRespone.setCollectionId(collection.getCollecitonId());
                colelctionRespone.setCollectionTitle(collection.getCollectionTitle());
                colelctionRespone.setPrice(collection.getPrice());
                colelctionRespone.setThumnail(collection.getThumnails().get(0));
                allColelctionResponeList.add(colelctionRespone);
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "List Collection", allColelctionResponeList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "no data", e.getMessage()));
        }
    }

    @GetMapping("/collection_detail/{collection_id}")
    public ResponseEntity<ObjectResponse> collectionDetail(@PathVariable("collection_id") String id) {
        try {
            Collection collectionExisting = collectionService.getCollection(id);
            if(collectionExisting == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "no data", null));
            }
            CollectionDetailRespone collectionDetailRespone = new CollectionDetailRespone();
            collectionDetailRespone.setCollectionId(collectionExisting.getCollecitonId());
            collectionDetailRespone.setCollectionTitle(collectionExisting.getCollectionTitle());
            collectionDetailRespone.setPrice(collectionExisting.getPrice());
            collectionDetailRespone.setCollectionName(collectionExisting.getCollectionName());
            collectionDetailRespone.setGemStone(collectionExisting.getGemStone());
            collectionDetailRespone.setGoldOld(collectionExisting.getGoldOld());
            collectionDetailRespone.setGoldType(collectionExisting.getGoldType());
            collectionDetailRespone.setStatus(collectionExisting.isStatus());
            List<CollectionProductRespone> collectionProductResponeList = new ArrayList<>();
            List<Float> sizeDiamond = new ArrayList<>();
            for (CollectionProduct collectionProduct : collectionExisting.getCollectionProduct()) {
                CollectionProductRespone collectionProductRespone = new CollectionProductRespone();
                collectionProductRespone.setProductId(collectionProduct.getProduct().getProductID());
                collectionProductRespone.setProductName(collectionProduct.getProduct().getProductName());
                Float size = collectionProduct.getProduct().getDimensionsDiamond();
                sizeDiamond.add(size);
                collectionProductResponeList.add(collectionProductRespone);
            }
            collectionDetailRespone.setCollectionProductRespones(collectionProductResponeList);
            collectionDetailRespone.setSizeDiamond(sizeDiamond);
            collectionDetailRespone.setThumnails(collectionExisting.getThumnails());
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Collection Detail Data", collectionDetailRespone));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "no data", e.getMessage()));
        }
    }

    @GetMapping("/{collection_id}")
    public ResponseEntity<ObjectResponse> getThumnai(@PathVariable("collection_id") String id) {
        try {
            List<Thumnail> thumnailCollection = thumnailService.getThumnailByObject(id);
            if(thumnailCollection.isEmpty() || thumnailCollection == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "no data", null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Thumnail collection with id: " + id, thumnailCollection));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "no data", e.getMessage()));
        }
    }

    @PostMapping("/update_thumail/{collection_id}")
    public ResponseEntity<?> updateThumnailCollection(@PathVariable("collection_id") String id, @RequestBody List<ThumnailRequest> thumnailRequests) {
        try {
            List<Thumnail> thumnailCollection = thumnailService.getThumnailByObject(id);
            if(thumnailCollection.isEmpty() || thumnailCollection == null) {
                return ResponseEntity.badRequest().body("Collection null");
            }
            for (ThumnailRequest thumnailRequest: thumnailRequests) {
                Thumnail thumnailExisting = thumnailService.getById(thumnailRequest.getImageId());
                if(thumnailExisting == null) {
                    thumnailExisting = new Thumnail();
                    thumnailExisting.setImageUrl(thumnailRequest.getImageUrl());
                    Collection collection = collectionService.getCollection(id);
                    thumnailExisting.setCollection(collection);
                    thumnailExisting.setProduct(null);
                    thumnailService.createThumnail(thumnailExisting);
                }
                thumnailExisting.setImageUrl(thumnailRequest.getImageUrl());
                thumnailService.updateThumnaiV2(thumnailExisting);
            }
            return ResponseEntity.ok().body("Update thumnail successfull");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
