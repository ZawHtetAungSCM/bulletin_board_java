package com.mtm.bulletin_board.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.mtm.bulletin_board.constants.DbConstants;

public class DbConnection {

    private static final String JDBC_URL = DbConstants.getFullJdbcUrl();
    private static final String USERNAME = DbConstants.USERNAME;
    private static final String PASSWORD = DbConstants.PASSWORD;

    static {
        try {
            // Load the JDBC driver
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading PostgreSQL JDBC driver");
        }
    }

    public static Connection getConnection() {
        try {
            // Create and return a database connection
            return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error connecting to the database");
        }
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                // Close the database connection
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Error closing the database connection");
            }
        }
    }

    public static void closeStatement(PreparedStatement statement) {
        if (statement != null) {
            try {
                // Close the prepared statement
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Error closing the prepared statement");
            }
        }
    }

    public static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                // Close the result set
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Error closing the result set");
            }
        }
    }

    public static void closeResources(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        closeResultSet(resultSet);
        closeStatement(preparedStatement);
        closeConnection(connection);
    }
}
