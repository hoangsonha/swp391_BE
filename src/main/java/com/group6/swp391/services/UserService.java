package com.group6.swp391.services;


import com.group6.swp391.pojos.Order;
import com.group6.swp391.pojos.User;
import com.group6.swp391.requests.OTPRequest;
import com.group6.swp391.requests.OTPValidationRequest;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

public interface UserService {

    public List<User> findAll(String role);

    public void save(User user);

    public boolean sendVerificationEmail(User user, String siteUrl) throws MessagingException, UnsupportedEncodingException;

    public boolean sendResetPasswordEmail(OTPRequest otpRequest, String siteUrl) throws MessagingException, UnsupportedEncodingException;

    public boolean verifyAccount(String code);

    public User getUserByID(int userID);

    public User getUserByEmail(String email);

    public boolean lockedUser(int id);

    public boolean unLockedUser(int id);

    public boolean verifyRecaptcha(String gRecaptchaResponse);

    public boolean sendSMS(OTPRequest otpRequest);

    public boolean validateOTP(OTPValidationRequest otpValidationRequest);

    public boolean getUserByPhone(String phone);

    public void lockedUserByEmail(String email);

    public void setQuantityLoginFailed(int quantity,String email);

    public void setTimeLoginFailed(Date date, String email);

    public void setQuantityReceiveEmailOffline(int number, String email);

    public int getPartDate(Date date, int calendarPart);

    public long calculateSecondInMinute(User user);

    public boolean checkEmailOrPhone(String s);

    public boolean setNewPassword(String emailOrPhone, String newPassword);

    public List<Integer> sendNotificationEmail() throws MessagingException, UnsupportedEncodingException;

    public boolean sendNotificationEmailHappyBirthDay() throws MessagingException, UnsupportedEncodingException;

    public void setTimeOffline(Date date, String email);

    public void sendInvoice(Order order);

    public boolean crawlData() throws InterruptedException;

    public List<User> getStaffWithLeastOrder();
    public List<User> getDeliveryWithLeastOrder();

}
