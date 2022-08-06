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
> auth_from SMALLINT NOT NULL,
> join_date TIMESTAMP WITH TIME ZONE NOT NULL,
> last_login TIMESTAMP WITH TIME ZONE,
> privilege CHAR(5) NOT NULL
> );
> ```
> 
> ```sql
> CREATE TABLE IF NOT EXISTS problems(
> problem_code SERIAL NOT NULL,
> problem_name VARCHAR(50) NOT NULL,
> content TEXT NOT NULL,
> solution TEXT NOT NULL,
> answer TEXT NOT NULL,
> tags TEXT NOT NULL,
> difficulty SMALLINT NOT NULL,
> branch SMALLINT NOT NULL,
> status SMALLINT NOT NULL,
> );
> ```
>
> ```sql 
> CREATE TABLE IF NOT EXISTS resources(
> resource_code CHAR(24) PRIMARY KEY,
> resource BYTEA NOT NULL,
> registered TIMESTAMP WITH TIME ZONE NOT NULL,
> registered_by VARCHAR(50) NOT NULL,
> resource_name VARCHAR(50) NOT NULL
> );
> ```

## 3. Grant required privileges
> ```sql
> GRANT ALL PRIVILEGES ON users TO <USERNAME>;
> ALTER TABLE users OWNER TO lila_user;
> ```
