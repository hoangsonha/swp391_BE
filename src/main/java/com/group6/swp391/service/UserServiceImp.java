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
        List<User> listsByRole = userRepository.findAll();
        Role role_admin = roleService.getRoleByRoleName(EnumRoleName.ROLE_ADMIN);
        Role role_manager = roleService.getRoleByRoleName(EnumRoleName.ROLE_MANAGER);
        if(role.equals("manager")) {
                for(User user : lists) {
                    List<Role> list_role = user.getRoles().stream().toList();
                    if(list_role != null) {
                        if(list_role.size() > 0) {
                            for(Role ro : list_role) {
                                if(ro.equals(role_admin)) {
                                    listsByRole.remove(user);
                                }
                            }
                        }
                    }
                }
            } else if(role.equals("staff")) {
                for(User user : lists) {
                    List<Role> list_role = user.getRoles().stream().toList();
                    if(list_role != null) {
                        if(list_role.size() > 0) {
                            for(Role ro : list_role) {
                                if(ro.equals(role_admin) || ro.equals(role_manager)) {
                                    listsByRole.remove(user);
                                }
                            }
                        }
                    }
                }
            } else if(role.equals("admin")) {
                    return lists;
                }
        return listsByRole;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public boolean sendVerificationEmail(User user, String siteUrl) throws MessagingException, UnsupportedEncodingException {
        if(user.getEmail() == null) {
            return false;
        }

        String title = "Please verify your registration";
        String senderName = "Group6";
        String mailContent = "<p>Dear My Customer<p>";
        mailContent += "<p>Please click the link below to verify your account<p>";
        String verifyURL = siteUrl + "/verify?code=" + user.getCodeVerify();
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
