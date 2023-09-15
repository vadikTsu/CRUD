package ua.com.foxminded.repository;

import ua.com.foxminded.dto.Group;
import ua.com.foxminded.dto.Student;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class EmploeeRepository {

    private final DataSource dataSource;
    private static final Logger LOGGER = Logger.getLogger(EmploeeRepository.class.getName());

    private static final String FIND_ALL_GROUPS_WHERE_LESS_STUDENTS_THAN ="SELECT * FROM get_groups_with_less_students(?);";
//                "SELECT " +
//                    "groups.group_id, " +
//                    "groups.group_name " +
//                    "FROM " +
//                        "groups " +
//                    "LEFT JOIN " +
//                        "students ON groups.group_id = students.group_id " +
//                    "GROUP BY " +
//                        "groups.group_id, groups.group_name " +
//                    "HAVING " +
//                        "COUNT(students.student_id) <= ? ;";

    private static final String FIND_ALL_STUDENTS_RELATED_TO_THE_COURSE = "SELECT * FROM find_students_related_to_course(?);";
//            "SELECT " +
//                    "students.student_id, " +
//                    "students.group_id, " +
//                    "students.first_name, " +
//                    "students.last_name " +
//            "FROM " +
//                "students " +
//            "JOIN " +
//                "students_courses ON students.student_id = students_courses.student_id " +
//            "JOIN " +
//                "courses ON students_courses.course_id = courses.course_id " +
//            "WHERE courses.course_name = ?;";
    private static final String ADD_NEW_STUDENT =
            "INSERT INTO students(group_id, first_name, last_name) " +
                    "VALUES ((SELECT group_id  FROM groups WHERE groups.group_id = ?), ?, ?)";

    private static final String DELETE_STUDENT_BY_ID =
            "DELETE * FROM students WHERE students.student_id = ? " +
            "DELETE * FROM students_courses WHERE students_courses.student_id = ?;";

    private static final String REMOVE_STUDENT_FROM_COURSE =
            "DELETE * FROM students_courses WHERE students_courses.student_id = ? AND students_courses.course_name = ?;";

    public EmploeeRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Group> getAllGroupsWithLessOrEqualStudents(int numberOfStudents) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             CallableStatement preparedStatement = connection.prepareCall(FIND_ALL_GROUPS_WHERE_LESS_STUDENTS_THAN)) {
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

    public void addNewStudent(Student student) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_NEW_STUDENT)) {
            preparedStatement.setInt(1, student.getGroupId());
            preparedStatement.setString(2, student.getFirstName());
            preparedStatement.setString(3, student.getLastName());
            preparedStatement.executeUpdate();
        }
    }

    public void addNewStudents(List<Student> students) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_NEW_STUDENT)) {
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
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_STUDENT_BY_ID)) {
            preparedStatement.setInt(1, stuedntId);
            preparedStatement.setInt(2, stuedntId);
            preparedStatement.executeUpdate();
        }
    }

    public void reomoveStudentFromCourse(int stuedntId, String courseName) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_STUDENT_FROM_COURSE)) {
            preparedStatement.setInt(1, stuedntId);
            preparedStatement.setString(1, courseName);
            preparedStatement.executeUpdate();
        }
    }
}