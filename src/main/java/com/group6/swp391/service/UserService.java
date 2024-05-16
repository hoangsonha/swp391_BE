package com.group6.swp391.service;


import com.group6.swp391.model.User;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface UserService {

    public List<User> findAll(String role);
    public void save(User user);
    public boolean sendVerificationEmail(User user, String siteUrl, String role) throws MessagingException, UnsupportedEncodingException;

    public boolean verifyAccount(String code);

    public User getUserByID(int userID);

    public User getUserByEmail(String email);
}
