package eu.innowise.moviereviewproject.utils;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceUtil {

    public static DataSource getDataSource() throws NamingException {
        InitialContext context = new InitialContext();
        return (DataSource) context.lookup("java:/comp/env/jdbc/MyLocalDB");
    }

    public static Connection getConnection() throws NamingException, SQLException {
        DataSource dataSource = getDataSource();
        return dataSource.getConnection();
    }
}