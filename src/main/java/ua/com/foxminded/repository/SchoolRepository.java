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

public class SchoolRepository {

    private final DataSource dataSource;

    public SchoolRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Group> getAllGroupsWithLessOrEqualStudents(int numberOfStudents) throws SQLException {
        String query = "SELECT * FROM get_groups_with_less_students(?);";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
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
        String query = "SELECT * FROM find_students_related_to_course(?);";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
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

    public void addNewStudent(Student student) throws SQLException {
        String query =
            "INSERT INTO students(group_id, first_name, last_name) " +
            "VALUES ((SELECT group_id  FROM groups WHERE groups.group_id = ?), ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, student.getGroupId());
            preparedStatement.setString(2, student.getFirstName());
            preparedStatement.setString(3, student.getLastName());
            preparedStatement.executeUpdate();
        }
    }


    public void addNewStudents(List<Student> students) throws SQLException {
        String query = "INSERT INTO students(group_id, first_name, last_name) " +
            "VALUES ((SELECT group_id  FROM groups WHERE groups.group_id = ?), ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (Student student : students) {
                preparedStatement.setInt(1, student.getGroupId());
                preparedStatement.setString(2, student.getFirstName());
                preparedStatement.setString(3, student.getLastName());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }

    }

    public void deleteStudent(int stuedntId) throws SQLException {
        String query = "DELETE FROM students WHERE student_id = ?;" +
                       " DELETE FROM students_courses WHERE student_id = ?;";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, stuedntId);
            preparedStatement.setInt(2, stuedntId);
            System.out.println(preparedStatement.executeUpdate());
        }
    }

    public void reomoveStudentFromCourse(int stuedntId, int courseName) throws SQLException {
        String query = "DELETE FROM students_courses WHERE student_id = ? AND course_id = ? ;";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, stuedntId);
            preparedStatement.setInt(2, courseName);
            preparedStatement.executeUpdate();
        }
    }
}
