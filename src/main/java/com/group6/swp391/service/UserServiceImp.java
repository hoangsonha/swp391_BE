package com.group6.swp391.service;

import com.group6.swp391.enums.EnumRoleName;
import com.group6.swp391.model.*;
import com.group6.swp391.repository.UserRepository;
import com.group6.swp391.request.OTPRequest;
import com.group6.swp391.request.OTPValidationRequest;
import com.group6.swp391.response.RecaptchaResponse;
import com.group6.swp391.sms.SpeedSMSAPI;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class UserServiceImp implements UserService {

    @Value(value = "${recaptcha.secretKey}")
    private String recaptchaSecretKey;

    @Value(value = "${recaptcha.url}")
    private String recaptchaUrl;

    private Map<String, String> otpMap = new HashMap<>();

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired private CrawledDataProperties crawledDataProperties;

    private WebDriver webDriver;

    // CRUD


    @Override
    public List<User> findAll(String role) {
        List<User> lists = userRepository.findAll();
        List<User> listsByRole = userRepository.findAll();
        Role role_admin = roleService.getRoleByRoleName(EnumRoleName.ROLE_ADMIN);
        Role role_delivery = roleService.getRoleByRoleName(EnumRoleName.ROLE_DELIVERY);

        if (role.equals("staff")) {
            for (User user : lists) {
                if (user.getRole().equals(role_admin) || user.getRole().equals(role_delivery)) {
                    listsByRole.remove(user);
                }
            }
        } else if (role.equals("admin")) {
            return lists;
        }
        return listsByRole;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public boolean lockedUser(int id) {
        User user = userRepository.getUserByUserID(id);
        if (user != null && user.isNonLocked()) {
            userRepository.locked(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean unLockedUser(int id) {
        User user = userRepository.getUserByUserID(id);
        if (user != null && !user.isNonLocked()) {
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
    public User getUserByID(int userID) {
        return userRepository.getUserByUserID(userID);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }


    // active account


    @Override
    public boolean sendVerificationEmail(User user, String siteUrl) throws MessagingException, UnsupportedEncodingException {
        if (user.getEmail() == null) {
            return false;
        }
        try {
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
        } catch (Exception e) {
            log.error("Cannot send mail {}", e.toString());
            return false;
        }
    }

    @Override
    public boolean verifyAccount(String code) {
        User user = userRepository.getUserByCodeVerify(code);
        if (user == null || user.isEnabled() || !user.isNonLocked()) {
            return false;
        }
        userRepository.enabled(user.getUserID());
        return true;
    }


    // function recaptcha


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


    // function set password


    @Override
    public boolean sendResetPasswordEmail(OTPRequest otpRequest, String siteUrl) throws MessagingException, UnsupportedEncodingException {
        try {
            String phoneOrEmail = otpRequest.getEmailOrPhone();
            User user = null;
            boolean check = checkEmailOrPhone(phoneOrEmail);
            if (check) {
                user = userRepository.getUserByEmail(phoneOrEmail);
            } else user = userRepository.getUserByPhone(phoneOrEmail);
            String otp = generatedNumber();
            String content = "Dear Customer, Absolutely do not provide this authentication Code to anyone. Enter OTP code " + otp + " to reset the password";
            otpMap.put(phoneOrEmail, otp);
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
                    "            <h1\n" +
                    "            style=\"font-size: 20px;\n" +
                    "                line-height: 1.5;\">" + content + "</h1>\n" +
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
        } catch (Exception e) {
            log.error("Cannot send email {}", e.toString());
            return false;
        }
    }

    @Override
    public boolean setNewPassword(String emailOrPhone, String newPassword) {
        boolean check = false;
        char c = emailOrPhone.toLowerCase().charAt(0);
        if (c >= 'a' && c <= 'z') {
            userRepository.setPasswordByEmail(bCryptPasswordEncoder.encode(newPassword), emailOrPhone);
            check = true;
        } else if (c >= '0' && c <= '9') {
            userRepository.setPasswordByPhone(bCryptPasswordEncoder.encode(newPassword), emailOrPhone);
            check = true;
        }
        return check;
    }

    public String generatedNumber() {
        Random random = new Random();
        int otpRD = random.nextInt(999999);
        String otp = String.valueOf(otpRD);
        while (otp.length() < 6) {
            otp = '0' + otp;
        }
        return otp;
    }

    @Override
    public boolean validateOTP(OTPValidationRequest otpValidationRequest) {
        Set<String> set = otpMap.keySet();
        String phoneOrEmail = null;
        for (String s : set) {
            phoneOrEmail = s;
        }
        if (otpValidationRequest.getEmailOrPhone().equals(phoneOrEmail) && otpMap.get(phoneOrEmail).equals(otpValidationRequest.getOtp())) {
            otpMap.remove(phoneOrEmail);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean getUserByPhone(String phone) {
        User user = userRepository.getUserByPhone(phone);
        if (user == null || !user.isNonLocked() || !user.isEnabled()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean sendSMS(OTPRequest otpRequest) {
        try {
            String phoneOrEmail = otpRequest.getEmailOrPhone();
            int type = 5;
            String otp = generatedNumber();
            String content = "Dear Customer, Absolutely do not provide this authentication Code to anyone. Enter OTP code" + otp + " to reset the password";
            String sender = "07eda63bd942bf35";
            SpeedSMSAPI api = new SpeedSMSAPI("Your token");
            String result = api.sendSMS(phoneOrEmail, content, type, sender);
            otpMap.put(phoneOrEmail, otp);
            return true;
        } catch (Exception e) {
            log.error("Cannot send SMS {}", e.toString());
            return false;
        }
    }


    // function login failed


    @Override
    public void lockedUserByEmail(String email) {
        User user = userRepository.getUserByEmail(email);
        if (user != null && user.isNonLocked()) {
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
    public long calculateSecondIn5Minute(User user) {
        long countSecond = 0;
        if (user.getTimeLoginFailed() != null) {
            int yearInDB = getPartDate(user.getTimeLoginFailed(), Calendar.YEAR);
            int monthInDB = getPartDate(user.getTimeLoginFailed(), Calendar.MONTH);
            int dayInDB = getPartDate(user.getTimeLoginFailed(), Calendar.DATE);
            int hourInDB = getPartDate(user.getTimeLoginFailed(), Calendar.HOUR_OF_DAY);
            int minuteInDB = getPartDate(user.getTimeLoginFailed(), Calendar.MINUTE);
            int secondInDB = getPartDate(user.getTimeLoginFailed(), Calendar.SECOND);
            monthInDB += 1;
            int yearInNow = getPartDate(new Date(), Calendar.YEAR);
            int monthInNow = getPartDate(new Date(), Calendar.MONTH);
            int dayInNow = getPartDate(new Date(), Calendar.DATE);
            int hourNow = getPartDate(new Date(), Calendar.HOUR_OF_DAY);
            int minuteNow = getPartDate(new Date(), Calendar.MINUTE);
            int secondNow = getPartDate(new Date(), Calendar.SECOND);
            monthInNow += 1;

            int countDayInYearInDB = countDayInYear(yearInDB, monthInDB, dayInDB);
            int countDayInYearInNow = countDayInYear(yearInNow, monthInNow, dayInNow);
            long countSecondInDB = (countDayInYearInDB * 24 * 60 * 60) + (hourInDB * 60 * 60 + (minuteInDB * 60 + secondInDB));
            long countSecondInNow = countDayInYearInNow * 24 * 60 * 60 + (hourNow * 60 * 60 + (minuteNow * 60 + secondNow));

            if (yearInDB == yearInNow) {
                countSecond = countSecondInNow - countSecondInDB;
            } else {
                if (yearInDB < yearInNow) {
                    long countSecondIn1Year = countDayIn1Year(yearInDB) * 24 * 60 * 60;
                    long countSecondInBD = countSecondIn1Year - countSecondInDB;
                    countSecond = countSecondInBD + countSecondInNow;
                }
            }
        }
        return countSecond;
    }

    public int countDayInMonth(int year, int month) {
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            return 31;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        }
        if (year % 4 == 0 && year % 100 != 0) {
            if (month == 2) {
                return 29;
            }
        }
        return 28;
    }

    public int countDayInYear(int year, int month, int day) {
        int count = 0;
        for (int i = 1; i < month; i++) {
            count += countDayInMonth(year, i);
        }
        count += day;
        return count - 1;
    }

    public int countDayIn1Year(int year) {
        if (year % 4 == 0 && year % 100 != 0) {
            return 366;
        }
        return 365;
    }

    @Override
    public boolean checkEmailOrPhone(String s) {
        User user = null;
        boolean check = false;
        char c = s.toLowerCase().charAt(0);
        if (c >= 'a' && c <= 'z') {
            user = userRepository.getUserByEmail(s);
            check = true;
        } else if (c >= '0' && c <= '9') {
            user = userRepository.getUserByPhone(s);
            check = false;
        }
        return check;
    }

    // function automation send email (System handler)

    @Override
    public boolean sendNotificationEmail() throws MessagingException, UnsupportedEncodingException {
        boolean check = false;
        List<User> lists = userRepository.findAll();
        for (User user : lists) {
            if (user.getOfflineAt() != null) {
                long dayOff = calculateSecondIn5Minute(user);
                int monthOff = (int) (dayOff / 2_592_000);
                int yearInDB = getPartDate(user.getOfflineAt(), Calendar.YEAR);
                int monthInDB = getPartDate(user.getOfflineAt(), Calendar.MONTH);
                int dayInDB = getPartDate(user.getOfflineAt(), Calendar.DATE);
                monthInDB += 1;
                int yearInNow = getPartDate(new Date(), Calendar.YEAR);
                int monthInNow = getPartDate(new Date(), Calendar.MONTH);
                int dayInNow = getPartDate(new Date(), Calendar.DATE);
                monthInNow += 1;

                if (dayInNow == dayInDB) {
                    if (yearInNow == yearInDB) {
                        if ((monthInNow - monthInDB) == 1) {
                            check = mailOffline(user, monthOff);
                        }
                    } else {
                        if (monthInNow == 1 && monthInDB == 12) {
                            check = mailOffline(user, monthOff);
                        }
                    }
                }
            }
        }
        return check;
    }

    @Override
    public boolean sendNotificationEmailHappyBirthDay() throws MessagingException, UnsupportedEncodingException {
        boolean check = false;
        List<User> lists = userRepository.findAll();
        for (User user : lists) {
            if (user.getYearOfBirth() != null) {
                boolean happy = checkBirthDay(user);
                if (happy) {
                    check = emailHappyBirth(user);
                }
            }
        }
        return check;
    }

    @Override
    public void setTimeOffline(Date date, String email) {
        userRepository.setTimeOfflineAt(date, email);
    }

    public boolean mailOffline(User user, int monthOff) throws MessagingException, UnsupportedEncodingException {
        if (user.getEmail() == null) {
            return false;
        }
        try {
            String verifyURL = "feedback";
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
                    "            >Bạn đã không đăng nhập \"" + monthOff + "\" tháng!</h1>\n" +
                    "            <p\n" +
                    "            style=\"font-size: 16px;\n" +
                    "                    line-height: 1.5;\">Welcome to Group 6 Diamond.</p>\n" +
                    "            <p\n" +
                    "            style=\"font-size: 16px;\n" +
                    "                line-height: 1.5;\">Nếu có vấn đề gì không hài lòng hãy phản hồi cho chúng tôi.</p>\n" +
                    "            <a href=\"" + verifyURL + "\" class=\"btn\"\n" +
                    "               style=\"display: inline-block;\n" +
                    "               margin-top: 20px;\n" +
                    "               padding: 15px 25px;\n" +
                    "               font-size: 16px;\n" +
                    "               color: #ffffff;\n" +
                    "               background-color: #f52d56;\n" +
                    "               border-radius: 5px;\n" +
                    "               text-decoration: none;\">Góp ý</a>\n" +
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

            String title = "Chăm sóc khách hàng";
            String senderName = "Auto notification";
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setFrom("hoangsonhadiggory@gmail.com", senderName);
            helper.setTo(user.getEmail());
            helper.setSubject(title);
            helper.setText(mai, true);

            javaMailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            log.error("Cannot send mail ", e.toString());
            return false;
        }
    }

    public boolean emailHappyBirth(User user) throws MessagingException, UnsupportedEncodingException {
        if (user.getEmail() == null) {
            return false;
        }
        try {
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
                    "            >Chúc mừng sinh nhật</h1>\n" +
                    "            <p\n" +
                    "            style=\"font-size: 16px;\n" +
                    "                    line-height: 1.5;\">Welcome to Group 6 Diamond.</p>\n" +
                    "            <p\n" +
                    "            style=\"font-size: 16px;\n" +
                    "                line-height: 1.5;\">Diamond shop xin cảm ơn quý khách đã ủng hộ shop trong thời gian vừa qua và Happy Birth Day</p>\n" +
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

            String title = "Happy Birth Day";
            String senderName = "Auto notification";
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setFrom("hoangsonhadiggory@gmail.com", senderName);
            helper.setTo(user.getEmail());
            helper.setSubject(title);
            helper.setText(mai, true);

            javaMailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            log.error("Cannot send mail {}", e.toString());
            return false;
        }
    }

    public boolean checkBirthDay(User user) {
        boolean check = false;
        int yearInDB = getPartDate(user.getYearOfBirth(), Calendar.YEAR);
        int monthInDB = getPartDate(user.getYearOfBirth(), Calendar.MONTH);
        int dayInDB = getPartDate(user.getYearOfBirth(), Calendar.DATE);
        monthInDB += 1;
        int yearInNow = getPartDate(new Date(), Calendar.YEAR);
        int monthInNow = getPartDate(new Date(), Calendar.MONTH);
        int dayInNow = getPartDate(new Date(), Calendar.DATE);
        monthInNow += 1;
        if (dayInNow == dayInDB && monthInNow == monthInDB && ((yearInNow - yearInDB) >= 1)) {
            check = true;
        }
        return check;
    }

    // Invoice

    @Override
    public boolean sendInvoice(Order order) {
        if (order == null) {
            return false;
        }
        try {
            NumberFormat nb = NumberFormat.getInstance();
            Payment payment = paymentService.findByOrder(order);
            SimpleDateFormat spdfm = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

            String mail = "<body style=\"padding: 20px; margin: 0; font-family: Inter, -apple-system, BlinkMacSystemFont, Segoe UI, Roboto, Oxygen, Ubuntu, Cantarell, Open Sans, Helvetica Neue, sans-serif; -webkit-font-smoothing: antialiased; background-color: #dcdcdc;\">\n" +
                    "                        <section class=\"wrapper-invoice\" style=\"display: flex; justify-content: center;\">\n" +
                    "                          <div class=\"invoice\" style=\"height: auto;\n" +
                    "                          background: #fff;\n" +
                    "                          padding: 5vh;\n" +
                    "                          margin-top: 5vh;\n" +
                    "                          width: 100%;\n" +
                    "                          box-sizing: border-box;\n" +
                    "                          border: 1px solid #dcdcdc;\">\n" +
                    "                            <div class=\"invoice-information\" style=\"float: right;\n" +
                    "                            text-align: right;\">\n" +
                    "                              <p style=\"font-size: 2vh;\n" +
                    "  color: gray;\"><b style=\"color: #0F172A;\">Invoice #</b> : 1</p>\n" +
                    "                              <p style=\"font-size: 2vh;\n" +
                    "  color: gray;\"><b style=\"color: #0F172A;\">Created Date </b>: " + spdfm.format(new Date()) + "</p>\n" +
                    "                            </div>\n" +
                    "                            <div class=\"invoice-logo-brand\">               \n" +
                    "                              <img src= \"https://firebasestorage.googleapis.com/v0/b/diamond-6401b.appspot.com/o/Logo.png?alt=media&token=13f983ed-b3e1-4bbe-83b2-a47edf62c6a6\"     \n" +
                    "                              alt=\"Logo\" style=\"width: 360px;\">\n" +
                    "                            </div>\n" +
                    "                            <div class=\"invoice-head\" style=\"display: flex;\n" +
                    "  margin-top: 8vh; justify-content: space-between;\" >\n" +
                    "                              <div class=\"head client-info\" style=\"width: 100%;\n" +
                    "  box-sizing: border-box;\">\n" +
                    "                                <p style=\"font-size: 2vh;\n" +
                    "  color: gray;\">Mã số hoá đơn</p>\n" +
                    "                                <p style=\"font-size: 2vh;\n" +
                    "  color: gray;\">Tên khách hàng</p>\n" +
                    "                                <p style=\"font-size: 2vh;\n" +
                    "  color: gray;\">Phone</p>\n" +
                    "                                <p style=\"font-size: 2vh;\n" +
                    "  color: gray;\">Address</p>\n" +
                    "  <p style=\"font-size: 2vh;\n" +
                    "  color: gray;\">Email</p>\n" +
                    "                              </div>\n" +
                    "                              <div class=\"head client-data\" style=\"min-width: 200px;\">\n" +
                    "                                <p style=\"font-size: 2vh;\n" +
                    "  color: gray;\">" + order.getOrderID() + "</p>\n" +
                    "                                <p style=\"font-size: 2vh;\n" +
                    "  color: gray;\">" + order.getUser().getFirstName() + " " + order.getUser().getLastName() + "</p>\n" +
                    "                                <p style=\"font-size: 2vh;\n" +
                    "  color: gray;\">" + order.getUser().getPhone() + "</p>\n" +
                    "                                <p style=\"font-size: 2vh;\n" +
                    "  color: gray;\">" + order.getUser().getAddress() + "</p>\n" +
                    "    <p style=\"font-size: 2vh;\n" +
                    "  color: gray;\">" + order.getEmail() + "</p>\n" +
                    "                              </div>\n" +
                    "                            </div>\n" +
                    "                            <div class=\"invoice-body\" style=\"margin-top: 8vh;\">\n" +
                    "                              <table class=\"table\" style=\"border-collapse: collapse;\n" +
                    "                               width: 100%;\">\n" +
                    "                                <thead>\n" +
                    "                                  <tr>\n" +
                    "                                   <th style=\" font-size: 2vh;\n" +
                    "                                    border: 1px solid #dcdcdc;\n" +
                    "                                    text-align: left;\n" +
                    "                                    background-color: #eeeeee;\">No.</th>\n" +
                    "                                    <th style=\" font-size: 2vh;\n" +
                    "                                    border: 1px solid #dcdcdc;\n" +
                    "                                    text-align: left;\n" +
                    "                                    padding: 1vh;\n" +
                    "                                    background-color: #eeeeee;\">Item Description</th>\n" +
                    "                                    <th style=\" font-size: 2vh;\n" +
                    "                                    border: 1px solid #dcdcdc;\n" +
                    "                                    text-align: left;\n" +
                    "                                    padding: 1vh;\n" +
                    "                                    background-color: #eeeeee;\">Amount</th>\n" +
                    "                                  </tr>\n" +
                    "                                </thead>\n" +
                    "                                <tbody>\n";
            int no = 1;
            for (OrderDetail orderDetail : order.getOrderDetails()) {

                if (orderDetail.getDiamond() != null) {
                    mail +=         "                                   <tr>\n" +
                                    "                                    <td style=\"font-size: 2vh;\n" +
                                    "                                    border: 1px solid #dcdcdc;\n" +
                                    "                                    text-align: left;\n" +
                                    "                                    padding: 1vh;\n" +
                                    "                                    background-color: #fff;\">" + no + "</td>\n" +
                                    "                                    <td style=\"font-size: 2vh;\n" +
                                    "                                    border: 1px solid #dcdcdc;\n" +
                                    "                                    text-align: left;\n" +
                                    "                                    padding: 1vh;\n" +
                                    "                                    background-color: #fff;\">" + orderDetail.getDiamond().getDiamondName() + "</td>\n" +
                                    "                                    <td style=\"font-size: 2vh;\n" +
                                    "                                    border: 1px solid #dcdcdc;\n" +
                                    "                                    text-align: left;\n" +
                                    "                                    padding: 1vh;\n" +
                                    "                                    background-color: #fff;\">" + nb.format(orderDetail.getDiamond().getTotalPrice()) + " VND</td>\n" +
                                    "                                  </tr>\n";
                    no++;
                }
                if (orderDetail.getProductCustomize() != null) {
                    mail += "                                  <tr>\n" +
                            "                                   <td style=\"font-size: 2vh;\n" +
                            "                                    border: 1px solid #dcdcdc;\n" +
                            "                                    text-align: left;\n" +
                            "                                    padding: 1vh;\n" +
                            "                                    background-color: #fff;\">" + no + "</td>\n" +
                            "                                    <td style=\"font-size: 2vh;\n" +
                            "                                    border: 1px solid #dcdcdc;\n" +
                            "                                    text-align: left;\n" +
                            "                                    padding: 1vh;\n" +
                            "                                    background-color: #fff;\">" + orderDetail.getProductCustomize().getProduct().getProductName() + "</td>\n" +
                            "                                    <td style=\"font-size: 2vh;\n" +
                            "                                    border: 1px solid #dcdcdc;\n" +
                            "                                    text-align: left;\n" +
                            "                                    padding: 1vh;\n" +
                            "                                    background-color: #fff;\">" + nb.format(orderDetail.getProductCustomize().getProduct().getTotalPrice()) + " VND</td>\n" +
                            "                                  </tr>\n";

                    if (orderDetail.getProductCustomize().getDiamond() != null) {
                        mail += "                                  <tr>\n" +
                                "                                   <td style=\"font-size: 2vh;\n" +
                                "                                    border: 1px solid #dcdcdc;\n" +
                                "                                    text-align: left;\n" +
                                "                                    padding: 1vh;\n" +
                                "                                    background-color: #fff;\">" + no + "</td>\n" +
                                "                                    <td style=\"font-size: 2vh;\n" +
                                "                                    border: 1px solid #dcdcdc;\n" +
                                "                                    text-align: left;\n" +
                                "                                    padding: 1vh;\n" +
                                "                                    background-color: #fff;\">" + orderDetail.getProductCustomize().getDiamond().getDiamondName() + "</td>\n" +
                                "                                    <td style=\"font-size: 2vh;\n" +
                                "                                    border: 1px solid #dcdcdc;\n" +
                                "                                    text-align: left;\n" +
                                "                                    padding: 1vh;\n" +
                                "                                    background-color: #fff;\">" + nb.format(orderDetail.getProductCustomize().getDiamond().getTotalPrice()) + " VND</td>\n" +
                                "                                  </tr>\n";
                        no++;
                    }
                }
            }
            mail += "                                  \n" +
                    "                                </tbody>\n" +
                    "                              </table>\n" +
                    "                              <div class=\"flex-table\" style=\"display: flex;\">\n" +
                    "                                <div class=\"flex-column\" style=\" width: 100%;\n" +
                    "  box-sizing: border-box;\"></div>\n" +
                    "                                <div class=\"flex-column\" style=\" width: 100%;\n" +
                    "  box-sizing: border-box;\">\n" +
                    "                                  <table class=\"table-subtotal\" style=\"border-collapse: collapse;\n" +
                    "                                  box-sizing: border-box;\n" +
                    "                                  width: 100%;\n" +
                    "                                  margin-top: 2vh;\">\n" +
                    "                                    <tbody>\n" +
                    "                                      <tr>\n" +
                    "                                        <td style=\" font-size: 2vh;\n" +
                    "                                        border-bottom: 1px solid #dcdcdc;\n" +
                    "                                        text-align: left;\n" +
                    "                                        padding: 1vh;\n" +
                    "                                        background-color: #fff;\">Subtotal</td>\n" +
                    "                                        <td style=\" font-size: 2vh;\n" +
                    "                                        border-bottom: 1px solid #dcdcdc;\n" +
                    "                                        text-align: left;\n" +
                    "                                        padding: 1vh;\n" +
                    "                                        background-color: #fff;\">" + nb.format(order.getPrice() + order.getDiscount()) + " VND</td>\n" +
                    "                                      </tr>\n" +
                    "                                      <tr>\n" +
                    "                                        <td style=\" font-size: 2vh;\n" +
                    "                                        border-bottom: 1px solid #dcdcdc;\n" +
                    "                                        text-align: left;\n" +
                    "                                        padding: 1vh;\n" +
                    "                                        background-color: #fff;\">Discount</td>\n" +
                    "                                        <td style=\" font-size: 2vh;\n" +
                    "                                        border-bottom: 1px solid #dcdcdc;\n" +
                    "                                        text-align: left;\n" +
                    "                                        padding: 1vh;\n" +
                    "                                        background-color: #fff;\"><span style=\"color: red; font-size: 20px;\">-</span>" + nb.format(order.getDiscount()) + " VND ( <span style=\"font-size: 20px; color: red;\">-</span> ? points)</td>\n" +
                    "                                      </tr>\n" +
                    "                                      <tr>\n" +
                    "                                        <td style=\" font-size: 2vh;\n" +
                    "                                        border-bottom: 1px solid #dcdcdc;\n" +
                    "                                        text-align: left;\n" +
                    "                                        padding: 1vh;\n" +
                    "                                        background-color: #fff;\">Tax</td>\n" +
                    "                                        <td style=\" font-size: 2vh;\n" +
                    "                                        border-bottom: 1px solid #dcdcdc;\n" +
                    "                                        text-align: left;\n" +
                    "                                        padding: 1vh;\n" +
                    "                                        background-color: #fff;\">0</td>\n" +
                    "                                      </tr>\n" +
                    "                                      <tr>\n" +
                    "                                        <td style=\" font-size: 2vh;\n" +
                    "                                        border-bottom: 1px solid #dcdcdc;\n" +
                    "                                        text-align: left;\n" +
                    "                                        padding: 1vh;\n" +
                    "                                        background-color: #fff;\">Payment Method</td>\n" +
                    "                                        <td style=\" font-size: 2vh;\n" +
                    "                                        border-bottom: 1px solid #dcdcdc;\n" +
                    "                                        text-align: left;\n" +
                    "                                        padding: 1vh;\n" +
                    "                                        background-color: #fff;\">" + payment.getMethodPayment() + "</td>\n" +
                    "                                      </tr>\n" +
                    "                                      <tr>\n" +
                    "                                        <td style=\" font-size: 2vh;\n" +
                    "                                        border-bottom: 1px solid #dcdcdc;\n" +
                    "                                        text-align: left;\n" +
                    "                                        padding: 1vh;\n" +
                    "                                        background-color: #fff;\">Note</td>\n" +
                    "                                        <td style=\" font-size: 2vh;\n" +
                    "                                        border-bottom: 1px solid #dcdcdc;\n" +
                    "                                        text-align: left;\n" +
                    "                                        padding: 1vh;\n" +
                    "                                        background-color: #fff;\">" + order.getNote() + "</td>\n" +
                    "                                      </tr>\n" +
                    "                                    </tbody>\n" +
                    "                                  </table>\n" +
                    "                                </div>\n" +
                    "                              </div>\n" +
                    "                              <div class=\"invoice-total-amount\" style=\"margin-top: 1rem;\">\n" +
                    "                                <p style=\"font-weight: bold;\n" +
                    "                                color: #0F172A;\n" +
                    "                                text-align: right;\n" +
                    "                                font-size: 22px;\">Total : " + nb.format(order.getPrice()) + " VND</p>\n" +
                    "                              </div>\n" +
                    "                            </div>\n" +
                    "                            <div class=  \"footer  \"     \n" +
                    "                            style=  \"text-align: center; \"    \n" +
                    "                            font-size: 14px;    \n" +
                    "                            color: #777777;    \n" +
                    "                            margin-top: 20px;>    \n" +
                    "                           <p>Copyright &copy; 2019, YOUWORK.TODAY INC</p>    \n" +
                    "                          <h2>Thank you, have a good day .</h2>Group6 Team   \n" +
                    "                       </div> \n" +
                    "                          </div>\n" +
                    "                          \n" +
                    "                        </section>                       \n" +
                    "                      </body>";

            String title = "Invoice";
            String senderName = "Invoice";
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setFrom("hoangsonhadiggory@gmail.com", senderName);
            helper.setTo(order.getEmail());
            helper.setSubject(title);
            helper.setText(mail, true);

            javaMailSender.send(mimeMessage);
            return true;

        } catch (Exception e) {
            log.error("Error at send invoice {}", e.toString());
            return false;
        }
    }

    @Override
    public boolean crawlData() throws InterruptedException {
        ChromeOptions op = new ChromeOptions();
//        op.addArguments("--incognito");
//        op.addArguments("--lang=ja-JP");
        webDriver = new ChromeDriver(op);
        webDriver.manage().window().maximize();
        Thread.sleep(5000);

        webDriver.get("https://google.com");
        Thread.sleep(5000);

        WebElement element = webDriver.findElement(By.id("APjFqb"));
        element.sendKeys("giá dola ngày hôm nay");
        element.submit();

        Thread.sleep(5000);
        WebElement element_price = webDriver.findElement(By.xpath("//span[@class='DFlfde SwHCTb']"));

        String[] split = element_price.getText().split(",");
        String price = split[0];

        Thread.sleep(5000);
        webDriver.quit();

        crawledDataProperties.setDola(price);
        if(crawledDataProperties.getDola() != null && StringUtils.hasText(crawledDataProperties.getDola())) {
            return true;
        }
        return false;
    }

}
