package com.mycompany.device.service;

import com.mycompany.device.model.Device;
import java.util.List;
import java.util.Optional;

/**
 * Service interface cho Device - Business Logic Layer
 * @author Team Member 3 - Business Logic Layer
 */
public interface DeviceService {
    
    /**
     * Thêm thiết bị mới với validation
     */
    boolean addDevice(Device device);
    
    /**
     * Cập nhật thông tin thiết bị
     */
    boolean updateDevice(Device device);
    
    /**
     * Xóa thiết bị (chỉ khi không được giao cho ai)
     */
    boolean deleteDevice(String deviceId);
    
    /**
     * Tìm thiết bị theo ID
     */
    Optional<Device> findDeviceById(String deviceId);
    
    /**
     * Lấy tất cả thiết bị
     */
    List<Device> getAllDevices();
    
    /**
     * Tìm thiết bị theo tên
     */
    List<Device> searchDevicesByName(String deviceName);
    
    /**
     * Tìm thiết bị theo loại
     */
    List<Device> getDevicesByType(String deviceType);
    
    /**
     * Tìm thiết bị theo trạng thái
     */
    List<Device> getDevicesByStatus(Device.DeviceStatus status);
    
    /**
     * Thay đổi trạng thái thiết bị
     */
    boolean changeDeviceStatus(String deviceId, Device.DeviceStatus newStatus);
    
    /**
     * Lấy thống kê thiết bị
     */
    DeviceStatistics getDeviceStatistics();
    
    /**
     * Validate thông tin thiết bị
     */
    boolean validateDevice(Device device);
    
    /**
     * Tạo ID thiết bị tự động
     */
    String generateDeviceId();
} 