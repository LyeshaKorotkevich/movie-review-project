package eu.innowise.moviereviewproject.utils;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public final class DataSourceUtil {

    private DataSourceUtil () {}

    public static DataSource getDataSource() throws NamingException {
        InitialContext context = new InitialContext();
        return (DataSource) context.lookup("java:/comp/env/jdbc/MyLocalDB");
    }
}