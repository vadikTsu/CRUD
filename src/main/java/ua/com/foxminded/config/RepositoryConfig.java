package ua.com.foxminded.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.postgresql.jdbc3.Jdbc3SimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.Properties;

public class RepositoryConfig {

    private static Logger logger = LoggerFactory.getLogger(RepositoryConfig.class);

    /**
     * Creates data source for database from given properties, executes migration script placed in db/migration.
     *
     * @param properties url, username and password to
     * @return DataSource implementation by PostgreSQL(vendor)
     */
    public static DataSource getPostgresDataSource(Properties properties) {
        Jdbc3SimpleDataSource dataSource = new Jdbc3SimpleDataSource();
        dataSource.setURL(properties.getProperty("DB_URL"));
        dataSource.setUser(properties.getProperty("DB_USERNAME"));
        dataSource.setPassword(properties.getProperty("DB_PASSWORD"));
        try {
            Flyway flyway = Flyway.configure().dataSource(dataSource).cleanDisabled(false).load();
            flyway.clean();
            flyway.migrate();
        } catch (FlywayException exception) {
            logger.error(exception.getMessage());
        }
        return dataSource;
    }
}
