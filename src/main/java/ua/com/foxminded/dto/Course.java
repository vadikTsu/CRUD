package ua.com.foxminded.dto;

public class Course {

    private int courseId;
    private String courseName;
    private String courseDescription;

    public Course(int courseId, String courseName, String courseDescription) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    @Override
    public String toString() {
        return "Course{" +
            "courseId=" + courseId +
            ", courseName='" + courseName + '\'' +
            ", course_description='" + courseDescription + '\'' +
            '}';
    }
}
