package ua.com.foxminded.repository;


import org.flywaydb.core.Flyway;
import org.postgresql.jdbc3.Jdbc3SimpleDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class EmploeeRepositoryConfig {

    public static DataSource getPostgresDataSource() throws IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("src\\main\\resources\\db.properties")) {
            props.load(fis);
            Jdbc3SimpleDataSource dataSource = new Jdbc3SimpleDataSource();
            dataSource.setURL(props.getProperty("H2_DB_URL"));
            dataSource.setUser(props.getProperty("H2_DB_USERNAME"));
            dataSource.setPassword(props.getProperty("H2_DB_PASSWORD"));
            System.out.println("Connected to the H2 database");
            Flyway flyway = Flyway.configure().dataSource(dataSource).load();
            flyway.migrate();
            return dataSource;
        }
    }
}