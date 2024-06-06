package com.group6.swp391;

//import com.group6.swp391.cart.Cart;
import com.group6.swp391.model.Diamond;
import com.group6.swp391.model.Product;
import com.group6.swp391.model.User;
import com.group6.swp391.repository.DiamondRepository;
import com.group6.swp391.repository.RoleRepository;
import com.group6.swp391.repository.UserRepository;
import com.group6.swp391.service.UserService;
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
    @Autowired private UserRepository userRepository;
    @Autowired private UserService userService;
    @Autowired private DiamondRepository diamondRepository;

    @Test
    void contextLoads() {

        User user = userRepository.getUserByEmail("hoangsonha492@gmail.com");
    int year = 2024;
    int month = 6;
    int day = 7;
    int hour = 0;
    int minute = 3;
    int second = 30;

        int year1 = 2025;
        int month1 = 1;
        int day1 = 1;
        int hour1 = 0;
        int minute1 = 3;
        int second1 = 30;

        System.out.println(userService.calculateSecondIn5Minute(user));



//    select * from railway.diamond where total_price <= 1000000000 and total_price >= 500000000 and carat <= 1.15 and carat >= 0.75
//    and dimensions <= 6 and dimensions >= 5.8 and color_level = 'E' and clarify = 'VS2' and shape = 'Round';
//
//        String string_price = "tren 500 trieu dong";
//        String[] str_price = string_price.split(" ");
//        List<Double> lists_price = new ArrayList<>();
//        for(int i =0; i< str_price.length; i++) {
//            char c = str_price[i].charAt(0);
//            if(c >= '0' && c <= '9') {
//                System.out.println(str_price[i]);
//                str_price[i] += "000000";
//                System.out.println(str_price[i]);
//                double d = Double.parseDouble(str_price[i]);
//                System.out.println(d);
//                lists_price.add(d);
//            }
//        }
//
//        for(Double d : lists_price) {
//            System.out.println(d);
//        }

//        boolean check = userService.checkEmailOrPhone("hoangsonhadev@gmail.com0334386995");
//        if(check == false) {
//            System.out.println("id k ton tai");
//        }

//        String siteUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
//        System.out.println(siteUrl);

//        class TestDiamond {
//            @Getter
//            private int id;
//            private String name;
//            @Getter
//            private int quantity;
//            @Getter
//            private double price;
//
//            public TestDiamond(int id, String name, double price, int quantity) {
//                this.id = id;
//                this.name = name;
//                this.price = price;
//                this.quantity = quantity;
//            }
//        }
//
//        class TestProduct {
//            @Getter
//            private int id;
//            private String name;
//            @Getter
//            private int quantity;
//            @Getter
//            private double price;
//            @Getter
//            private TestDiamond diamond;
//
//            public TestProduct(int id, String name, double price, int quantity, TestDiamond diamond) {
//                this.id = id;
//                this.name = name;
//                this.price = price;
//                this.quantity = quantity;
//                this.diamond = diamond;
//            }
//        }
//
//
//        Cart cart = new Cart();
//
//
//
//
//        TestDiamond testDiamond1 = new TestDiamond(6, "D6", 60, 1);
//        TestDiamond testDiamond2 = new TestDiamond(7, "D7", 70, 1);
//        TestDiamond testDiamond3 = new TestDiamond(8, "D8", 80, 1);
//        TestDiamond testDiamond4 = new TestDiamond(9, "D9", 90, 1);
//        TestDiamond testDiamond5 = new TestDiamond(10, "D10", 100, 1);
//
//        TestProduct testProduct1 = new TestProduct(1, "P1", 1, 10, testDiamond3);
//        TestProduct testProduct2 = new TestProduct(2, "P2", 1, 20, testDiamond1);
//        TestProduct testProduct3 = new TestProduct(3, "P3", 1, 30, testDiamond2);
//        TestProduct testProduct4 = new TestProduct(4, "P4", 1, 40, null);
//        TestProduct testProduct5 = new TestProduct(5, "P5", 1, 50, null);
//
//        cart.add(testProduct1);
//        cart.add(testProduct2);
//        cart.add(testProduct3);
//        cart.add(testProduct4);
//        cart.add(testProduct5);
//        cart.add(testDiamond4);
//        cart.add(testDiamond5);
//
//
////        System.out.println(cart.getTotalQuantity());
////
////        System.out.println(cart.getTotalPrice());
//
//
//
//
//        MultiValueMap<Integer, String> map = new LinkedMultiValueMap<Integer, String>();
//
//        map.add(1, "1");
//        map.add(2, "2");
//        map.add(2, "3");
//
//        System.out.println(map);
//
//        for(Integer i : map.keySet()) {
//            System.out.println(map.get(i));
//        }
//
//        for(Integer i : map.keySet()) {
//            Iterator<String> ite = map.get(i).iterator();
//            for (Iterator<String> it = ite; it.hasNext(); ) {
//                String t = it.next();
//
//                System.out.println(t);
//                }
//            }
//
//
//    }
    }
}
