package ua.com.foxminded.repository;


import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.postgresql.jdbc3.Jdbc3SimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class SchoolRepositoryConfig {

    private static Logger logger = LoggerFactory.getLogger(SchoolRepositoryConfig.class);

    //todo add logging
    public static DataSource getPostgresDataSource() throws IOException {
        Properties properties = new Properties();
        Jdbc3SimpleDataSource dataSource = new Jdbc3SimpleDataSource();
        try (FileInputStream inputStream = new FileInputStream("src\\main\\resources\\db.properties")) {
            properties.load(inputStream);
            dataSource.setURL(properties.getProperty("POSTGRES_DB_URL"));
            dataSource.setUser(properties.getProperty("POSTGRES_DB_USERNAME"));
            dataSource.setPassword(properties.getProperty("POSTGRES_DB_PASSWORD"));
            try {
                Flyway flyway = Flyway.configure().dataSource(dataSource).load();
                flyway.migrate();
            } catch (FlywayException exception) {
                //todo handle exception
                throw new RuntimeException(exception);
            }
            return dataSource;
        }
    }
}
