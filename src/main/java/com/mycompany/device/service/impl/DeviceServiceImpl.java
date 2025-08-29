package com.mycompany.device.service.impl;

import com.mycompany.device.dao.DeviceDAO;
import com.mycompany.device.dao.impl.DeviceDAOMySQLImpl;
import com.mycompany.device.model.Device;
import com.mycompany.device.service.DeviceService;
import com.mycompany.device.service.DeviceStatistics;
import com.mycompany.device.util.DatabaseConnection;
import java.util.*;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation của DeviceService
 * @author Team Member 3 - Business Logic Layer
 */
public class DeviceServiceImpl implements DeviceService {
    
    private static final Logger logger = LoggerFactory.getLogger(DeviceServiceImpl.class);
    private final DeviceDAO deviceDAO;
    private int deviceCounter = 1;
    
    public DeviceServiceImpl() {
        // Khởi tạo database
        DatabaseConnection.initializeDatabase();
        this.deviceDAO = new DeviceDAOMySQLImpl();
        
        // Kiểm tra kết nối database
        if (!DatabaseConnection.testConnection()) {
            logger.error("Cannot connect to database. Please check your MySQL configuration.");
            throw new RuntimeException("Database connection failed");
        }
        
        initializeSampleData();
    }
    
    @Override
    public boolean addDevice(Device device) {
        if (!validateDevice(device)) {
            return false;
        }
        
        if (device.getDeviceId() == null) {
            device.setDeviceId(generateDeviceId());
        }
        
        if (device.getStatus() == null) {
            device.setStatus(Device.DeviceStatus.AVAILABLE);
        }
        
        return deviceDAO.addDevice(device);
    }
    
    @Override
    public boolean updateDevice(Device device) {
        if (!validateDevice(device)) {
            return false;
        }
        
        return deviceDAO.updateDevice(device);
    }
    
    @Override
    public boolean deleteDevice(String deviceId) {
        Optional<Device> deviceOpt = deviceDAO.findById(deviceId);
        if (deviceOpt.isPresent()) {
            Device device = deviceOpt.get();
            // Chỉ cho phép xóa thiết bị có trạng thái AVAILABLE
            if (device.getStatus() == Device.DeviceStatus.AVAILABLE) {
                return deviceDAO.deleteDevice(deviceId);
            } else {
                return false; // Thiết bị đang được sử dụng, không thể xóa
            }
        }
        return false;
    }
    
    @Override
    public Optional<Device> findDeviceById(String deviceId) {
        return deviceDAO.findById(deviceId);
    }
    
    @Override
    public List<Device> getAllDevices() {
        return deviceDAO.getAllDevices();
    }
    
    @Override
    public List<Device> searchDevicesByName(String deviceName) {
        return deviceDAO.findByName(deviceName);
    }
    
    @Override
    public List<Device> getDevicesByType(String deviceType) {
        return deviceDAO.findByType(deviceType);
    }
    
    @Override
    public List<Device> getDevicesByStatus(Device.DeviceStatus status) {
        return deviceDAO.findByStatus(status);
    }
    

    
    @Override
    public boolean changeDeviceStatus(String deviceId, Device.DeviceStatus newStatus) {
        Optional<Device> deviceOpt = deviceDAO.findById(deviceId);
        if (deviceOpt.isPresent()) {
            Device device = deviceOpt.get();
            device.setStatus(newStatus);
            
            return deviceDAO.updateDevice(device);
        }
        return false;
    }
    

    
    @Override
    public DeviceStatistics getDeviceStatistics() {
        DeviceStatistics stats = new DeviceStatistics();
        
        List<Device> allDevices = deviceDAO.getAllDevices();
        stats.setTotalDevices(allDevices.size());
        
        // Thống kê theo trạng thái
        Map<Device.DeviceStatus, Integer> statusCount = new HashMap<>();
        for (Device.DeviceStatus status : Device.DeviceStatus.values()) {
            int count = deviceDAO.getDeviceCountByStatus(status);
            statusCount.put(status, count);
        }
        stats.setDevicesByStatus(statusCount);
        
        // Thống kê theo loại
        Map<String, Integer> typeCount = new HashMap<>();
        for (Device device : allDevices) {
            String type = device.getDeviceType();
            if (type != null) {
                typeCount.put(type, typeCount.getOrDefault(type, 0) + 1);
            }
        }
        stats.setDevicesByType(typeCount);
        
        // Đếm thiết bị đang sử dụng và có sẵn
        int inUseCount = (int) allDevices.stream()
                .filter(d -> d.getStatus() == Device.DeviceStatus.IN_USE)
                .count();
        stats.setAssignedDevices(inUseCount);
        
        int availableCount = deviceDAO.getDeviceCountByStatus(Device.DeviceStatus.AVAILABLE);
        stats.setAvailableDevices(availableCount);
        
        return stats;
    }
    
    @Override
    public boolean validateDevice(Device device) {
        if (device == null) {
            return false;
        }
        
        // Kiểm tra các trường bắt buộc
        if (device.getDeviceName() == null || device.getDeviceName().trim().isEmpty()) {
            return false;
        }
        
        if (device.getDeviceType() == null || device.getDeviceType().trim().isEmpty()) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public String generateDeviceId() {
        return String.format("DEV%06d", deviceCounter++);
    }
    
    /**
     * Khởi tạo dữ liệu mẫu
     */
    private void initializeSampleData() {
        // Thêm một số thiết bị mẫu
        Device laptop1 = new Device("DEV000001", "Laptop Dell XPS 13", "Laptop");
        laptop1.setDescription("Laptop cao cấp cho nhân viên IT");
        laptop1.setPrice(25000000);
        laptop1.setManufacturer("Dell");
        laptop1.setSerialNumber("DL123456789");
        deviceDAO.addDevice(laptop1);
        
        Device printer1 = new Device("DEV000002", "Máy in HP LaserJet", "Máy in");
        printer1.setDescription("Máy in laser đen trắng");
        printer1.setPrice(3500000);
        printer1.setManufacturer("HP");
        printer1.setSerialNumber("HP987654321");
        deviceDAO.addDevice(printer1);
        
        Device monitor1 = new Device("DEV000003", "Màn hình Samsung 24\"", "Màn hình");
        monitor1.setDescription("Màn hình LED 24 inch");
        monitor1.setPrice(4500000);
        monitor1.setManufacturer("Samsung");
        monitor1.setSerialNumber("SM456789123");
        deviceDAO.addDevice(monitor1);
        
        Device phone1 = new Device("DEV000004", "Điện thoại iPhone 14", "Điện thoại");
        phone1.setDescription("Điện thoại thông minh");
        phone1.setPrice(18000000);
        phone1.setManufacturer("Apple");
        phone1.setSerialNumber("AP789123456");
        deviceDAO.addDevice(phone1);
        
        Device tablet1 = new Device("DEV000005", "iPad Pro 12.9\"", "Máy tính bảng");
        tablet1.setDescription("Máy tính bảng cao cấp");
        tablet1.setPrice(22000000);
        tablet1.setManufacturer("Apple");
        tablet1.setSerialNumber("AP321654987");
        deviceDAO.addDevice(tablet1);
    }
} 