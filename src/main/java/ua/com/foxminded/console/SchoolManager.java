package ua.com.foxminded.console;

import ua.com.foxminded.dto.Group;
import ua.com.foxminded.dto.Student;
import ua.com.foxminded.repository.SchoolRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class SchoolManager {

    private final SchoolRepository schoolRepository;

    public SchoolManager(SchoolRepository emploeeRepository) {
        this.schoolRepository = emploeeRepository;
    }

    public void getConsole() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("a. Find all groups with less or equal students’ number");
            System.out.println("b. Find all students related to the course with the given name");
            System.out.println("c. Add a new student");
            System.out.println("d. Delete a student by the STUDENT_ID");
            System.out.println("e. Add a student to the course (from a list)");
            System.out.println("f. Remove the student from one of their courses");
            System.out.println("q. Quit");

            String choice = scanner.nextLine();

            switch (choice.toLowerCase()) {
                case "a":
                    getAllGroupsWithLessOrEqualStudentsDialog(scanner);
                    break;
                case "b":
                    findAllStudentsRelatedToTheCourseDialog(scanner);
                    break;
                case "c":
                    addNewStudentDialog(scanner);
                    break;
                case "d":
                    deleteStudentDialog(scanner);
                    break;
                case "e":
                    //todo add a list of students (from excel or ?)
                    break;
                case "f":
                    reomoveStudentFromCourseDialog(scanner);
                    break;
                case "q":
                    System.out.println("Exiting the application.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }


    private void reomoveStudentFromCourseDialog(Scanner scanner) throws SQLException {
        System.out.print("Enter the student ID: ");
        int studentId = scanner.nextInt();
        System.out.print("Enter the course name: ");
        int courseId = scanner.nextInt();
        scanner.nextInt();
        schoolRepository.reomoveStudentFromCourse(studentId, courseId);
    }

    private void deleteStudentDialog(Scanner scanner) throws SQLException {
        System.out.print("Enter the student ID to delete: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();
        schoolRepository.deleteStudent(studentId);
        System.out.println("Student deleted successfully.");
    }

    private void addNewStudentDialog(Scanner scanner) throws SQLException {
        System.out.print("Enter the group ID: ");
        int groupId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter the student's first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter the student's last name: ");
        String lastName = scanner.nextLine();
        Student newStudent = new Student(0, groupId, firstName, lastName);
        schoolRepository.addNewStudent(newStudent);
        System.out.println("Student added successfully.");
    }

    private void findAllStudentsRelatedToTheCourseDialog(Scanner scanner) throws SQLException {
        System.out.print("Enter the course name: ");
        String courseName = scanner.nextLine();
        List<Student> studentsInCourse = schoolRepository.getAllStudentsRelatedToTheCourse(courseName);
        studentsInCourse.forEach(System.out::println);
    }

    private void getAllGroupsWithLessOrEqualStudentsDialog(Scanner scanner) throws SQLException {
        System.out.print("Enter the maximum number of students: ");
        int maxStudents = scanner.nextInt();
        scanner.nextLine();
        List<Group> groupsWithLessOrEqualStudents = schoolRepository.getAllGroupsWithLessOrEqualStudents(maxStudents);
        groupsWithLessOrEqualStudents.forEach(System.out::println);
    }
}
