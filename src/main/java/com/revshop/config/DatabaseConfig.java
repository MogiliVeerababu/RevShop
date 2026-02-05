package com.revshop.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Database configuration manager
 * Loads and provides database connection settings
 */
public class DatabaseConfig {
    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream input = DatabaseConfig.class.getClassLoader()
                .getResourceAsStream("database.properties")) {

            if (input == null) {
                System.err.println("Sorry, unable to find database.properties");
                // Set default values for testing
                setDefaultProperties();
                return;
            }

            properties.load(input);
            System.out.println("Database configuration loaded successfully");

        } catch (IOException e) {
            System.err.println("Error loading database.properties: " + e.getMessage());
            // Set default values
            setDefaultProperties();
        }
    }

    private static void setDefaultProperties() {
        properties.setProperty("db.url", "jdbc:mysql://localhost:3306/revshop");
        properties.setProperty("db.username", "root");
        properties.setProperty("db.password", "");
        properties.setProperty("db.driver", "com.mysql.cj.jdbc.Driver");
        properties.setProperty("db.pool.size", "10");
        properties.setProperty("db.max.connections", "50");
        properties.setProperty("db.connection.timeout", "30000");

        System.out.println("Using default database configuration");
    }

    public static String getUrl() {
        return properties.getProperty("db.url");
    }

    public static String getUsername() {
        return properties.getProperty("db.username");
    }

    public static String getPassword() {
        return properties.getProperty("db.password");
    }

    public static String getDriver() {
        return properties.getProperty("db.driver");
    }

    public static int getPoolSize() {
        return Integer.parseInt(properties.getProperty("db.pool.size", "10"));
    }

    public static int getMaxConnections() {
        return Integer.parseInt(properties.getProperty("db.max.connections", "50"));
    }

    public static int getConnectionTimeout() {
        return Integer.parseInt(properties.getProperty("db.connection.timeout", "30000"));
    }

    public static void printConfig() {
        System.out.println("\n=== Database Configuration ===");
        System.out.println("URL: " + getUrl());
        System.out.println("Username: " + getUsername());
        System.out.println("Password: " + (getPassword().isEmpty() ? "[empty]" : "***"));
        System.out.println("Driver: " + getDriver());
        System.out.println("Pool Size: " + getPoolSize());
        System.out.println("Max Connections: " + getMaxConnections());
        System.out.println("Connection Timeout: " + getConnectionTimeout() + "ms");
    }

    public static boolean isValidConfig() {
        String url = getUrl();
        String username = getUsername();
        String password = getPassword();

        return url != null && !url.isEmpty() &&
                username != null && !username.isEmpty();
    }
}