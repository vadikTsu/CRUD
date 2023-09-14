package ua.com.foxminded.repository;

import ua.com.foxminded.dto.Group;
import ua.com.foxminded.dto.Student;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmploeeRepository {

    private final DataSource dataSource;

    private static final String FIND_ALL_GROUPS_WHERE_LESS_STUDENTS_THAN =
                "SELECT " +
                    "groups.group_id, " +
                    "groups.group_name " +
                    "FROM " +
                        "groups " +
                    "LEFT JOIN " +
                        "students ON groups.group_id = students.group_id " +
                    "GROUP BY " +
                        "groups.group_id, groups.group_name " +
                    "HAVING " +
                        "COUNT(students.student_id) <= ? ;";

    private static final String FIND_ALL_STUDENTS_RELATED_TO_THE_COURSE =
            "SELECT " +
                    "students.student_id, " +
                    "students.group_id, " +
                    "students.first_name, " +
                    "students.last_name " +
            "FROM " +
                "students " +
            "JOIN " +
                "students_courses ON students.student_id = students_courses.student_id " +
            "JOIN " +
                "courses ON students_courses.course_id = courses.course_id " +
            "WHERE courses.course_name = ?;";

    public EmploeeRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void getStudentsData() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String query = "SELECT * FROM students";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int studentId = resultSet.getInt("student_id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    System.out.println("Student ID: " + studentId);
                    System.out.println("First Name: " + firstName);
                    System.out.println("Last Name: " + lastName);
                    System.out.println("------------------------");
                }
            }
        }
    }


    public List<Group> getAllGroupsWithLessOrEqualStudents(int numberOfStudents) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_GROUPS_WHERE_LESS_STUDENTS_THAN)) {
            preparedStatement.setInt(1, numberOfStudents);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Group> groups = new ArrayList<>();
                while (resultSet.next()) {
                    int groupId = resultSet.getInt("group_id");
                    String groupName = resultSet.getString("group_name");
                    groups.add(new Group(groupId, groupName));
                }
                return groups;
            }
        }
    }

    public List<Student> findAllStudentsRelatedToTheCourse(String courseName) throws SQLException {
        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_STUDENTS_RELATED_TO_THE_COURSE)) {
            preparedStatement.setString(1, courseName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Student> students = new ArrayList<>();
                while (resultSet.next()) {
                    int studentId = resultSet.getInt("student_id");
                    int groupId = resultSet.getInt("group_id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    students.add(new Student(studentId, groupId, firstName, lastName));
                }
                return students;
            }
        }
    }
}