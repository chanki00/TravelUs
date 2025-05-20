create database tripdb;

use tripdb;

create table member (
	id varchar(50) PRIMARY KEY,
    pw varchar(50) NOT NULL,
    name varchar(50) NOT NULL,
    email VARCHAR(45) NOT NULL unique,
    roll VARCHAR(30)
);

