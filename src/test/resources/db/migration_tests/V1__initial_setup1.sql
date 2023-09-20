DROP TABLE IF EXISTS students_courses;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS groups;

-- stored function
CREATE OR REPLACE FUNCTION get_students_related_to_course(given_course_name VARCHAR)
RETURNS TABLE (student_id INT, group_id INT, first_name VARCHAR, last_name VARCHAR) AS
$$
BEGIN
    RETURN QUERY
    SELECT
        students.student_id,
        students.group_id,
        students.first_name,
        students.last_name
    FROM
        students
    JOIN
        students_courses ON students.student_id = students_courses.student_id
    JOIN
        courses ON students_courses.course_id = courses.course_id
    WHERE
        courses.course_name = given_course_name;
END;
$$ LANGUAGE plpgsql;

-- stored function
CREATE OR REPLACE FUNCTION get_groups_with_less_students(student_count_limit INT)
RETURNS TABLE (group_id INT, group_name VARCHAR) AS
$$
BEGIN
    RETURN QUERY
    SELECT
        groups.group_id,
        groups.group_name
    FROM
        groups
    LEFT JOIN
        students ON groups.group_id = students.group_id
    GROUP BY
        groups.group_id, groups.group_name
    HAVING
        COUNT(students.student_id) <= student_count_limit;
END;
$$
LANGUAGE plpgsql;

-- groups table
CREATE TABLE groups
(
    group_id   SERIAL PRIMARY KEY,
    group_name VARCHAR(255)
);

-- students table
CREATE TABLE students
(
    student_id SERIAL PRIMARY KEY,
    group_id   INT REFERENCES groups(group_id),
    first_name VARCHAR(50),
    last_name  VARCHAR(50)
);

-- courses table
CREATE TABLE courses
(
    course_id          SERIAL PRIMARY KEY,
    course_name        VARCHAR(100),
    course_description TEXT
);

-- many-to-many relation between students and courses
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
    ('Group !!!');

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
       (5, 'Bruno', 'Erica'),
       (3, 'Zane', 'Jenna'),
       (1, 'Tanisha', 'Todd'),
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
       (1, 'Meghan', 'Hakeem'),
       (6, 'aasfsa', 'fasdf');

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
       (30, 2),
       (31, 6);


