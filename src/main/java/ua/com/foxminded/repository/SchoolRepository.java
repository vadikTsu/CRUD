package ua.com.foxminded.repository;

import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.foxminded.dto.Group;
import ua.com.foxminded.dto.Student;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// todo exception handing
public class SchoolRepository {

    private final DataSource dataSource;
    private static final Logger logger = LoggerFactory.getLogger(SchoolRepository.class);

    public SchoolRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Group> getAllGroupsWithLessOrEqualStudents(int numberOfStudents) {
        logger.info("getAllGroupsWithLessOrEqualStudents invoked");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT * FROM get_groups_with_less_students(?);")) {
            logger.info("Connection to DB established");
            preparedStatement.setInt(1, numberOfStudents);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                logger.info("Sent query to DB, accepting response");
                List<Group> groups = new ArrayList<>();
                while (resultSet.next()) {
                    int groupId = resultSet.getInt("group_id");
                    String groupName = resultSet.getString("group_name");
                    groups.add(new Group(groupId, groupName));
                }
                return groups;
            } catch (PSQLException exception) {
                logger.error(exception.getClass().getName() + " catched with code " + exception.getErrorCode());
                logger.error(exception.getMessage());
                logger.info("Exception occurred while accepting response");
            }
        } catch (SQLException exception) {
            logger.error(exception.getClass().getName() + " catched with code " + exception.getErrorCode());
            logger.error(exception.getMessage());
            logger.info("Exception occurred while connecting to database");
        }
        return Collections.emptyList();
    }

    public List<Student> findAllStudentsRelatedToTheCourse(String courseName) {
        logger.info("findAllStudentsRelatedToTheCourse invoked");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM find_students_related_to_course(?);")) {
            logger.info("Connection to DB established");
            preparedStatement.setString(1, courseName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                logger.info("Sent query to DB, accepting response");
                List<Student> students = new ArrayList<>();
                while (resultSet.next()) {
                    int studentId = resultSet.getInt("student_id");
                    int groupId = resultSet.getInt("group_id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    students.add(new Student(studentId, groupId, firstName, lastName));
                }
                return students;
            } catch (PSQLException exception) {
                logger.error(exception.getClass().getName() + " catched with code " + exception.getErrorCode());
                logger.error(exception.getMessage());
                logger.info("Exception occurred while accepting response");
            }
        } catch (SQLException exception) {
            logger.error(exception.getClass().getName() + " catched with code " + exception.getErrorCode());
            logger.error(exception.getMessage());
            logger.info("Exception occurred while connecting to database");
        }
        return Collections.emptyList();
    }

    public void addNewStudent(Student student) {
        logger.info("addNewStudent invoked");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO students(group_id, first_name, last_name) " +
                             "VALUES ((SELECT group_id  FROM groups WHERE groups.group_id = ?), ?, ?)")) {
            logger.info("Connection to DB established");
            preparedStatement.setInt(1, student.getGroupId());
            preparedStatement.setString(2, student.getFirstName());
            preparedStatement.setString(3, student.getLastName());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            logger.error(exception.getClass().getName() + " catched with code " + exception.getErrorCode());
            logger.error(exception.getMessage());
        }
    }

    public void addNewStudents(List<Student> students) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO students(group_id, first_name, last_name) " +
                             "VALUES ((SELECT group_id  FROM groups WHERE groups.group_id = ?), ?, ?)")) {
            logger.info("Connection to DB established");
            for (Student student : students) {
                preparedStatement.setInt(1, student.getGroupId());
                preparedStatement.setString(2, student.getFirstName());
                preparedStatement.setString(3, student.getLastName());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException exception) {
            logger.error(exception.getClass().getName() + " catched with code " + exception.getErrorCode());
            logger.error(exception.getMessage());
        }
    }

    public void deleteStudent(int stuedntId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM students WHERE student_id = ?;" +
                             "DELETE FROM students_courses WHERE student_id = ?;")) {
            preparedStatement.setInt(1, stuedntId);
            preparedStatement.setInt(2, stuedntId);
            System.out.println(preparedStatement.executeUpdate());
        } catch (SQLException exception) {
            logger.error(exception.getClass().getName() + " catched with code " + exception.getErrorCode());
            logger.error(exception.getMessage());
        }
    }

    public void reomoveStudentFromCourse(int stuedntId, int courseName) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM students_courses " +
                             "WHERE student_id = ? " +
                             "  AND course_id = ? ;")) {
            preparedStatement.setInt(1, stuedntId);
            preparedStatement.setInt(2, courseName);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            logger.error(exception.getClass().getName() + " catched with code " + exception.getErrorCode());
            logger.error(exception.getMessage());
        }
    }
}
