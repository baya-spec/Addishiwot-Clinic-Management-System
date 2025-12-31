CREATE DATABASE IF NOT EXISTS `AddisHiwot-Clinic`;
USE `AddisHiwot-Clinic`;

CREATE TABLE IF NOT EXISTS patients (
    id VARCHAR(20) PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    second_name VARCHAR(50),
    last_name VARCHAR(50) NOT NULL,
    age INT,
    gender VARCHAR(10),
    address VARCHAR(255),
    phone_number VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS doctors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    specialty VARCHAR(100),
    available_days VARCHAR(255), -- Stored as comma-separated string
    available_time VARCHAR(255),
    time_per_patient VARCHAR(50),
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT FALSE,
    registration_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS examiners (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS laboratory_staff (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS pharmacy_staff (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL -- 'ADMIN', 'RECEPTION'
);

-- Default users with corrected, standardized hashed passwords
-- Admin / Admin@123
INSERT INTO users (username, password, role) VALUES ('Admin', 'XmDVHiApJJlHLAMhc9MzIXWqIViQM4iVlZFRa5M4AlE=', 'ADMIN') ON DUPLICATE KEY UPDATE username='Admin', password='XmDVHiApJJlHLAMhc9MzIXWqIViQM4iVlZFRa5M4AlE=';
-- Reception / Reception@123
INSERT INTO users (username, password, role) VALUES ('Reception', 'BQPjFHVVtGmZZTHDlHsZQ0A0BYMkUxuHR0SEVUKKIRQ=', 'RECEPTION') ON DUPLICATE KEY UPDATE username='Reception', password='BQPjFHVVtGmZZTHDlHsZQ0A0BYMkUxuHR0SEVUKKIRQ=';
