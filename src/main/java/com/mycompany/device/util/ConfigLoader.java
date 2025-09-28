package com.mycompany.device.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class để load cấu hình từ file config.env
 * @author Team Device Management
 */
public class ConfigLoader {
    
    private static final Logger logger = LoggerFactory.getLogger(ConfigLoader.class);
    private static final String CONFIG_FILE = "config.env";
    private static Properties properties;
    
    static {
        loadConfig();
    }
    
    /**
     * Load cấu hình từ file config.env
     */
    private static void loadConfig() {
        properties = new Properties();
        
        try {
            // Thử đọc từ file config.env trong thư mục gốc
            try (InputStream input = new FileInputStream(CONFIG_FILE)) {
                properties.load(input);
                logger.info("Configuration loaded from {}", CONFIG_FILE);
            } catch (IOException e) {
                // Nếu không tìm thấy file, sử dụng cấu hình mặc định
                logger.warn("Config file {} not found, using default configuration", CONFIG_FILE);
                setDefaultConfig();
            }
        } catch (Exception e) {
            logger.error("Error loading configuration", e);
            setDefaultConfig();
        }
    }
    
    /**
     * Thiết lập cấu hình mặc định
     */
    private static void setDefaultConfig() {
        properties.setProperty("DB_URL", "jdbc:mysql://localhost:3306/quanlythietbi?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
        properties.setProperty("DB_USERNAME", "root");
        properties.setProperty("DB_PASSWORD", "");
        properties.setProperty("DB_DRIVER", "com.mysql.cj.jdbc.Driver");
        properties.setProperty("APP_NAME", "Hệ thống quản lý thiết bị");
        properties.setProperty("APP_VERSION", "1.0.0");
        properties.setProperty("APP_ENV", "development");
        properties.setProperty("LOG_LEVEL", "INFO");
        properties.setProperty("LOG_FILE", "logs/device-management.log");
    }
    
    /**
     * Lấy giá trị cấu hình theo key
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * Lấy giá trị cấu hình theo key với giá trị mặc định
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Reload cấu hình
     */
    public static void reloadConfig() {
        loadConfig();
    }
    
    // Getters cho các cấu hình database
    public static String getDatabaseUrl() {
        return getProperty("DB_URL");
    }
    
    public static String getDatabaseUsername() {
        return getProperty("DB_USERNAME");
    }
    
    public static String getDatabasePassword() {
        return getProperty("DB_PASSWORD");
    }
    
    public static String getDatabaseDriver() {
        return getProperty("DB_DRIVER");
    }
    
    // Getters cho các cấu hình ứng dụng
    public static String getAppName() {
        return getProperty("APP_NAME");
    }
    
    public static String getAppVersion() {
        return getProperty("APP_VERSION");
    }
    
    public static String getAppEnv() {
        return getProperty("APP_ENV");
    }
    
    public static String getLogLevel() {
        return getProperty("LOG_LEVEL");
    }
    
    public static String getLogFile() {
        return getProperty("LOG_FILE");
    }
} 