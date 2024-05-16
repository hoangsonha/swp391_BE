package com.group6.swp391.service;

import com.group6.swp391.model.EnumRoleName;
import com.group6.swp391.model.Role;
import com.group6.swp391.model.User;
import com.group6.swp391.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImp implements UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private RoleService roleService;
    @Autowired private JavaMailSender javaMailSender;

    @Override
    public List<User> findAll(String role) {
        List<User> lists = userRepository.findAll();
        if(role.equals("admin")) {

        } else {
            if(role.equals("manager")) {
                lists.forEach(rol -> {
                    Role ro1 = roleService.getRoleByRoleName(EnumRoleName.ROLE_ADMIN);
                    Role ro2 = roleService.getRoleByRoleName(EnumRoleName.ROLE_MANAGER);



                });
            }
        }
        return lists;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public boolean sendVerificationEmail(User user, String siteUrl, String role) throws MessagingException, UnsupportedEncodingException {
        if(user.getEmail() == null) {
            return false;
        }
        String admin_role = "/api/admin/";
        String user_role = "/api/user/";
        String manager_role = "/api/manager/";
        String staff_role = "/api/staff/";

        String ro = "";
        if(role.equals("admin")) {
            ro = admin_role;
        } else {
            if(role.equals("manager")) {
                ro = manager_role;
            } else {
                if(role.equals("staff")) {
                    ro = staff_role;
                } else {
                    ro = user_role;
                }
            }
        }

        String title = "Please verify your registration";
        String senderName = "Group6";
        String mailContent = "<p>Dear " + user.getLastName() + "<p>";
        mailContent += "<p>Please click the link below to verify your account<p>";
        String verifyURL = siteUrl + ro + "verify?code=" + user.getCodeVerify();
        mailContent += "<h3><a href=\"" + verifyURL + "\">VERIFY<a><h3>";
        mailContent += "<p>Thank you<br>The Group6Team<p>";
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom("hoangsonhadiggory@gmail.com", senderName);
        helper.setTo(user.getEmail());
        helper.setSubject(title);
        helper.setText(mailContent, true);

        javaMailSender.send(mimeMessage);
        return true;
    }

    @Override
    public boolean verifyAccount(String code) {
        User user = userRepository.getUserByCodeVerify(code);
        if(user == null || user.isEnabled() || !user.isLooked()) {
            return false;
        }
        userRepository.enabled(user.getUserID());
        return true;
    }

    @Override
    public User getUserByID(int userID) {
        return userRepository.getUserByUserID(userID);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }
}
