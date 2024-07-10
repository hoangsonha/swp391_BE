package com.group6.swp391.controller;

import com.group6.swp391.model.Collection;
import com.group6.swp391.model.CollectionProduct;
import com.group6.swp391.model.Product;
import com.group6.swp391.model.Thumnail;
import com.group6.swp391.request.CollectionProductRequest;
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
import jakarta.validation.Valid;
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

    private String generateNewCollectionId(String lastCollectionId) {
        int newIdNumber = 100;
        if(lastCollectionId != null && lastCollectionId.startsWith("SET")) {
            String lastIdNumberStr = lastCollectionId.substring(3);
            int lastIdNumber = Integer.parseInt(lastIdNumberStr);
            newIdNumber = lastIdNumber + 1;
        }
        return "SET" + newIdNumber;
    }
    @PostMapping("/create_collection")
    public ResponseEntity<?> createCollection(@RequestBody @Valid CollectionRequest collectionRequest) {
        try {
            double price = 0.0;
            Collection newCollection = new Collection();
            String lastCollection = collectionService.getLastCollectionId();
            String newCollectionId = generateNewCollectionId(lastCollection);
            newCollection.setCollecitonId(newCollectionId);
            newCollection.setCollectionTitle(collectionRequest.getCollectionTitle());
            newCollection.setCollectionName(collectionRequest.getCollectionName());
            newCollection.setGemStone(collectionRequest.getGemStone());
            newCollection.setGoldOld(collectionRequest.getGoldOld());
            newCollection.setGoldType(collectionRequest.getGoldType());
            newCollection.setStatus(true);
            List<CollectionProduct> collectionProducts = new ArrayList<>();
            for (CollectionProductRequest collectionProductRequest : collectionRequest.getCollectionProductRequests()) {
                CollectionProduct newCollectionProduct = new CollectionProduct();
                collectionProducts.add(newCollectionProduct);
                Product productExisting = productService.getProductById(collectionProductRequest.getProductId());
                if (productExisting == null) {
                    return ResponseEntity.badRequest().body("Product not found");
                }
                price += productExisting.getTotalPrice();
                newCollectionProduct.setProduct(productExisting);
            }
            newCollection.setPrice(price);
            newCollection.setCollectionProduct(collectionProducts);
            collectionService.createCollection(newCollection);
            for (CollectionProduct collectionProduct : collectionProducts) {
                collectionProduct.setCollection(newCollection);
                collectionProductService.createCollectionProduct(collectionProduct);
            }
            List<Thumnail> thumnailList = new ArrayList<>();
            for(Thumnail thumnail  : collectionRequest.getThumnails()) {
                Thumnail newThumnail = new Thumnail();
                newThumnail.setImageUrl(thumnail.getImageUrl());
                newThumnail.setCollection(newCollection);
                thumnailList.add(newThumnail);
                thumnailService.createThumnailV2(newCollection.getCollecitonId(), newThumnail.getImageUrl());
            }
            newCollection.setThumnails(thumnailList);
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
    public ResponseEntity<?> updateThumnailCollection(@PathVariable("collection_id") String id,
                                                      @RequestBody @Valid List<ThumnailRequest> thumnailRequests) {
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

    @PostMapping("/update_collection/{collection_id}")
    public ResponseEntity<ObjectResponse> updateCollection(@PathVariable("collection_id") String id, @RequestBody CollectionRequest collectionRequest) {
        try {
            Collection collectionExisting = collectionService.getCollection(id);
            if(collectionExisting == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Collection not exist", null));
            }
            if(collectionRequest.getCollectionTitle() != null) {
                collectionExisting.setCollectionTitle(collectionRequest.getCollectionTitle());
            }
            if(collectionRequest.getCollectionName() != null) {
                collectionExisting.setCollectionName(collectionRequest.getCollectionName());
            }
            if(collectionRequest.getGemStone() != null) {
                collectionExisting.setGemStone(collectionRequest.getGemStone());
            }
            if(collectionRequest.getGoldOld() != null) {
                collectionExisting.setGoldOld(collectionRequest.getGoldOld());
            }
            if(collectionRequest.getGoldType() != null) {
                collectionExisting.setGoldType(collectionRequest.getGoldType());
            }
            if(collectionRequest.getStatus().equalsIgnoreCase("Hết")) {
                collectionExisting.setStatus(false);
            } else if(collectionRequest.getStatus().equalsIgnoreCase("Còn")) {
                collectionExisting.setStatus(true);
            }
            if(collectionRequest.getCollectionProductRequests() != null) {
                for (CollectionProductRequest collectionProductRequest : collectionRequest.getCollectionProductRequests()) {
                    Product product = productService.getProductById(collectionProductRequest.getProductId());
                    if(product == null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Product not exist", null));
                    }
                    CollectionProduct collectionProduct = collectionProductService.getCollectionProduct(collectionProductRequest.getCollectionProductId());
                    if(collectionProduct == null) {
                        collectionProduct = new CollectionProduct();
                        collectionProduct.setCollection(collectionExisting);
                        collectionProduct.setProduct(product);
                        collectionExisting.setPrice(collectionExisting.getPrice() + product.getTotalPrice());
                        collectionProductService.createCollectionProduct(collectionProduct);
                        break;
                    }
                    collectionExisting.setPrice(collectionExisting.getPrice() - collectionProduct.getProduct().getTotalPrice() + product.getTotalPrice());
                    collectionProduct.setProduct(product);
                    collectionProductService.updateCollectionProduct(collectionProduct);
                }
            }
            collectionService.updateCollection(collectionExisting);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Update Collection Successfully", collectionExisting));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "update failed", e.getMessage()));
        }
    }

}
