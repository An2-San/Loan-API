# Loan-API
Loan API is an REST API that employees can create, list and pay loans for their customers.

# Class Diagram :
![image](https://github.com/user-attachments/assets/a3ad4fec-f706-41f6-b660-da6ed0ba445a)


# Database
H2 Database is used.

Driver Class : org.h2.Driver

JDBC URL : jdbc:h2:mem:testdb

User Name : sa

Password : password

# How To Use
When application build ; a customer will be inserted with id : "e7f4d254-2ff5-4e5b-9f42-83591b8b7c57"

You can access endpoints : http://localhost:8085/ 

Used basic authentication for accessing endpoints.

**While ADMIN users can operate for all customers, CUSTOMER role users can operate for themselves.**

To access all endpoints without auhtorization problem use these credentials:

username : admin , password : admin_secret

To test customer authorization use the customer's default id as username , and password (all customer's password info is the same for test purposes).

username : e7f4d254-2ff5-4e5b-9f42-83591b8b7c57 , password : admin_secret
