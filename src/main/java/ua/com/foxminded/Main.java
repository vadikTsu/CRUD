package ua.com.foxminded;

import ua.com.foxminded.config.RepositoryConfig;
import ua.com.foxminded.console.SchoolManager;
import ua.com.foxminded.repository.SchoolRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws IOException, SQLException {
        Properties properties = new Properties();
        try (InputStream fileInputStream = new FileInputStream("src/main/resources/db.properties")) {
            properties.load(fileInputStream);
        }
        new SchoolManager(new SchoolRepository(RepositoryConfig.getPostgresDataSource(properties))).getConsole();
    }
}
