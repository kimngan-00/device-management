package com.mycompany.device.dao.factory;

import com.mycompany.device.dao.DeviceDAO;
import com.mycompany.device.dao.UserDAO;
import com.mycompany.device.dao.impl.DeviceDAOMySQLImpl;
import com.mycompany.device.dao.impl.UserDAOMySQLImpl;
import com.mycompany.device.util.ConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory class để tạo các DAO objects (Factory Pattern)
 * @author Team Device Management
 */
public class DAOFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(DAOFactory.class);
    
    // Database types
    public enum DatabaseType {
        MYSQL,
        SQLSERVER,
        POSTGRESQL,
        IN_MEMORY
    }
    
    /**
     * Tạo DeviceDAO dựa trên cấu hình
     */
    public static DeviceDAO createDeviceDAO() {
        return createDeviceDAO(getDefaultDatabaseType());
    }
    
    /**
     * Tạo DeviceDAO với database type cụ thể
     */
    public static DeviceDAO createDeviceDAO(DatabaseType databaseType) {
        logger.info("Creating DeviceDAO for database type: {}", databaseType);
        
        switch (databaseType) {
            case MYSQL:
                return new DeviceDAOMySQLImpl();
            case IN_MEMORY:
                return new com.mycompany.device.dao.impl.DeviceDAOImpl();
            case SQLSERVER:
                // TODO: Implement SQL Server DAO
                logger.warn("SQL Server DAO not implemented yet, using MySQL");
                return new DeviceDAOMySQLImpl();
            case POSTGRESQL:
                // TODO: Implement PostgreSQL DAO
                logger.warn("PostgreSQL DAO not implemented yet, using MySQL");
                return new DeviceDAOMySQLImpl();
            default:
                logger.warn("Unknown database type: {}, using MySQL", databaseType);
                return new DeviceDAOMySQLImpl();
        }
    }
    
    /**
     * Tạo UserDAO dựa trên cấu hình
     */
    public static UserDAO createUserDAO() {
        return createUserDAO(getDefaultDatabaseType());
    }
    
    /**
     * Tạo UserDAO với database type cụ thể
     */
    public static UserDAO createUserDAO(DatabaseType databaseType) {
        logger.info("Creating UserDAO for database type: {}", databaseType);
        
        switch (databaseType) {
            case MYSQL:
                return new UserDAOMySQLImpl();
            case IN_MEMORY:
                // TODO: Implement in-memory UserDAO
                logger.warn("In-memory UserDAO not implemented yet, using MySQL");
                return new UserDAOMySQLImpl();
            case SQLSERVER:
                // TODO: Implement SQL Server DAO
                logger.warn("SQL Server DAO not implemented yet, using MySQL");
                return new UserDAOMySQLImpl();
            case POSTGRESQL:
                // TODO: Implement PostgreSQL DAO
                logger.warn("PostgreSQL DAO not implemented yet, using MySQL");
                return new UserDAOMySQLImpl();
            default:
                logger.warn("Unknown database type: {}, using MySQL", databaseType);
                return new UserDAOMySQLImpl();
        }
    }
    
    /**
     * Lấy database type mặc định từ cấu hình
     */
    private static DatabaseType getDefaultDatabaseType() {
        String dbUrl = ConfigLoader.getDatabaseUrl();
        if (dbUrl.contains("mysql")) {
            return DatabaseType.MYSQL;
        } else if (dbUrl.contains("sqlserver")) {
            return DatabaseType.SQLSERVER;
        } else if (dbUrl.contains("postgresql")) {
            return DatabaseType.POSTGRESQL;
        } else {
            return DatabaseType.MYSQL; // Default
        }
    }
} 