package eu.innowise.moviereviewproject.utils.db;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;

import javax.naming.NamingException;

@Slf4j
public class FlywayMigrationUtil {

    private FlywayMigrationUtil() {
    }

    public static void runMigrations() {
        log.info("Starting Flyway migrations...");
        try {
            Flyway flyway = Flyway.configure()
                    .dataSource(DataSourceUtil.getDataSource())
                    .load();

            flyway.migrate();
            log.info("Flyway migrations executed successfully.");
        } catch (NamingException e) {
            log.error("Error during Flyway migrations.", e);
            throw new RuntimeException(e);
        }
    }
}
