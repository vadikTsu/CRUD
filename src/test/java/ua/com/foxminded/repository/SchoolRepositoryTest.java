package ua.com.foxminded.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;

//todo implement testing
@ExtendWith(MockitoExtension.class)
class SchoolRepositoryTest {

    @Mock
    DataSource dataSource;

    @InjectMocks
    SchoolRepository schoolRepository;

    @Test
    void getAllGroupsWithLessOrEqualStudents() {
    }

    @Test
    void findAllStudentsRelatedToTheCourse() {
    }

    @Test
    void addNewStudent() {
    }

    @Test
    void addNewStudents() {
    }

    @Test
    void deleteStudent() {
    }

    @Test
    void reomoveStudentFromCourse() {
    }
}