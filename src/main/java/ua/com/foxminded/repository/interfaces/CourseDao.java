package ua.com.foxminded.repository.interfaces;

import ua.com.foxminded.dto.Course;

import java.sql.SQLException;
import java.util.List;

public interface CourseDao {

    List<Course> getAllCourses() throws SQLException;

    int addNewCourse(Course course) throws SQLException;
}
