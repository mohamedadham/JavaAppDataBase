create database employee;  
use employee;  
create table emp(
	id int NOT NULL AUTO_INCREMENT,
    FirstName varchar(255),
    MiddleName varchar(255),
    LastName varchar(255),
    Email varchar(255),
    PhoneNumber varchar(255),
    PRIMARY KEY (id)
);