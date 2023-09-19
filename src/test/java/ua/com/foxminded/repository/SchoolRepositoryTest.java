package ua.com.foxminded.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.dto.Group;
import ua.com.foxminded.dto.Student;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SchoolRepositoryTest {

    @Mock
    private DataSource dataSource;

    @InjectMocks
    private SchoolRepository schoolRepository;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    void setUp() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    }

    @Test
    void getAllGroupsWithLessOrEqualStudents() throws SQLException {
        Group group = new Group(1, "Group 1");

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("group_id")).thenReturn(group.getGroupId());
        when(resultSet.getString("group_name")).thenReturn(group.getGroupName());
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        List<Group> groups = schoolRepository.getAllGroupsWithLessOrEqualStudents(12);  //any int passed as argument

        assertEquals(1, groups.size());
        assertEquals(group, groups.get(0));
    }

    @Test
    void getAllStudentsRelatedToTheCourse() throws SQLException {
        Student student = new Student(1, 1, "Pepe", "Frog");

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("student_id")).thenReturn(student.getStudentId());
        when(resultSet.getInt("group_id")).thenReturn(student.getGroupId());
        when(resultSet.getString("first_name")).thenReturn(student.getFirstName());
        when(resultSet.getString("last_name")).thenReturn(student.getLastName());
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        List<Student> students = schoolRepository.getAllStudentsRelatedToTheCourse("some_course");  //any int passed as argument

        assertEquals(1, students.size());
        assertEquals(student, students.get(0));
    }

    @Test
    void addNewStudent_shouldCorrectlySetValuesForPreparedStatement_whenStudentArgument() throws SQLException {
        when(preparedStatement.executeUpdate()).thenReturn(1);

        int changes = schoolRepository.addNewStudent(new Student(1, 1, "Pepe", "Frog"));
        verify(preparedStatement, times(1)).setInt(anyInt(), anyInt());
        verify(preparedStatement, times(2)).setString(anyInt(), anyString());
        verify(preparedStatement).executeUpdate();
        assertEquals(1, changes);
    }

    @Test
    void addNewStudent_shouldThrowNullPointerException_whenNullArgument() throws SQLException {
        verifyNoInteractions(preparedStatement);
        assertThrows(NullPointerException.class, () -> schoolRepository.addNewStudent((Student) null));
    }

    @Test
    void addNewStudent_shouldProcessBatching_whenListOfStudentsArgument() throws SQLException {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, 3, "Vasya", "Pupkin"));
        students.add(new Student(2, 2, "Dyadya", "Petiea"));

        schoolRepository.addNewStudent(students);

        verify(preparedStatement, times(2)).addBatch();
        verify(preparedStatement, times(2)).setInt(anyInt(), anyInt());
        verify(preparedStatement, times(4)).setString(anyInt(), anyString());
        verify(preparedStatement, times(1)).executeBatch();
    }

    @Test
    void deleteStudent_shouldCorrectlySetValuesForPreparedStatement_whenSrudentIdArgument() throws SQLException {
        when(preparedStatement.executeUpdate()).thenReturn(1);

        int changes = schoolRepository.deleteStudent(123);
        verify(preparedStatement, times(2)).setInt(anyInt(), anyInt());
        verify(preparedStatement).executeUpdate();
        assertEquals(1, changes);
    }

    @Test
    void reomoveStudentFromCourse_shouldCorrectlySetValuesForPreparedStatement_whenSrudentIdAndCoursIdArgument()
        throws SQLException {
        when(preparedStatement.executeUpdate()).thenReturn(1);

        int changes = schoolRepository.reomoveStudentFromCourse(123, 123);
        verify(preparedStatement, times(2)).setInt(anyInt(), anyInt());
        verify(preparedStatement).executeUpdate();
        assertEquals(1, changes);
    }
}
