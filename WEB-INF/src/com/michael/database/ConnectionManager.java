package com.michael.database;

import java.sql.*;

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
}
