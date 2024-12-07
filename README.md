# Loan-API
Loan API is an REST API that employees can create, list and pay loans for their customers.

## Table of Contents
1. [Features](#features)
2. [Tech Stack](#tech-stack)
3. [Installation](#installation)
4. [Authorization](#authorization)
5. [Database](#database)
6. [Endpoints](#endpoints)
7. [Postman Collection](#postman-collection)

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
   git clone https://github.com/An2-San/Loan-API.git
   ```
2. **Build and run the application**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
3. **Access the API :**
   The API will be available at http://localhost:8085.

## Authorization
- **While ADMIN users can operate for all customers, CUSTOMER role users can operate for themselves.** (Bonus 1)
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
- /loan endpoints will be avaliable for both ADMIN and CUSTOMER users
- /customer endpoints only avaliable for ADMIN users.
  
**1. Create Loan**
  - POST /loan/create
  - Example Request Body: 
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
    - numberOfInstallment: Optional(e.g., 6) - Used for filtering.
    - isPaid: Optional(e.g., false) - Used for filtering
  - Example Response :
```json
[
    {
        "loanId": "aff35212-f0d3-4c06-9bf2-32798678fe0a",
        "loanAmount": 715.00,
        "createDate": "2024-12-07T21:06:07.276719+03:00",
        "numberOfInstallments": 6,
        "isPaid": false
    }
]
```
 
**3. List Loan Installments**
- GET /loan/list-loan-installments
- Query Parameters:
    - customerId: Required.
    - loanId: Required.
    - isPaid: Optional(e.g., false) - Used for filtering
- Example Response Body:
```json
[
    {
        "amount": 119.17,
        "dueDate": "2025-01-01T23:59:00+03:00",
        "isPaid": false
    },
    {
        "amount": 119.17,
        "dueDate": "2025-02-01T23:59:00+03:00",
        "isPaid": false
    },
    {
        "amount": 119.17,
        "dueDate": "2025-03-01T23:59:00+03:00",
        "isPaid": false
    },
    {
        "amount": 119.17,
        "dueDate": "2025-04-01T23:59:00+03:00",
        "isPaid": false
    },
    {
        "amount": 119.17,
        "dueDate": "2025-05-01T23:59:00+03:00",
        "isPaid": false
    },
    {
        "amount": 119.17,
        "dueDate": "2025-06-01T23:59:00+03:00",
        "isPaid": false
    }
]
```
 
**4. Pay Loan**
  - POST /loan/pay-loan
  - Request Body: 
```json
{
    "customerId":"e7f4d254-2ff5-4e5b-9f42-83591b8b7c57",
    "loanId" : "aff35212-f0d3-4c06-9bf2-32798678fe0a",
    "amount" : 1500
}
```
   - Response Body :
```json
{
    "numberOfInstallmentsPaid": 2,
    "totalAmountSpent": 228.69,
    "isPaidCompletely": false
}
```
   - Installments are paid wholly or not at all.
   - Payments prioritize the earliest installments first.
   - Payments cannot cover installments more than 3 months in the future.
   - If an installment is paid before due date , there will be discount (Bonus 2).
   - If an installment is paid after due date , there will be penalty (Bonus 2).

**4. Create Customer**
   - POST /customer/create
   - Request Body :
```json
{
    "name" : "Furkan Enes",
    "surname" : "Genç",
    "creditLimit" : 6000
}
```
   - Response Body:
```json
{
    "id": "c391987f-dba7-49d6-bfa8-38edce627256",
    "name": "Furkan Enes",
    "surname": "Genç",
    "creditLimit": 6000,
    "usedCreditLimit": null
}
```
**5. List Customers**
   - GET /customer/list-customers
   - Response Body :
```json
[
    {
        "id": "e7f4d254-2ff5-4e5b-9f42-83591b8b7c57",
        "name": "John",
        "surname": "Doe",
        "creditLimit": 5000.00,
        "usedCreditLimit": 0.00
    },
    {
        "id": "c391987f-dba7-49d6-bfa8-38edce627256",
        "name": "Furkan Enes",
        "surname": "Genç",
        "creditLimit": 6000.00,
        "usedCreditLimit": null
    }
]
```

## Postman Collection

You can download postman collection from here : 

[Loan API.postman_collection.json](https://github.com/user-attachments/files/18049496/Loan.API.postman_collection.json)
