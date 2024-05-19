package com.group6.swp391;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class Swp391ApplicationTests {

    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Test
    void contextLoads() {
        System.out.println(bCryptPasswordEncoder.encode("123"));
    }

}
