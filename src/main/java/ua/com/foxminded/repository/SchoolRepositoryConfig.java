package ua.com.foxminded.repository;


import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.postgresql.jdbc3.Jdbc3SimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;


public class SchoolRepositoryConfig {

    private static Logger logger = LoggerFactory.getLogger(SchoolRepositoryConfig.class);

    public static DataSource getPostgresDataSource(String url, String username , String password) throws IOException {
        Jdbc3SimpleDataSource dataSource = new Jdbc3SimpleDataSource();
        dataSource.setURL(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        try  {
            Flyway flyway = Flyway.configure().dataSource(dataSource).cleanDisabled(false).load();
            flyway.clean();
            flyway.migrate();
        } catch (FlywayException exception) {
            logger.error(exception.getMessage());
        }
        return dataSource;
    }
}
