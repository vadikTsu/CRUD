package ua.com.foxminded.repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmploeeRepository {

    private final DataSource dataSource;

    public EmploeeRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void getStudentsData() throws SQLException {

        try (Connection connection = dataSource.getConnection()) {
            String query = "SELECT * FROM students";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    int studentId = resultSet.getInt("student_id");
                    int groupId = resultSet.getInt("group_id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");

                    System.out.println("Student ID: " + studentId);
                    System.out.println("Group ID: " + groupId);
                    System.out.println("First Name: " + firstName);
                    System.out.println("Last Name: " + lastName);
                    System.out.println("------------------------");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
