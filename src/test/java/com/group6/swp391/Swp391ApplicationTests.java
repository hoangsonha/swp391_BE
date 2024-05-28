package com.group6.swp391;

import com.group6.swp391.repository.RoleRepository;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

@SpringBootTest
class Swp391ApplicationTests {

    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired private RoleRepository roleRepository;
    @Test
    void contextLoads() {

        class TestProduct {
            @Getter
            private int id;
            private String name;
            @Getter
            private int quantity;
            @Getter
            private double price;

            public TestProduct(int id, String name, double price, int quantity) {
                this.id = id;
                this.name = name;
                this.price = price;
                this.quantity = quantity;
            }
        }

        // System.out.println(bCryptPasswordEncoder.encode("1"));

        MultiValueMap<Integer, TestProduct> map = new LinkedMultiValueMap<Integer, TestProduct>();

        TestProduct testProduct1 = new TestProduct(1, "Ha", 1, 10);
        TestProduct testProduct2 = new TestProduct(2, "Ngan", 1, 20);
        TestProduct testProduct3 = new TestProduct(3, "Quynh", 1, 30);
        TestProduct testProduct4 = new TestProduct(4, "Phuong", 1, 40);
        TestProduct testProduct5 = new TestProduct(5, "Hoang", 1, 50);
        map.add(testProduct1.getId(), testProduct1);
        map.add(testProduct2.getId(), testProduct2);
        map.add(testProduct3.getId(), testProduct3);
        map.add(testProduct4.getId(), testProduct4);
        map.add(testProduct5.getId(), testProduct5);
        map.add(testProduct1.getId(), testProduct1);



        int totalQ = 0;

        for(Integer i : map.keySet()) {
            totalQ += map.get(i).size();
        }

        System.out.println(totalQ);



        int totalQuan = 0;

        for(Integer i : map.keySet()) {
            Iterator<TestProduct> ite = map.get(i).iterator();
            for (Iterator<TestProduct> it = ite; it.hasNext(); ) {
                TestProduct t = it.next();

                totalQuan += t.getQuantity();

            }
        }

        System.out.println(totalQuan);

        double totalPrice = 0;

        for(Integer i : map.keySet()) {
            Iterator<TestProduct> ite = map.get(i).iterator();
            for (Iterator<TestProduct> it = ite; it.hasNext(); ) {
                TestProduct t = it.next();

                totalPrice += t.getPrice();

            }
        }

        System.out.println(totalPrice);


    }

}
