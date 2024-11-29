package eu.innowise.moviereviewproject.listener;

import eu.innowise.moviereviewproject.utils.FlywayMigrationUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class FlywayMigrationListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        FlywayMigrationUtil.runMigrations();
    }
}