package com.revshop.util;

import com.revshop.config.DatabaseConfig;
import com.revshop.config.LoggerConfig;
import org.apache.logging.log4j.Logger;
import java.sql.*;

/**
 * Database connection utility
 * Manages database connections and resources
 */
public class DatabaseUtil {
    private static Connection connection = null;
    private static final Logger logger = LoggerConfig.getLogger(DatabaseUtil.class);

    /**
     * Get a database connection
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Load database configuration
                if (!DatabaseConfig.isValidConfig()) {
                    logger.error("Invalid database configuration");
                    throw new SQLException("Invalid database configuration");
                }

                // Load MySQL driver
                Class.forName(DatabaseConfig.getDriver());

                // Create connection
                connection = DriverManager.getConnection(
                        DatabaseConfig.getUrl(),
                        DatabaseConfig.getUsername(),
                        DatabaseConfig.getPassword()
                );

                // Test connection
                if (connection != null && !connection.isClosed()) {
                    //logger.info("Database connection established successfully");
                    LoggerConfig.logDatabaseStatus("SUCCESS",
                            "Connected to: " + DatabaseConfig.getUrl());
                }

            } catch (ClassNotFoundException e) {
                logger.error("MySQL JDBC Driver not found: " + e.getMessage());
                throw new SQLException("Database driver not found", e);
            } catch (SQLException e) {
                logger.error("Database connection failed: " + e.getMessage());
                LoggerConfig.logDatabaseStatus("ERROR",
                        "Connection failed: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }

    /**
     * Close database connection
     */
    public static void closeConnection(Connection conn) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
                logger.info("Database connection closed");
                LoggerConfig.logDatabaseStatus("SUCCESS", "Connection closed");
            }
        } catch (SQLException e) {
            logger.error("Error closing database connection: " + e.getMessage());
        }
    }

    /**
     * Test database connection
     */
    public static boolean testConnection() {
        try (Connection testConn = DriverManager.getConnection(
                DatabaseConfig.getUrl(),
                DatabaseConfig.getUsername(),
                DatabaseConfig.getPassword())) {

            if (testConn != null && !testConn.isClosed()) {
                logger.info("Database connection test: SUCCESS");
                return true;
            }
        } catch (SQLException e) {
            logger.error("Database connection test: FAILED - " + e.getMessage());
        }
        return false;
    }

    /**
     * Execute a simple query to verify database is working
     */
    public static boolean verifyDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT 1")) {

            if (rs.next()) {
                logger.debug("Database verification: SUCCESS");
                return true;
            }
        } catch (SQLException e) {
            logger.error("Database verification failed: " + e.getMessage());
        }
        return false;
    }

    /**
     * Close resources safely
     */
    public static void closeResources(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception e) {
                    logger.warn("Error closing resource: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Close result set
     */
    public static void closeResultSet(ResultSet rs) {
        closeResources(rs);
    }

    /**
     * Close statement
     */
    public static void closeStatement(Statement stmt) {
        closeResources(stmt);
    }

    /**
     * Close prepared statement
     */
    public static void closePreparedStatement(PreparedStatement pstmt) {
        closeResources(pstmt);
    }

    /**
     * Get database metadata
     */
    public static void printDatabaseInfo() {
        try (Connection conn = getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();

            logger.info("\n=== Database Information ===");
            logger.info("Database Product: " + metaData.getDatabaseProductName());
            logger.info("Database Version: " + metaData.getDatabaseProductVersion());
            logger.info("Driver Name: " + metaData.getDriverName());
            logger.info("Driver Version: " + metaData.getDriverVersion());
            logger.info("URL: " + metaData.getURL());
            logger.info("User: " + metaData.getUserName());

        } catch (SQLException e) {
            logger.error("Error getting database info: " + e.getMessage());
        }
    }
}