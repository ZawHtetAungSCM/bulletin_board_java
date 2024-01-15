package com.mtm.bulletin_board;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import com.mtm.bulletin_board.constants.DbConstants;

@WebListener
public class DatabaseInitializer implements ServletContextListener {

	private static final Logger logger = Logger.getLogger(DatabaseInitializer.class.getName());

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		try {
			// Get the database connection details from your configuration
			String dbUrl = DbConstants.JDBC_URL;
			String dbName = DbConstants.DATABASE_NAME;
			String dbUser = DbConstants.USERNAME;
			String dbPassword = DbConstants.PASSWORD;
			String fullJdbcUrl = DbConstants.getFullJdbcUrl();
			String migrationFolderPath = sce.getServletContext().getRealPath("/WEB-INF/classes/db/migration");

			if (!databaseExists(fullJdbcUrl, dbUser, dbPassword)) {
				// If not, create the database
				logger.info("Creating database...");
				// Create Database
				createDatabase(dbUrl, dbName, dbUser, dbPassword);
				// Migrate Database
				migrateDatabase(fullJdbcUrl, dbUser, dbPassword, migrationFolderPath);
			} else {
				logger.info("Database already exists. No need to migrate.");
			}
		} catch (Exception e) {
			logger.info("DatabaseInitializer Error : " + e);
		}

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	private boolean databaseExists(String url, String user, String password) throws SQLException {
		try (Connection connection = DriverManager.getConnection(url, user, password)) {
			return true;
		} catch (SQLException e) {
			// Check if the exception is related to database not found
			return !e.getSQLState().equals("3D000");
		}
	}

	private void createDatabase(String url, String dbName, String user, String password) throws SQLException {
		try (Connection connection = DriverManager.getConnection(url, user, password);
				java.sql.Statement statement = connection.createStatement()) {
			statement.executeUpdate("CREATE DATABASE " + dbName);
			logger.info("Database created: " + dbName);
		}
	}

	private void migrateDatabase(String jdbcUrl, String username, String password, String migrationFolderPath)
			throws SQLException {
		try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
				Statement statement = connection.createStatement();) {

			File folder = new File(migrationFolderPath);

			File[] files = folder.listFiles();

			if (files != null) {
				for (File file : files) {
					if (file.isFile() && file.getName().endsWith(".sql")) {
						executeScript(statement, file);
					}
				}
			}

			logger.info("All migration scripts executed successfully.");
		} catch (SQLException | java.io.IOException e) {
			e.printStackTrace();
		}
	}

	private static void executeScript(Statement statement, File scriptFile) throws SQLException, IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(scriptFile))) {
			StringBuilder scriptContent = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				scriptContent.append(line).append("\n");
			}

			// Execute the SQL script
			statement.execute(scriptContent.toString());
			logger.info("Executed SQL script: " + scriptFile.getName());
		}
	}

}
