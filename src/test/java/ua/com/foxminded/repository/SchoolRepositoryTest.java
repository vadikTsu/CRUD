package ua.com.foxminded.repository;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import ua.com.foxminded.config.RepositoryConfig;
import ua.com.foxminded.dto.Group;
import ua.com.foxminded.dto.Student;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class SchoolRepositoryTest {

    private static PostgreSQLContainer<?> postgresContainer;
    private SchoolRepository schoolRepository;
    DataSource dataSource;
    private Properties properties;

    @BeforeAll
    private static void startContainer() {
        postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpassword");
        postgresContainer.start();
    }

    @BeforeEach
    private void setDataSource() {
        properties = new Properties();
        properties.setProperty("DB_URL", postgresContainer.getJdbcUrl());
        properties.setProperty("DB_USERNAME", postgresContainer.getUsername());
        properties.setProperty("DB_PASSWORD", postgresContainer.getPassword());
        dataSource = RepositoryConfig.getPostgresDataSource(properties);
        schoolRepository = new SchoolRepository(dataSource);
    }

    @AfterAll
    public static void stopContainer() {
        postgresContainer.stop();
    }

    @Test
    public void getAllGroupsWithLessOrEqualStudents_shouldFetchAllGroups_whenMaximalInteger() {
        try {
            List<Group> groups = schoolRepository.getAllGroupsWithLessOrEqualStudents(Integer.MAX_VALUE);
            Assertions.assertNotNull(groups);
            groups.forEach(group ->
                Assertions.assertTrue(group.getGroupName().matches("^[A-Z]{2}\\s-\\s\\d{2}$")));
        } catch (SQLException e) {
            //todo
        }
    }

    @Test
    void getAllStudentsRelatedToTheCourse() throws SQLException {
        List<Student> students = schoolRepository.getAllStudentsRelatedToTheCourse("Biology");
        Assertions.assertFalse(students.isEmpty());

        students = schoolRepository.getAllStudentsRelatedToTheCourse("History of USSSR");
        Assertions.assertTrue(students.isEmpty());
    }

    @Test
    void addNewStudent_shouldCorrectlySetValuesForPreparedStatement_whenStudentArgument() throws SQLException {
        int changes = schoolRepository.addNewStudent(new Student(1, 1, "Pepe", "Frog"));
        Assertions.assertEquals(1, changes);
    }

    @Test
    void addNewStudent_shouldThrowRuntimeException_whenInvalidStudentArgument() throws SQLException {
        Exception e = Assertions.assertThrows(RuntimeException.class, () -> schoolRepository
            .addNewStudent(new Student(1, 1, null, "Frog")));
        Assertions.assertEquals("invalid student`s data", e.getMessage());
    }

    @Test
    void addNewStudent_shouldProcessBatching_whenListOfStudentsArgument() throws SQLException {
        List<Student> students = List.of(new Student(2, 2, "Dyadya", "Petiea"),
            new Student(1, 3, "Vasya", "Pupkin"));
        schoolRepository.addNewStudent(students);
    }

    @Test
    void testDeleteStudent() {
        int studentIdToDelete = 1;
        try {
            int changes = schoolRepository.deleteStudent(studentIdToDelete);

            assertEquals(2, changes);
        } catch (SQLException e) {
            fail("Exception should not be thrown for an existing student");
        }
    }

    @Test
    void testDeleteNonexistentStudent() {
        int nonexistentStudentId = 1000;
        try {
            int changes = schoolRepository.deleteStudent(nonexistentStudentId);
            assertEquals(0, changes);
        } catch (SQLException e) {
            fail("Exception should not be thrown for a nonexistent student");
        }
    }

    @Test
    void testRemoveStudentFromCourse() {
        int studentId = 1;
        int courseId = 1;
        try {
            int changes = schoolRepository.reomoveStudentFromCourse(studentId, courseId);
            assertEquals(1, changes);
        } catch (SQLException e) {
            fail("Exception should not be thrown for a valid student and course");
        }
    }

    @Test
    void testRemoveStudentFromNonexistentCourse() {
        int studentId = 1;
        int nonexistentCourseId = 1000;
        try {
            int changes = schoolRepository.reomoveStudentFromCourse(studentId, nonexistentCourseId);
            assertEquals(0, changes);
        } catch (SQLException e) {
            fail("Exception should not be thrown for a nonexistent course");
        }
    }
}
