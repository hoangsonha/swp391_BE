package com.group6.swp391.service;

import com.group6.swp391.model.EnumRoleName;
import com.group6.swp391.model.Role;
import com.group6.swp391.model.User;
import com.group6.swp391.repository.UserRepository;
import com.group6.swp391.response.RecaptchaResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImp implements UserService {

    @Value(value = "${recaptcha.secretKey}")
    private String recaptchaSecretKey;


    @Value(value = "${recaptcha.url}")
    private String recaptchaUrl;

    @Autowired private UserRepository userRepository;
    @Autowired private RoleService roleService;
    @Autowired private JavaMailSender javaMailSender;
    @Autowired private RestTemplate restTemplate;

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

        String verifyURL = siteUrl + "/verify?code=" + user.getCodeVerify();

        String mai = "<body \n" +
                "    style=\"font-family: Arial, sans-serif;\n" +
                "            background-color: #f4f4f4;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            -webkit-text-size-adjust: none;\n" +
                "            -ms-text-size-adjust: none;\">\n" +
                "    <div class=\"email-container\"\n" +
                "         style=\"max-width: 600px;\n" +
                "                margin: auto;\n" +
                "                background-color: #ffffff;\n" +
                "                padding: 20px;\n" +
                "                border-radius: 8px;\n" +
                "                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\">\n" +
                "        <div class=\"header\"\n" +
                "             style=\"text-align: center;\n" +
                "                    padding-bottom: 20px;\">\n" +
                "            <img src=\"https://firebasestorage.googleapis.com/v0/b/diamond-6401b.appspot.com/o/Logo.png?alt=media&token=13f983ed-b3e1-4bbe-83b2-a47edf62c6a6\"\n" +
                "                alt=\"Logo\" style=\"max-width: 300px;\">\n" +
                "        </div>\n" +
                "        <div class=\"content\"\n" +
                "              style=\"text-align: center;\n" +
                "                    color: #333333;\">\n" +
                "            <h1\n" +
                "            style=\"font-size: 24px;\n" +
                "                margin: 0;\n" +
                "                padding: 0;\"\n" +
                "            >Verify your email address</h1>\n" +
                "            <p\n" +
                "            style=\"font-size: 16px;\n" +
                "                    line-height: 1.5;\">Welcome to Group 6 Diamond.</p>\n" +
                "            <p\n" +
                "            style=\"font-size: 16px;\n" +
                "                line-height: 1.5;\">Please click the button below to confirm your email address and activate your account.</p>\n" +
                "            <a href=\"" + verifyURL + "\" class=\"btn\"\n" +
                "               style=\"display: inline-block;\n" +
                "               margin-top: 20px;\n" +
                "               padding: 15px 25px;\n" +
                "               font-size: 16px;\n" +
                "               color: #ffffff;\n" +
                "               background-color: #f52d56;\n" +
                "               border-radius: 5px;\n" +
                "               text-decoration: none;\">Confirm Email</a>\n" +
                "            <p>If you received this email in error, simply ignore this email and do not click the button.</p>\n" +
                "        </div>\n" +
                "        <div class=\"footer\"\n" +
                "             style=\"text-align: center;\n" +
                "             font-size: 14px;\n" +
                "             color: #777777;\n" +
                "             margin-top: 20px;\">\n" +
                "            <p>Copyright &copy; 2019, YOUWORK.TODAY INC</p>\n" +
                "            <h2>Thank you, have a good day .</h2></br>Group6 Team\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>";

        String title = "Please verify your registration";
        String senderName = "Group6";
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom("hoangsonhadiggory@gmail.com", senderName);
        helper.setTo(user.getEmail());
        helper.setSubject(title);
        helper.setText(mai, true);

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

    @Override
    public boolean lockedUser(int id) {
        User user = userRepository.getUserByUserID(id);
        if(user!=null && user.isLooked()) {
            userRepository.locked(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteUser(int id) {
        User user = userRepository.getUserByUserID(id);
        if (user != null) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean verifyRecaptcha(String gRecaptchaResponse) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("secret", recaptchaSecretKey); // Thay chỗ này sẽ ra error
        map.add("response", gRecaptchaResponse);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        RecaptchaResponse response = restTemplate.postForObject(recaptchaUrl, request, RecaptchaResponse.class);
        // gửi object tới url này rồi trả về đối tượng .class
        return response.isSuccess();
    }
}
