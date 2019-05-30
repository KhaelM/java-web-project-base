package com.michael.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionManager{
    public static Connection getConnection(String url, String driverName, String username, String password) throws Exception {
        Connection con = null;
        try {
            Class.forName(driverName); 
            con=DriverManager.getConnection(url, username, password);

        } catch(Exception exception) {
            exception.printStackTrace();
        }
        return con;
    }

    public static Connection getOracleConnection(String username, String password) throws Exception {
        return getConnection("jdbc:oracle:thin:@localhost:1521:xe", "oracle.jdbc.driver.OracleDriver", username, password);
    }

    public static Connection getPsqlConnection(String username, String password) throws Exception {
        return getConnection("jdbc:postgresql://127.0.0.1:5432/michael", "org.postgresql.Driver", username, password);
    }
}
