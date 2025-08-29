package com.mycompany.device.dao;

import com.mycompany.device.model.Device;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface cho Device
 * @author Team Member 2 - Data Access Layer
 */
public interface DeviceDAO {
    
    /**
     * Thêm thiết bị mới
     */
    boolean addDevice(Device device);
    
    /**
     * Cập nhật thông tin thiết bị
     */
    boolean updateDevice(Device device);
    
    /**
     * Xóa thiết bị theo ID
     */
    boolean deleteDevice(String deviceId);
    
    /**
     * Tìm thiết bị theo ID
     */
    Optional<Device> findById(String deviceId);
    
    /**
     * Lấy tất cả thiết bị
     */
    List<Device> getAllDevices();
    
    /**
     * Tìm thiết bị theo tên
     */
    List<Device> findByName(String deviceName);
    
    /**
     * Tìm thiết bị theo loại
     */
    List<Device> findByType(String deviceType);
    
    /**
     * Tìm thiết bị theo trạng thái
     */
    List<Device> findByStatus(Device.DeviceStatus status);
    

    
    /**
     * Đếm tổng số thiết bị
     */
    int getTotalDeviceCount();
    
    /**
     * Đếm thiết bị theo trạng thái
     */
    int getDeviceCountByStatus(Device.DeviceStatus status);
} 