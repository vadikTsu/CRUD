package ua.com.foxminded.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import ua.com.foxminded.dto.Group;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

//todo validate on migration
class SchoolRepositoryConfigTest {

    private static PostgreSQLContainer<?> postgresContainer;

    @BeforeAll
    public static void startContainer() {
        postgresContainer = new PostgreSQLContainer<>()
                .withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpassword");
        postgresContainer.start();
    }

    @Test
    public void testGetPostgresDataSource() throws IOException, SQLException, IOException {
        System.setProperty("POSTGRES_DB_URL", postgresContainer.getJdbcUrl());
        System.setProperty("POSTGRES_DB_USERNAME", postgresContainer.getUsername());
        System.setProperty("POSTGRES_DB_PASSWORD", postgresContainer.getPassword());

        DataSource dataSource = SchoolRepositoryConfig.getPostgresDataSource();
        assertNotNull(dataSource);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT * FROM get_groups_with_less_students(?);")) {
            preparedStatement.setInt(1, 123);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Group> groups = new ArrayList<>();
                while (resultSet.next()) {
                    int groupId = resultSet.getInt("group_id");
                    String groupName = resultSet.getString("group_name");
                    groups.add(new Group(groupId, groupName));
                }
                groups.forEach(System.out::println);
            }
        }
    }

    @AfterAll
    public static void stopContainer() {
        postgresContainer.stop();
    }
}
