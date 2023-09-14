DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS courses;

CREATE TABLE groups
(
    group_id   INT AUTO_INCREMENT PRIMARY KEY,
    group_name VARCHAR(255) NOT NULL
);

-- Sample data for groups table

CREATE TABLE students
(
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR_IGNORECASE(255) NOT NULL,
    last_name  VARCHAR_IGNORECASE(255) NOT NULL
);

-- Sample data for students table
INSERT INTO students (first_name, last_name)
VALUES ( 'John', 'Doe'),
       ('Jane', 'Smith'),
       ( 'Alice', 'Johnson'),
       ( 'Bob', 'Williams'),
       ( 'Eva', 'Brown');

CREATE TABLE courses
(
    course_name        VARCHAR(255) NOT NULL,
    course_description VARCHAR(255) NOT NULL
);

INSERT INTO groups (group_name)
VALUES ('Group A'),
       ('Group B'),
       ('Group C');

-- Sample data for courses table
INSERT INTO courses (course_name, course_description)
VALUES ('Mathematics', 'Introduction to mathematics'),
       ('Biology', 'Biology fundamentals'),
       ('Physics', 'Basic principles of physics');