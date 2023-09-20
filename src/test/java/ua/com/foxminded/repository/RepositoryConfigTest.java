package ua.com.foxminded.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import ua.com.foxminded.config.RepositoryConfig;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryConfigTest {

    private static PostgreSQLContainer<?> postgresContainer;
    private DataSource dataSource;
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
    }

    @Test
    public void getPostgresDataSource_shouldEstablishConnectionToDB_whenPostgresDataSource() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            assertTrue(connection.isValid(4));
            String testQuery = "SELECT 1";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(testQuery)) {
                assertTrue(resultSet.next());
                assertEquals(1, resultSet.getInt(1));
                System.out.println(properties.getProperty("DB_URL"));
            } catch (SQLException e) {
                fail("Failed to execute test query: " + e.getMessage());
            }
        } catch (SQLException e) {
            fail("Failed to establish a database connection: " + e.getMessage());
        }
    }

    @Test
    public void getPostgresDataSource_shouldMigrateTablesSchema_whenPostgresDataSourceExecutesMigrationScript() {
        var tableNames = Arrays.asList("groups", "students", "courses", "students_courses");
        var actualTableNames = new ArrayList<String>();

        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            try (ResultSet resultSet = databaseMetaData.getTables(null, "public", "%", null);) {
                while (resultSet.next()) {
                    actualTableNames.add(resultSet.getString("TABLE_NAME"));
                }
            } catch (SQLException e) {
                fail("Failed to execute test query: " + e.getMessage());
            }
        } catch (SQLException e) {
            fail("Failed to establish a database connection: " + e.getMessage());
        }

        for (String expectedTableName : tableNames) {
            assertTrue(actualTableNames.contains(expectedTableName), "Expected table '" + expectedTableName + "' does not exist.");
        }
    }

    @Test
    public void getPostgresDataSource_shouldMigrateStoredFunctions_whenPostgresDataSourceExecutesMigrationScript() {
        var expectedStoredFunctionsName = Arrays.asList("get_groups_with_less_students", "get_students_related_to_course");
        var actualStoredFunctionsName = new ArrayList<String>();

        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            try (ResultSet resultSet = databaseMetaData.getFunctions(null, null, null)) {
                while (resultSet.next()) {
                    actualStoredFunctionsName.add(resultSet.getString("FUNCTION_NAME"));
                }
            } catch (SQLException e) {
                fail("Failed to execute test query: " + e.getMessage());
            }
        } catch (SQLException e) {
            fail("Failed to establish a database connection: " + e.getMessage());
        }
        for (String expectedFunctionName : expectedStoredFunctionsName) {
            assertTrue(actualStoredFunctionsName.contains(expectedFunctionName), "Expected table '" + expectedFunctionName + "' does not exist.");
        }
    }

    @AfterAll
    public static void stopContainer() {
        postgresContainer.stop();
    }
}
