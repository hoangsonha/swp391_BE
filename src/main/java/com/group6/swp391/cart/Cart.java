package com.group6.swp391.cart;

import com.group6.swp391.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Iterator;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Cart {

    private MultiValueMap<Integer, Product> map = new LinkedMultiValueMap<Integer, Product>();

    public boolean add(Product product) {
        boolean check = false;
        try {
            this.map.add(product.getProductID(), product);
            check = true;
        } catch(Exception e) {
            log.error("Can not add this product");
        }
        return check;
    }

    public boolean remove(Product product) {
        boolean check = false;
        try {
            this.map.remove(product.getProductID(), product);
            check = true;
        } catch(Exception e) {
            log.error("Can not remove this product");
        }
        return check;
    }

    public int getTotalQuantity() {
        int total = 0;
        for(Integer i : map.keySet()) {
            total += this.map.get(i).size();
        }
        return total;
    }

    public double getTotalPrice() {
        double total = 0;
        for(Integer i : this.map.keySet()) {
            Iterator<Product> ite = this.map.get(i).iterator();
            for (Iterator<Product> it = ite; it.hasNext(); ) {
                Product t = it.next();
                total += t.getTotalPrice();
            }
        }
        return total;
    }

}
