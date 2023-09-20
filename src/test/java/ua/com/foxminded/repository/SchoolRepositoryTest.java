package ua.com.foxminded.repository;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import ua.com.foxminded.config.RepositoryConfig;
import ua.com.foxminded.dto.Group;
import ua.com.foxminded.dto.Student;
import ua.com.foxminded.repository.impl.SchoolRepository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Comparator;
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
    public void getAllGroupsWithLessOrEqualStudents_shouldFetchAllGroups_whenMaximalInteger() throws SQLException {
        List<Group> groups = schoolRepository.getAllGroupsWithLessOrEqualStudents(Integer.MAX_VALUE);
        List<Group> allGroups = schoolRepository.getAllGroups();

        allGroups.sort(Comparator.comparingInt(Group::getGroupId));
        groups.sort(Comparator.comparingInt(Group::getGroupId));

        assertEquals(allGroups, groups);

        groups.forEach(group ->
            assertTrue(group.getGroupName().matches("^[A-Z]{2}\\s-\\s\\d{2}$")));
    }

    @Test
    void getAllStudentsRelatedToTheCourse_shouldReturnExistingStudents_whenValidCourseName() throws SQLException {
        List<Student> students = schoolRepository.getAllStudentsRelatedToTheCourse("Biology");
        assertFalse(students.isEmpty());

        List<Student> allStudents = schoolRepository.getAllStudents();
        assertTrue(allStudents.containsAll(students));

        students = schoolRepository.getAllStudentsRelatedToTheCourse("NonExistingCourse");
        Assertions.assertTrue(students.isEmpty());
    }

    @Test
    void addNewStudent_shouldCorrectlySetValuesForPreparedStatement_whenStudentArgument() throws SQLException {
        int changes = schoolRepository.addNewStudent(new Student(1, 1, "Pepe", "Frog"));
        assertEquals(1, changes);

        List<Student> allStudents = schoolRepository.getAllStudents();
        assertTrue(allStudents.contains(new Student(1, 1, "Pepe", "Frog")));
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

        int[] changes = schoolRepository.addNewStudent(students);
        assertArrayEquals(changes, new int[]{1, 1});

        List<Student> allStudents = schoolRepository.getAllStudents();
        assertTrue(allStudents.containsAll(students));
    }

    @Test
    void deleteStudent_shouldReturnZero_whenNonexistentStudentId() throws SQLException {
        int changes = schoolRepository.deleteStudent(Integer.MAX_VALUE);
        assertEquals(0, changes);
    }

    @Test
    void removeStudentFromCourse_shouldReturnZero_whenNonexistentCourse() throws SQLException {
        int changes = schoolRepository.removeStudentFromCourse(1, Integer.MAX_VALUE);
        assertEquals(0, changes);
    }
}
