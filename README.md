# Loan-API
Loan API is an REST API that employees can create, list and pay loans for their customers.

## Table of Contents
1. [Features](#features)
2. [Tech Stack](#tech-stack)
3. [Installation](#installation)
4. [Authorization](#authorization)
5. [Database](#database)
6. [Endpoints](#endpoints)

## Features
- Create Loan : Create a loan for a given customer.
- List Loans: View all loans for a given customer.
- List Loan Installments : View all loan installments for given loan.
- Pay Loan : Pay loan installments with the support of bulk payments.
- User authorization with basic authentication for both ADMIN and CUSTOMER roles.

## Tech Stack
- **Java 17**
- **Spring Boot 3.4.0**
- **Maven**
- **H2 Database**

## Installation
1. **Clone the repository:**
   ```bash
   git clone https://github.com/username/loan-api.git](https://github.com/An2-San/Loan-API.git
   cd loan-api
   ```
2. **Build and run the application**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
3. **Access the API :**
   The API will be available at http://localhost:8085.

## Authorization
- **While ADMIN users can operate for all customers, CUSTOMER role users can operate for themselves.**
- ADMIN users can also access customer enpoints. 
- **ADMIN credentials : Username : admin , Password : admin_secret**
- **CUSTOMER credentials : Username : {customerId}, Password : admin_secret** (for CUSTOMER role, password is the same with the ADMIN role for test purposes)

## Database
- You can access h2 database at : http://localhost:8085/h2-console.
- Note : **When application built a customer will be inserted with id : "e7f4d254-2ff5-4e5b-9f42-83591b8b7c57".**
- H2 Database Access Credentials:
  - Driver Class : org.h2.Driver
  - JDBC URL : jdbc:h2:mem:testdb
  - User Name : sa
  - Password : password


## Endpoints
**1. Create Loan**
  - POST /loan/create
  - Request Body: 
```json
{
    "customerId" : "e7f4d254-2ff5-4e5b-9f42-83591b8b7c57",
    "loanAmount" : 550,
    "interestRate" : 0.3,
    "numberOfInstallments" : 6
}
```
- **Validation Rules:**
  - Customer must have sufficient limit for the loan.
  - Valid numberOfInstallments: 6, 9, 12, 24.
  - Valid interestRate interval: 0.1 to 0.5.
 
  
**2. List Loans**
- GET /loan/list-loans
- Query Parameters:
    - customerId: Required.
    - installments: Optional(e.g., 6) - Used for filtering.
    - isPaid: Optional(e.g., false) - Used for filtering
 
**3. List Loan Installments**
- GET /loan/list-loan-installments
- Query Parameters:
    - customerId: Required.
    - loanId: Required.
    - isPaid: Optional(e.g., false) - Used for filtering
 
**4. Pay Loan**
  - POST /loan/pay-loan
  - Request Body: 
```json
{
    "customerId":"e7f4d254-2ff5-4e5b-9f42-83591b8b7c57",
    "loanId" : {{loanId}},
    "amount" : 1500
}
```





