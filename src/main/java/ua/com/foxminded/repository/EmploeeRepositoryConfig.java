package ua.com.foxminded.repository;


import org.flywaydb.core.Flyway;
import org.postgresql.jdbc3.Jdbc3SimpleDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class EmploeeRepositoryConfig {

    public static DataSource getPostgresDataSource() throws IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("C:\\Users\\Vadym Tsudenko\\Dropbox\\PC\\Desktop\\code\\code\\CRUD\\src\\main\\resources\\db.properties")) {
            props.load(fis);
            Jdbc3SimpleDataSource dataSource = new Jdbc3SimpleDataSource();
            dataSource.setURL(props.getProperty("H2_DB_URL"));  // Use the H2 database URL property
            dataSource.setUser(props.getProperty("H2_DB_USERNAME"));  // Use the H2 database username property
            dataSource.setPassword(props.getProperty("H2_DB_PASSWORD"));  // Use the H2 database password property
            System.out.println("Connected to the H2 database");
            Flyway flyway = Flyway.configure().dataSource(dataSource).validateOnMigrate(false).load();
            flyway.migrate();
            return dataSource;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}