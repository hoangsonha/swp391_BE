[![Build and deploy JAR app to Azure Web App - DiamondShopProject](https://github.com/hoangsonha/swp391_BE/actions/workflows/main_diamondshopproject.yml/badge.svg)](https://github.com/hoangsonha/swp391_BE/actions/workflows/main_diamondshopproject.yml)


![Spring Boot 3.2.5](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)
![Mysql 8.0](https://img.shields.io/badge/Mysql-8.3.0-blue.svg)
![JDK 20](https://img.shields.io/badge/JDK-20-brightgreen.svg)
![Maven](https://img.shields.io/badge/Maven-3.9.7-yellowgreen.svg)
![license](https://img.shields.io/crates/l/rustc-serialize/0.3.24.svg)


# SWP391
Build a webapp to manager diamond shop with RESTfull API and Repository Pattern architecture

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

### To view Swagger 3 API

- Run the server and browse to https://diamondshopproject.azurewebsites.net/swagger-ui/index.html
- Run the development and browse to http://localhost:8080/swagger-ui/index.html

### Main function

- Authentication and Authorization with Spring Security and password encryption
- JWT for authentication of requests
- Deploy project and MySQL on Azure
- Login with Google, Facebook and email - password that user registered,
- Validation (Handle Exceptions)
- Config Firebase to store Image for User
- Crawl data from browser to collect data
- Register that admin can create user with any role but user can not choose role when user register account
- Validation and handle exception for validation
- Recaptcha protect websites from spam and abuse by verifying that users are human
- Send email when user register and click the link to active account
- Payment with Paypal and VNPay by card information is issued by the parties and when user payment successfully who will receiver a email for invoice
- Refund with paypal
- Look account when user login failed over 5 times and set over 5 minutes user can login failed 5 times again before looking account and when user login failed 6 times, account is locked
- Send email or SMS with the OTP which has 6 numbers when user want to reset password or forget password
- Search advanced diamonds by 7 properties which are carat, size, price (higher or lower a certain price or price range between start price and end price), color, clarify, shape and sorting price by desc or asc and user can choose any properties with Specification and Criteria API
- CRUD user with admin rights
- Get user by role, role manager only view other manager and lower role but can not view user with role admin
- Swagger for API
- Data is uploaded from the database (MySQL) for displaying information
- Automation send email when user not login over 1 month and reset at 8.AM every day
- Automation send Happy Birth Day and automation reset at 8.AM every day
- Export reporting of user to excel, html, pdf
- Logout

## License & Copyright
&copy; 2024 Hoàng Sơn Hà Licensed under the [Apache License 2.0](https://github.com/hoangsonha/swp391_BE/blob/main/LICENSE).

