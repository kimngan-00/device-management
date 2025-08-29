package com.mycompany.device.dao.impl;

import com.mycompany.device.dao.DeviceDAO;
import com.mycompany.device.model.Device;
import com.mycompany.device.util.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation của DeviceDAO sử dụng MySQL database
 * @author Team Member 2 - Data Access Layer
 */
public class DeviceDAOMySQLImpl implements DeviceDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(DeviceDAOMySQLImpl.class);
    
    @Override
    public boolean addDevice(Device device) {
        String sql = """
            INSERT INTO devices (device_id, device_name, device_type, status, description, price, manufacturer, serial_number)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, device.getDeviceId());
            pstmt.setString(2, device.getDeviceName());
            pstmt.setString(3, device.getDeviceType());
            pstmt.setString(4, device.getStatus().name());
            pstmt.setString(5, device.getDescription());
            pstmt.setFloat(6, device.getPrice());
            pstmt.setString(7, device.getManufacturer());
            pstmt.setString(8, device.getSerialNumber());
            
            int result = pstmt.executeUpdate();
            logger.info("Device added successfully: {}", device.getDeviceId());
            return result > 0;
            
        } catch (SQLException e) {
            logger.error("Error adding device: {}", device.getDeviceId(), e);
            return false;
        }
    }
    
    @Override
    public boolean updateDevice(Device device) {
        String sql = """
            UPDATE devices SET device_name = ?, device_type = ?, status = ?, description = ?, 
                             price = ?, manufacturer = ?, serial_number = ?
            WHERE device_id = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, device.getDeviceName());
            pstmt.setString(2, device.getDeviceType());
            pstmt.setString(3, device.getStatus().name());
            pstmt.setString(4, device.getDescription());
            pstmt.setFloat(5, device.getPrice());
            pstmt.setString(6, device.getManufacturer());
            pstmt.setString(7, device.getSerialNumber());
            pstmt.setString(8, device.getDeviceId());
            
            int result = pstmt.executeUpdate();
            logger.info("Device updated successfully: {}", device.getDeviceId());
            return result > 0;
            
        } catch (SQLException e) {
            logger.error("Error updating device: {}", device.getDeviceId(), e);
            return false;
        }
    }
    
    @Override
    public boolean deleteDevice(String deviceId) {
        String sql = "DELETE FROM devices WHERE device_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, deviceId);
            int result = pstmt.executeUpdate();
            logger.info("Device deleted successfully: {}", deviceId);
            return result > 0;
            
        } catch (SQLException e) {
            logger.error("Error deleting device: {}", deviceId, e);
            return false;
        }
    }
    
    @Override
    public Optional<Device> findById(String deviceId) {
        String sql = "SELECT * FROM devices WHERE device_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, deviceId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Device device = mapResultSetToDevice(rs);
                return Optional.of(device);
            }
            
        } catch (SQLException e) {
            logger.error("Error finding device by ID: {}", deviceId, e);
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<Device> getAllDevices() {
        String sql = "SELECT * FROM devices ORDER BY device_id";
        List<Device> devices = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Device device = mapResultSetToDevice(rs);
                devices.add(device);
            }
            
            logger.debug("Retrieved {} devices from database", devices.size());
            
        } catch (SQLException e) {
            logger.error("Error retrieving all devices", e);
        }
        
        return devices;
    }
    
    @Override
    public List<Device> findByName(String deviceName) {
        String sql = "SELECT * FROM devices WHERE device_name LIKE ? ORDER BY device_name";
        List<Device> devices = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + deviceName + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Device device = mapResultSetToDevice(rs);
                devices.add(device);
            }
            
        } catch (SQLException e) {
            logger.error("Error finding devices by name: {}", deviceName, e);
        }
        
        return devices;
    }
    
    @Override
    public List<Device> findByType(String deviceType) {
        String sql = "SELECT * FROM devices WHERE device_type = ? ORDER BY device_name";
        List<Device> devices = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, deviceType);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Device device = mapResultSetToDevice(rs);
                devices.add(device);
            }
            
        } catch (SQLException e) {
            logger.error("Error finding devices by type: {}", deviceType, e);
        }
        
        return devices;
    }
    
    @Override
    public List<Device> findByStatus(Device.DeviceStatus status) {
        String sql = "SELECT * FROM devices WHERE status = ? ORDER BY device_name";
        List<Device> devices = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status.name());
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Device device = mapResultSetToDevice(rs);
                devices.add(device);
            }
            
        } catch (SQLException e) {
            logger.error("Error finding devices by status: {}", status, e);
        }
        
        return devices;
    }
    

    
    @Override
    public int getTotalDeviceCount() {
        String sql = "SELECT COUNT(*) FROM devices";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            logger.error("Error getting total device count", e);
        }
        
        return 0;
    }
    
    @Override
    public int getDeviceCountByStatus(Device.DeviceStatus status) {
        String sql = "SELECT COUNT(*) FROM devices WHERE status = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status.name());
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            logger.error("Error getting device count by status: {}", status, e);
        }
        
        return 0;
    }
    
    /**
     * Map ResultSet thành Device object
     */
    private Device mapResultSetToDevice(ResultSet rs) throws SQLException {
        Device device = new Device();
        
        device.setDeviceId(rs.getString("device_id"));
        device.setDeviceName(rs.getString("device_name"));
        device.setDeviceType(rs.getString("device_type"));
        device.setStatus(Device.DeviceStatus.valueOf(rs.getString("status")));
        device.setDescription(rs.getString("description"));
        device.setPrice(rs.getFloat("price"));
        device.setManufacturer(rs.getString("manufacturer"));
        device.setSerialNumber(rs.getString("serial_number"));
        
        return device;
    }
} 