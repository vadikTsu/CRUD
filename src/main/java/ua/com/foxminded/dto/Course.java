package ua.com.foxminded.dto;

public class Course {

    private int courseId;
    private String courseName;
    private String course_description;

    public Course(int courseId, String courseName, String course_description) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.course_description = course_description;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourse_description() {
        return course_description;
    }

    @Override
    public String toString() {
        return "Course{" +
            "courseId=" + courseId +
            ", courseName='" + courseName + '\'' +
            ", course_description='" + course_description + '\'' +
            '}';
    }
}
