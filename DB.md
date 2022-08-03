# PSQL Guide

LiLA uses postgresql as DBMS. Here's the structure and user spec for IPU.

## 1.DBMS Setup
### 1.1. PostgreSQL Setup
> Run below command in order to install PostgreSQL to your server.   
> `sudo apt-get update`   
> `sudo apt-get install postgresql postgresql-contrib`
### 1.2 USER setup
> `CREATE USER <USERNAME> WITH ENCRYPTED PASSWORD '<Password>';`

## 2. Create Database and Tables
### 2.1. Create database
> `CREATE DATABASE lila OWNER <USERNAME>;`
### 2.2. Create tables
> ```sql
> CREATE TABLE IF NOT EXISTS users(
> user_code SERIAL NOT NULL,
> user_id VARCHAR(256) PRIMARY KEY NOT NULL,
> refresh_token VARCHAR(256) NOT NULL,
> user_name VARCHAR(250) NOT NULL,
> email VARCHAR(250) NOT NULL,
> auth_from smallint NOT NULL,
> join_date TIMESTAMP WITH TIME ZONE NOT NULL,
> last_login TIMESTAMP WITH TIME ZONE
> );
)```
