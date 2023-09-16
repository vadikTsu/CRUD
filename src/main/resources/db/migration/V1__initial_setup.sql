DROP TABLE IF EXISTS students_courses;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS groups;

-- Create the groups table
CREATE TABLE groups
(
    group_id   SERIAL PRIMARY KEY,
    group_name VARCHAR(255)
);

-- Create the students table
CREATE TABLE students
(
    student_id SERIAL PRIMARY KEY,
    group_id   INT REFERENCES groups(group_id),
    first_name VARCHAR(50),
    last_name  VARCHAR(50)
);

-- Create the courses table
CREATE TABLE courses
(
    course_id          SERIAL PRIMARY KEY,
    course_name        VARCHAR(100),
    course_description TEXT
);

-- Create the many-to-many relation between students and courses
CREATE TABLE students_courses
(
    student_id INT,
    course_id  INT,
    PRIMARY KEY (student_id, course_id)
);

INSERT INTO groups (group_name)
VALUES
    ('Group 1'),
    ('Group 2'),
    ('Group 3'),
    ('Group 4'),
    ('Group 5');

-- Insert data into the students table
INSERT INTO students (group_id, first_name, last_name)
VALUES
    (1, 'John', 'Doe'),
    (1, 'Jane', 'Smith'),
    (2, 'Alice', 'Johnson'),
    (2, 'Bob', 'Brown'),
    (3, 'Eva', 'White'),
    (3, 'Chris', 'Lee'),
    (4, 'Anna', 'Clark'),
    (4, 'David', 'Walker'),
    (5, 'Emily', 'Taylor'),
    (5, 'Frank', 'Harris');

-- Insert data into the courses table
INSERT INTO courses (course_name, course_description)
VALUES
    ('Math', 'Mathematics Course'),
    ('Biology', 'Biology Course'),
    ('Physics', 'Physics Course'),
    ('History', 'History Course'),
    ('Chemistry', 'Chemistry Course');


-- Generate random student-course relationships
INSERT INTO students_courses (student_id, course_id)
SELECT
    student_id,
    course_id
FROM
    (SELECT
         student_id,
         course_id,
         ROW_NUMBER() OVER (PARTITION BY student_id ORDER BY RANDOM()) AS row_num
     FROM
         students
             CROSS JOIN
         (SELECT course_id FROM courses) AS course_ids) AS subquery
WHERE
        row_num <= (FLOOR(RANDOM() * 3) + 1);

