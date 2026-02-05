package com.revshop.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Logger configuration and initialization
 * Provides centralized logging management
 */
public class LoggerConfig {
    private static boolean initialized = false;
    private static final String LOG_FILE_PATH = "logs/revshop.log";

    /**
     * Initialize Log4j2 configuration
     */
    public static void initialize() {
        if (initialized) {
            return;
        }

        try {
            // Create logs directory if it doesn't exist
            File logDir = new File("logs");
            if (!logDir.exists()) {
                boolean created = logDir.mkdirs();
                if (created) {
                    System.out.println("Created logs directory: " + logDir.getAbsolutePath());
                }
            }

            // Try to load configuration from file
            File configFile = new File("src/main/resources/log4j2.xml");
            if (configFile.exists()) {
                try (InputStream inputStream = new FileInputStream(configFile)) {
                    ConfigurationSource source = new ConfigurationSource(inputStream, configFile);
                    LoggerContext context = (LoggerContext) LogManager.getContext(false);
                    context.setConfigLocation(source.getURI());
                    System.out.println("Log4j2 configuration loaded from file");
                }
            } else {
                // Use default configuration
                System.out.println("log4j2.xml not found, using default configuration");
            }

            initialized = true;

            // Test logging
            Logger logger = getLogger(LoggerConfig.class);
            logger.info("Logging system initialized successfully");
            logger.debug("Log file location: " + new File(LOG_FILE_PATH).getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Error initializing logger: " + e.getMessage());
            // Continue with default configuration
            initialized = true;
        }
    }

    /**
     * Get logger for a class
     */
    public static Logger getLogger(Class<?> clazz) {
        if (!initialized) {
            initialize();
        }
        return LogManager.getLogger(clazz);
    }

    /**
     * Get logger by name
     */
    public static Logger getLogger(String name) {
        if (!initialized) {
            initialize();
        }
        return LogManager.getLogger(name);
    }

    /**
     * Log application startup information
     */
    public static void logApplicationStart() {
        Logger logger = getLogger("RevShop");
        logger.info("==========================================");
        logger.info("      RevShop E-Commerce Application");
        logger.info("==========================================");
        logger.info("Application starting...");

        // Log system information
        logger.info("Java Version: " + System.getProperty("java.version"));
        logger.info("Java Home: " + System.getProperty("java.home"));
        logger.info("OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
        logger.info("User: " + System.getProperty("user.name"));
        logger.info("Working Directory: " + System.getProperty("user.dir"));
    }

    /**
     * Log application shutdown
     */
    public static void logApplicationShutdown() {
        Logger logger = getLogger("RevShop");
        logger.info("Application shutting down...");
        logger.info("==========================================");
    }

    /**
     * Log database connection status
     */
    public static void logDatabaseStatus(String status, String message) {
        Logger logger = getLogger("Database");
        if ("SUCCESS".equals(status)) {
            logger.info("Database: " + message);
        } else if ("ERROR".equals(status)) {
            logger.error("Database Error: " + message);
        } else if ("WARN".equals(status)) {
            logger.warn("Database Warning: " + message);
        } else {
            logger.debug("Database: " + message);
        }
    }

    /**
     * Log user activity
     */
    public static void logUserActivity(String username, String action, String details) {
        Logger logger = getLogger("UserActivity");
        logger.info("User: {} | Action: {} | Details: {}", username, action, details);
    }

    /**
     * Log order activity
     */
    public static void logOrderActivity(int orderId, String action, String details) {
        Logger logger = getLogger("OrderActivity");
        logger.info("Order #{} | Action: {} | Details: {}", orderId, action, details);
    }

    /**
     * Get log file path
     */
    public static String getLogFilePath() {
        return LOG_FILE_PATH;
    }

    /**
     * Check if logging is initialized
     */
    public static boolean isInitialized() {
        return initialized;
    }
}