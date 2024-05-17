package com.group6.swp391.database;

import com.group6.swp391.model.EnumRoleName;
import com.group6.swp391.model.Role;
import com.group6.swp391.repository.RoleRepository;
import com.group6.swp391.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component

public class DatabaseInit {

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;

    @Bean
    public CommandLineRunner database() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
//                Role role_admin = new Role(EnumRoleName.ROLE_ADMIN, null);
//                Role role_manager = new Role(EnumRoleName.ROLE_MANAGER, null);
//                Role role_staff = new Role(EnumRoleName.ROLE_STAFF, null);
//                Role role_user = new Role(EnumRoleName.ROLE_USER, null);
//                Role role_non = new Role(EnumRoleName.ROLE_NON, null);
//                roleRepository.save(role_admin);
//                roleRepository.save(role_manager);
//                roleRepository.save(role_staff);
//                roleRepository.save(role_user);
//                roleRepository.save(role_non);
            }
        };
    }
}
