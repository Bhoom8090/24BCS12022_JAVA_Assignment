-- Optional: Hibernate creates this table automatically (ddl-auto=update).
-- Use this script only if you want to create everything manually in MySQL Workbench.

CREATE DATABASE IF NOT EXISTS student_db;
USE student_db;

CREATE TABLE IF NOT EXISTS students (
    id      BIGINT       NOT NULL AUTO_INCREMENT,
    name    VARCHAR(255) NOT NULL,
    email   VARCHAR(255) NOT NULL UNIQUE,
    course  VARCHAR(255) NOT NULL,
    age     INT          NOT NULL,
    PRIMARY KEY (id)
);

-- sample data (optional)
INSERT INTO students (name, email, course, age) VALUES
('Rahul Sharma', 'rahul@example.com', 'Computer Science', 20),
('Priya Verma',  'priya@example.com', 'Information Technology', 21),
('Amit Singh',   'amit@example.com',  'Computer Science', 22);
