package eu.innowise.moviereviewproject.listener;

import eu.innowise.moviereviewproject.utils.db.JpaUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class JpaContextListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JpaUtil.closeEntityManagerFactory();
    }
}
