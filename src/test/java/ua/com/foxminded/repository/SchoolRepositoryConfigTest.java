package ua.com.foxminded.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SchoolRepositoryConfigTest {

    public static PostgreSQLContainer<?> postgresContainer;

    @BeforeAll
    public static void startContainer() {
        postgresContainer = new PostgreSQLContainer<>()
                .withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpassword");
        postgresContainer.start();
    }

    @Test
    public void testGetPostgresDataSource_shouldEstablisheConnectionToDB_whenPostgresDataSource () throws SQLException, IOException {
        DataSource dataSource = SchoolRepositoryConfig.getPostgresDataSource(postgresContainer.getJdbcUrl(), postgresContainer.getUsername(), postgresContainer.getPassword());
        assertNotNull(dataSource);
        postgresContainer.stop();
    }

//    @Test
//    public void testGetPostgresDataSource_shouldMigrateTablesSchema_whenPostgresDataSource() {
//        //todo implement validation on migration
//    }

    @AfterAll
    public static void stopContainer() {
        postgresContainer.stop();
    }
}
