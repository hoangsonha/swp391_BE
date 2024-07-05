[![Build and deploy JAR app to Azure Web App - DiamondShopProject](https://github.com/hoangsonha/swp391_BE/actions/workflows/main_diamondshopproject.yml/badge.svg)](https://github.com/hoangsonha/swp391_BE/actions/workflows/main_diamondshopproject.yml)


![Spring Boot 3.2.5](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)
![Mysql 8.0](https://img.shields.io/badge/Mysql-8.3.0-blue.svg)
![JDK 20](https://img.shields.io/badge/JDK-20-brightgreen.svg)
![Maven](https://img.shields.io/badge/Maven-3.9.7-yellowgreen.svg)
![license](https://img.shields.io/crates/l/rustc-serialize/0.3.24.svg)


# swp391
Dự án quản lí và bán kim cương trực tuyến bên phía BackEnd sử dụng RESTfull API

## How to Run
- Clone the repository:
```bash
git clone https://github.com/hoangsonha/swp391_BE.git
```
- Open in your preferred IDE.<br>
       or
- Navigate to the project directory:
```bash
cd swp391_BE
```
- Once successfully built, you can run the service:
```bash
mvn spring-boot:run
```

### To view Swagger 3 API docs
Run the server and browse to http:www

### Main function

- Authentication and Authorization with Spring Security and password encryption
- JWT for authentication of requests
- Deploy project and MySQL on Azure
- Login with email and password that user registered, google and facebook
- Register, admin can create user with any role but user can not choose role when user register account
- Validation and handle exception for validation
- Recaptcha protect websites from spam and abuse by verifying that users are human
- Send email when user register and click the link to active account
- Payment with Paypal and VNPay by card information is issued by the parties
- Look account when user login failed over 5 times and set over 5 minutes user can login failed 5 times again before looking account
- Send email or SMS with the OTP which has 6 numbers when user want to reset password or forget password
- Search advanced diamonds by 7 properties which are carat, size, price (higher or lower a certain price or price range between start price and end price), color, clarify, shape and sorting price by desc or asc and user can choose any properties
- CRUD user with admin rights
- Get user by role, role manager only view other manager and lower role but can not view user with role admin
- Logout 
- Swagger for viewing API
- Data is uploaded from the database (MySQL) for displaying information
- Automation send email when user not login over 1 month and reset at 8.AM every day
- Automation send Happy Birth Day and automation reset at 8.AM every day

## License & Copyright
&copy; 2024 Hoàng Sơn Hà Licensed under the [Apache License 2.0](https://github.com/hoangsonha/swp391_BE/blob/main/LICENSE).

