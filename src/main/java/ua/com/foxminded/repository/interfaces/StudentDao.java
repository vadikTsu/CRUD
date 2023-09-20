package ua.com.foxminded.repository.interfaces;

import ua.com.foxminded.dto.Student;

import java.sql.SQLException;
import java.util.List;

public interface StudentDao {

    List<Student> getAllStudents() throws SQLException;

    int addNewStudent(Student student) throws SQLException;

    int[] addNewStudent(List<Student> students) throws SQLException;

    int deleteStudent(int studentId) throws SQLException;
}
