package com.mtm.bulletin_board.constants;

public class DbConstants {
    public static final String JDBC_URL = "jdbc:postgresql://localhost:5432/";
    public static final String USERNAME = "postgres";
    public static final String PASSWORD = "password";
    public static final String DATABASE_NAME = "bulletin_board";

    public static String getFullJdbcUrl() {
        return JDBC_URL + DATABASE_NAME;
    }
}
