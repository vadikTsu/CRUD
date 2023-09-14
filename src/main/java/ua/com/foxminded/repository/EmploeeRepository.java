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

    public EmploeeRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void getStudentsData() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String query = "SELECT * FROM students";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
//                    int studentId = resultSet.getInt("student_id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
//                    System.out.println("Student ID: " + studentId);
                    System.out.println("First Name: " + firstName);
                    System.out.println("Last Name: " + lastName);
                    System.out.println("------------------------");
                }
            }
        }
    }


    public List<Group> getAllGroupsWithLessOrEqualStudents(int numberOfStudents) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            //  todo  a. Find all groups with less or equal students’ number
            String query = "SELECT * FROM groups";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Group> groups = new ArrayList<>();
                while (resultSet.next()) {
                    int groupId = resultSet.getInt("group_id");
                    String groupName = resultSet.getString("grpup_name");
                    groups.add(new Group(groupId, groupName));
                }
                return groups;
            }
        }
    }

    public List<Student> findAllStudentsRelatedToTheCourse(String courseName) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            //todo Find all students related to the course with the given name
            String query = "SELECT * FROM groups";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Student> students = new ArrayList<>();
                while (resultSet.next()) {
                    int studentId = resultSet.getInt("");
                    int groupId = resultSet.getInt("");
                    String firstName = resultSet.getString("");
                    String lastName = resultSet.getString("");
                    students.add(new Student(studentId, groupId, firstName, lastName));
                }
                return students;
            }
        }
    }


}