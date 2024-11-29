package eu.innowise.moviereviewproject.utils;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;

import javax.naming.NamingException;

@Slf4j
public class FlywayMigrationUtil {

    public static void runMigrations() {
        try {
            Flyway flyway = Flyway.configure()
                    .dataSource(DataSourceUtil.getDataSource())
                    .load();

            flyway.migrate();

            log.info("Flyway migrations executed successfully.");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
