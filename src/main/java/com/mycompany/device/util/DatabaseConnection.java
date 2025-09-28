package com.mycompany.device.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton class để quản lý kết nối database MySQL
 * @author Team Device Management
 */
public class DatabaseConnection {
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    
    // Singleton instance
    private static DatabaseConnection instance;
    
    private String url;
    private String username;
    private String password;
    private String driver;
    
    // Private constructor để ngăn tạo instance từ bên ngoài
    private DatabaseConnection() {
        loadDatabaseConfig();
    }
    
    /**
     * Lấy instance duy nhất của DatabaseConnection (Singleton Pattern)
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    /**
     * Load cấu hình database từ ConfigLoader
     */
    private void loadDatabaseConfig() {
        try {
            // Sử dụng ConfigLoader để đọc cấu hình
            url = ConfigLoader.getDatabaseUrl();
            username = ConfigLoader.getDatabaseUsername();
            password = ConfigLoader.getDatabasePassword();
            driver = ConfigLoader.getDatabaseDriver();
            
            logger.info("Database configuration loaded from ConfigLoader");
        } catch (Exception e) {
            logger.error("Error loading database configuration", e);
            setDefaultConfig();
        }
    }
    
    /**
     * Set cấu hình mặc định
     */
    private void setDefaultConfig() {
        url = "jdbc:mysql://localhost:3306/device_management";
        username = "root";
        password = "";
        driver = "com.mysql.cj.jdbc.Driver";
    }
    
    /**
     * Lấy kết nối database
     */
    public Connection getConnection() throws SQLException {
        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, username, password);
            logger.debug("Database connection established");
            return connection;
        } catch (ClassNotFoundException e) {
            logger.error("MySQL driver not found", e);
            throw new SQLException("MySQL driver not found", e);
        } catch (SQLException e) {
            logger.error("Failed to connect to database", e);
            throw e;
        }
    }
    
    /**
     * Đóng kết nối database
     */
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                logger.debug("Database connection closed");
            } catch (SQLException e) {
                logger.error("Error closing database connection", e);
            }
        }
    }
    
    /**
     * Kiểm tra kết nối database
     */
    public boolean testConnection() {
        try (Connection connection = getConnection()) {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            logger.error("Database connection test failed", e);
            return false;
        }
    }
    
    /**
     * Tạo database và tables nếu chưa tồn tại
     */
    public void initializeDatabase() {
        try {
            // Trước tiên kết nối đến MySQL server (không chỉ định database)
            String serverUrl = url.substring(0, url.lastIndexOf("/"));
            Connection serverConnection = DriverManager.getConnection(serverUrl, username, password);
            
            // Tạo database nếu chưa tồn tại
            createDatabaseIfNotExists(serverConnection);
            serverConnection.close();
            
            // Sau đó kết nối đến database cụ thể và tạo tables
            try (Connection connection = getConnection()) {
                createTables(connection);
                logger.info("Database initialized successfully");
            }
        } catch (SQLException e) {
            logger.error("Failed to initialize database", e);
        }
    }
    
    /**
     * Tạo database nếu chưa tồn tại
     */
    private void createDatabaseIfNotExists(Connection connection) throws SQLException {
        String databaseName = "device_management";
        String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS " + databaseName + 
                                  " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createDatabaseSQL);
            logger.info("Database '{}' created or already exists", databaseName);
        }
    }
    
    /**
     * Tạo các bảng cần thiết
     */
    private void createTables(Connection connection) throws SQLException {
        // Tạo bảng users
        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                user_id VARCHAR(20) PRIMARY KEY,
                username VARCHAR(50) UNIQUE NOT NULL,
                full_name VARCHAR(100) NOT NULL,
                email VARCHAR(100),
                phone VARCHAR(20),
                department VARCHAR(100),
                role ENUM('ADMIN', 'MANAGER', 'USER') NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                is_active BOOLEAN DEFAULT TRUE
            )
        """;
        
        // Tạo bảng devices
        String createDevicesTable = """
            CREATE TABLE IF NOT EXISTS devices (
                device_id VARCHAR(20) PRIMARY KEY,
                device_name VARCHAR(100) NOT NULL,
                device_type VARCHAR(50) NOT NULL,
                status ENUM('AVAILABLE', 'IN_USE', 'MAINTENANCE') DEFAULT 'AVAILABLE',
                description TEXT,
                price FLOAT DEFAULT 0.0,
                manufacturer VARCHAR(100),
                serial_number VARCHAR(100),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
            )
        """;
        
        // Tạo bảng device_history để theo dõi lịch sử
        String createDeviceHistoryTable = """
            CREATE TABLE IF NOT EXISTS device_history (
                id INT AUTO_INCREMENT PRIMARY KEY,
                device_id VARCHAR(20) NOT NULL,
                action VARCHAR(50) NOT NULL,
                old_value TEXT,
                new_value TEXT,
                user_id VARCHAR(20),
                action_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (device_id) REFERENCES devices(device_id) ON DELETE CASCADE,
                FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
            )
        """;
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createDevicesTable);
            stmt.execute(createDeviceHistoryTable);
            logger.info("Database tables created successfully");
        }
    }
    
    // Getters cho cấu hình
    public String getUrl() { return url; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getDriver() { return driver; }
} 