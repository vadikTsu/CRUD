package ua.com.foxminded.repository;


import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class EmploeeRepositoryConfig {

    public static DataSource getH2DataSource() throws IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("C:\\Users\\Vadym Tsudenko\\Dropbox\\PC\\Desktop\\code\\code\\CRUD\\src\\main\\resources\\db.properties")) {
            props.load(fis);
            JdbcDataSource dataSource = new JdbcDataSource();
            dataSource.setURL(props.getProperty("H2_DB_URL"));  // Use the H2 database URL property
            dataSource.setUser(props.getProperty("H2_DB_USERNAME"));  // Use the H2 database username property
            dataSource.setPassword(props.getProperty("H2_DB_PASSWORD"));  // Use the H2 database password property
            System.out.println("Connected to the H2 database");

            Flyway flyway = Flyway.configure().dataSource(dataSource).load();
            flyway.clean();

            flyway.migrate();
            return dataSource;
        }
    }
}