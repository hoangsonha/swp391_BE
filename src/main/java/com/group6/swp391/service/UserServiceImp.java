package com.group6.swp391.service;

import com.group6.swp391.enums.EnumRoleName;
import com.group6.swp391.model.Role;
import com.group6.swp391.model.User;
import com.group6.swp391.repository.UserRepository;
import com.group6.swp391.request.OTPRequest;
import com.group6.swp391.request.OTPValidationRequest;
import com.group6.swp391.response.RecaptchaResponse;
import com.group6.swp391.sms.SpeedSMSAPI;
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
import java.util.*;

@Service
public class UserServiceImp implements UserService {

    @Value(value = "${recaptcha.secretKey}")
    private String recaptchaSecretKey;

    @Value(value = "${recaptcha.url}")
    private String recaptchaUrl;

    private Map<String, String> otpMap = new HashMap<>();

    @Autowired private UserRepository userRepository;
    @Autowired private RoleService roleService;
    @Autowired private JavaMailSender javaMailSender;
    @Autowired private RestTemplate restTemplate;

    @Override
    public List<User> findAll(String role) {
        List<User> lists = userRepository.findAll();
        List<User> listsByRole = userRepository.findAll();
        Role role_admin = roleService.getRoleByRoleName(EnumRoleName.ROLE_ADMIN);
        Role role_delivery = roleService.getRoleByRoleName(EnumRoleName.ROLE_DELIVERY);

        if(role.equals("staff")) {
            for (User user : lists) {
                if (user.getRole().equals(role_admin) || user.getRole().equals(role_delivery)) {
                    listsByRole.remove(user);
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

        String verifyURL = siteUrl + "/public/verify?code=" + user.getCodeVerify();

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
        if(user == null || user.isEnabled() || !user.isNonLocked()) {
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
        if(user!=null && user.isNonLocked()) {
            userRepository.locked(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean unLockedUser(int id) {
        User user = userRepository.getUserByUserID(id);
        if(user!=null && !user.isNonLocked()) {
            userRepository.unLocked(id);
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

    @Override
    public boolean sendSMS(OTPRequest otpRequest) {
        try {
            String phoneOrEmail = otpRequest.getEmailOrPhone();
            int type = 5;
            String otp = generatedNumber();
            String content = "Dear Customer, Absolutely do not provide this authentication Code to anyone. Enter OTP code" + otp + " to reset the password";
            String sender = "07eda63bd942bf35";
            SpeedSMSAPI api = new SpeedSMSAPI("BeAfmVJjdj9CrAhg7oU49zqMpC9pV83r");
            String result = api.sendSMS(phoneOrEmail, content, type, sender);
            otpMap.put(phoneOrEmail, otp);
            return true;
            } catch (Exception e) {
                return false;
            }
    }

    public String generatedNumber() {
        Random random = new Random();
        int otpRD = random.nextInt(999999);
        String otp = String.valueOf(otpRD);
        while(otp.length() < 6) {
            otp = '0' + otp;
        }
        return otp;
    }

    @Override
    public boolean validateOTP(OTPValidationRequest otpValidationRequest) {
        Set<String> set = otpMap.keySet();
        String phoneOrEmail = null;
        for(String s : set) {
            phoneOrEmail = s;
        }
        if(otpValidationRequest.getPhoneOrEmail().equals(phoneOrEmail) && otpMap.get(phoneOrEmail).equals(otpValidationRequest.getOtp())) {
            otpMap.remove(phoneOrEmail);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean getUserByPhone(String phone) {
        User user = userRepository.getUserByPhone(phone);
        if(user==null || !user.isNonLocked() || !user.isEnabled()) {
            return false;
        }
        return true;
    }

    @Override
    public void lockedUserByEmail(String email) {
        User user = userRepository.getUserByEmail(email);
        if(user!=null && user.isNonLocked()) {
            userRepository.lockedByEmail(email);
        }
    }

    @Override
    public void setQuantityLoginFailed(int quantity, String email) {
        userRepository.setQuantityFailedLogin(quantity, email);
    }

    @Override
    public void setTimeLoginFailed(Date date, String email) {
        userRepository.setTimeLoginFailed(new Date(), email);
    }

    @Override
    public int getPartDate(Date date, int calendarPart) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(calendarPart);
    }

    @Override
    public int calculateSecondIn5Minute(User user) {
        int hourInDB = getPartDate(user.getTimeLoginFailed(), Calendar.HOUR);
        int minuteInDB = getPartDate(user.getTimeLoginFailed(), Calendar.MINUTE);
        int secondInDB = getPartDate(user.getTimeLoginFailed(), Calendar.SECOND);
        int hourNow = getPartDate(new Date(), Calendar.HOUR);
        int minuteNow = getPartDate(new Date(), Calendar.MINUTE);
        int secondNow = getPartDate(new Date(), Calendar.SECOND);

        int minuteInSecond = (minuteInDB * 60) + secondInDB;
        int minuteInSecondNow = (minuteNow * 60) + secondNow;
        int minus = 0;
        if(hourNow == hourInDB) {
            minus = minuteInSecondNow - minuteInSecond;
        } else if(hourNow > hourInDB) {
            minuteInSecondNow += 3600;
            minus = minuteInSecondNow - minuteInSecond;
        } else if(hourNow < hourInDB) {
            int totalSecond = (12 * 60 * 60);
            int totalSecondInDB = (hourInDB * 60 + minuteInDB) * 60;
            int remainSecond = totalSecond - totalSecondInDB;
            minus = remainSecond + minuteInSecondNow;
        }
        return minus;
    }

    @Override
    public boolean checkEmailOrPhone(String s) {
        User user = null;
        boolean check = false;
        char c = s.toLowerCase().charAt(0);
        if(c >= 'a' && c <= 'z') {
            user = userRepository.getUserByEmail(s);
            check = true;
        } else if(c >= '0' && c <= '9') {
            user = userRepository.getUserByPhone(s);
            check = false;
        }
        return check;
    }

    @Override
    public boolean setNewPassword(String emailOrPhone, String newPassword) {
        boolean check = false;
        char c = emailOrPhone.toLowerCase().charAt(0);
        if(c >= 'a' && c <= 'z') {
            userRepository.setPasswordByEmail(newPassword, emailOrPhone);
            check = true;
        } else if(c >= '0' && c <= '9') {
            userRepository.setPasswordByPhone(newPassword, emailOrPhone);
            check = true;
        }
        return check;
    }


}
