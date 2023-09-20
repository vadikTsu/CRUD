package ua.com.foxminded.repository.impl;

import ua.com.foxminded.dto.Course;
import ua.com.foxminded.dto.Group;
import ua.com.foxminded.dto.Student;
import ua.com.foxminded.repository.interfaces.CourseDao;
import ua.com.foxminded.repository.interfaces.GroupDao;
import ua.com.foxminded.repository.interfaces.StudentDao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SchoolRepository implements StudentDao, CourseDao, GroupDao {

    private final DataSource dataSource;

    public SchoolRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Fetches groups from database with less or equals number of students.
     * This method executes stored function get_groups_with_less_students on database server.
     *
     * @param numberOfStudents
     * @return
     * @throws SQLException
     */
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

    /**
     * Fetches all students related to certain course.
     * This method executes stored function get_students_related_to_course on database server.
     *
     * @param courseName
     * @return
     * @throws SQLException
     */
    public List<Student> getAllStudentsRelatedToTheCourse(String courseName) throws SQLException {
        String query = "SELECT * FROM get_students_related_to_course(?);";
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

    /**
     * Removes student from one f his courses.
     *
     * @param stuedntId valid student`s id
     * @param courseId  valid course`s id
     * @return int (number of changes in database).
     * @throws SQLException
     */
    public int removeStudentFromCourse(int stuedntId, int courseId) throws SQLException {
        String query = "DELETE FROM students_courses WHERE student_id = ? AND course_id = ? ;";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, stuedntId);
            preparedStatement.setInt(2, courseId);
            int changes = preparedStatement.executeUpdate();
            return changes;
        }
    }

    @Override
    public List<Student> getAllStudents() throws SQLException {
        String query = "SELECT * FROM students";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
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

    @Override
    public int addNewStudent(Student student) throws SQLException {
        String query =
            "INSERT INTO students(group_id, first_name, last_name) " +
                "VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            if (student.getFirstName() == null || student.getLastName() == null) {
                throw new RuntimeException("invalid student`s data");
            }
            preparedStatement.setInt(1, student.getGroupId());
            preparedStatement.setString(2, student.getFirstName());
            preparedStatement.setString(3, student.getLastName());
            int changes = preparedStatement.executeUpdate();
            return changes;
        }
    }

    @Override
    public int[] addNewStudent(List<Student> students) throws SQLException {
        String query =
            "INSERT INTO students(group_id, first_name, last_name) " +
                "VALUES (?, ?, ?);";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (Student student : students) {
                if (student.getFirstName() == null || student.getLastName() == null) {
                    throw new RuntimeException("invalid student`s data");
                }
                preparedStatement.setInt(1, student.getGroupId());
                preparedStatement.setString(2, student.getFirstName());
                preparedStatement.setString(3, student.getLastName());
                preparedStatement.addBatch();
            }
            int[] changes = preparedStatement.executeBatch();
            return changes;
        }
    }

    @Override
    public int deleteStudent(int stuedntId) throws SQLException {
        String query = " DELETE FROM students_courses WHERE student_id = ?;" +
            "DELETE FROM students WHERE student_id = ?;";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, stuedntId);
            preparedStatement.setInt(2, stuedntId);
            int changes = preparedStatement.executeUpdate();
            return changes;
        }
    }

    @Override
    public List<Group> getAllGroups() throws SQLException {
        String query = "SELECT * FROM groups";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<Group> groups = new ArrayList<>();
            while (resultSet.next()) {
                int groupId = resultSet.getInt("group_id");
                String groupName = resultSet.getString("group_name");
                groups.add(new Group(groupId, groupName));
            }
            return groups;
        }
    }

    @Override
    public int addNewGroup(Group group) throws SQLException {
        if (!group.getGroupName().matches("^[A-Z]{2}\\s-\\s\\d{2}$")) {
            throw new RuntimeException("Invalid group name");
        }
        String query =
            "INSERT INTO groups(group_name) VALUES (?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, group.getGroupName());
            int changes = preparedStatement.executeUpdate();
            return changes;
        }
    }

    @Override
    public List<Course> getAllCourses() throws SQLException {
        String query = "SELECT * FROM courses";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<Course> courses = new ArrayList<>();
            while (resultSet.next()) {
                int courseId = resultSet.getInt("course_id");
                String courseName = resultSet.getString("course_name");
                String courseDescription = resultSet.getString("course_description");
                courses.add(new Course(courseId, courseName, courseDescription));
            }
            return courses;
        }
    }

    @Override
    public int addNewCourse(Course course) throws SQLException {
        String query =
            "INSERT INTO courses(course_name, course_description) " +
                "VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, course.getCourseName());
            preparedStatement.setString(2, course.getCourseDescription());
            int changes = preparedStatement.executeUpdate();
            return changes;
        }
    }
}
