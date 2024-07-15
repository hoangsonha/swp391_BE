package com.group6.swp391.database;

import com.group6.swp391.enums.EnumRoleName;
import com.group6.swp391.model.Role;
import com.group6.swp391.model.User;
import com.group6.swp391.repository.RoleRepository;
import com.group6.swp391.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInit {

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public CommandLineRunner database() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
//                Role role_admin = new Role(EnumRoleName.ROLE_ADMIN, null);
//                Role role_staff = new Role(EnumRoleName.ROLE_STAFF, null);
//                Role role_user = new Role(EnumRoleName.ROLE_USER, null);
//                Role role_delivery = new Role(EnumRoleName.ROLE_DELIVERY, null);
//                roleRepository.save(role_admin);
//                roleRepository.save(role_delivery);
//                roleRepository.save(role_staff);
//                roleRepository.save(role_user);
//
//
//                Role role = roleRepository.getRoleByRoleName(EnumRoleName.ROLE_USER);
//
//                User user = new User("Hoang", "Son", "", bCryptPasswordEncoder.encode("1");
//
//                userRepository.save(user);
            }
        };
    }
}
