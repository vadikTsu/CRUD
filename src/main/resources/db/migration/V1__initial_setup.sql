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
    student_id INT references students(student_id),
    course_id  INT references courses(course_id),
    PRIMARY KEY (student_id, course_id)
);

INSERT INTO groups (group_name)
VALUES
    ('Group 1'),
    ('Group 2'),
    ('Group 3'),
    ('Group 4'),
    ('Group 5'),
    ('Group 6'),
    ('Group 7'),
    ('Group 8'),
    ('Group 9'),
    ('Group 10');

-- Insert data into the students table
INSERT INTO students (group_id, first_name, last_name)
VALUES (1, 'John', 'Doe'),
       (1, 'Jane', 'Smith'),
       (2, 'Alice', 'Johnson'),
       (2, 'Bob', 'Brown'),
       (3, 'Eva', 'White'),
       (3, 'Chris', 'Lee'),
       (4, 'Anna', 'Clark'),
       (4, 'David', 'Walker'),
       (5, 'Emily', 'Taylor'),
       (5, 'Frank', 'Harris'),
       (6, 'Bruno', 'Erica'),
       (6, 'Zane', 'Jenna'),
       (7, 'Tanisha', 'Todd'),
       (4, 'Abbot', 'Eagan'),
       (2, 'Hayes', 'Harper'),
       (1, 'Adele', 'Ramona'),
       (2, 'Felicia', 'Kimberley'),
       (4, 'Brian', 'Harriet'),
       (5, 'Catherine', 'Piper'),
       (2, 'Anthony', 'Michelle'),
       (5, 'Kylynn', 'Brielle'),
       (1, 'David', 'Tobias'),
       (3, 'Yeo', 'Hamilton'),
       (3, 'Mechelle', 'Amaya'),
       (4, 'Summer', 'Jordan'),
       (2, 'Scott', 'Jolene'),
       (5, 'Alvin', 'Dante'),
       (3, 'Yardley', 'Basia'),
       (5, 'Adrienne', 'Jonah'),
       (1, 'Meghan', 'Hakeem');

-- INSERT INTO students (group_id, first_name, last_name)
-- SELECT
--     (random() * 10 + 1)::int AS group_id,
--         (SELECT first_name FROM (SELECT unnest('{John, Alice, Eva, Anna, Emily, Michael, Sara, Mark, Laura, Alex}'::text[])) AS first_names ORDER BY random() LIMIT 1),
--     (SELECT last_name FROM (SELECT unnest('{Smith, Johnson, Lee, Clark, Taylor, Brown, White, Harris, Davis, Miller}'::text[])) AS last_names ORDER BY random() LIMIT 1)
-- FROM generate_series(1, 10);

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
VALUES (1, 3),
       (2, 5),
       (3, 1),
       (4, 2),
       (5, 3),
       (6, 1),
       (7, 5),
       (8, 5),
       (9, 2),
       (10, 1),
       (11, 2),
       (12, 3),
       (13, 2),
       (14, 3),
       (15, 2),
       (16, 1),
       (17, 5),
       (18, 2),
       (19, 1),
       (20, 3),
       (21, 1),
       (22, 1),
       (23, 3),
       (24, 2),
       (25, 3),
       (26, 2),
       (27, 5),
       (28, 4),
       (29, 3),
       (30, 2);

