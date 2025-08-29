package com.mycompany.device.observer;

import com.mycompany.device.model.Device;

/**
 * Interface cho Observer Pattern - theo dõi thay đổi của Device
 * @author Team Device Management
 */
public interface DeviceObserver {
    
    /**
     * Được gọi khi có thay đổi về Device
     */
    void onDeviceChanged(Device device, String action, String oldValue, String newValue);
    
    /**
     * Được gọi khi Device được thêm mới
     */
    void onDeviceAdded(Device device);
    
    /**
     * Được gọi khi Device bị xóa
     */
    void onDeviceDeleted(String deviceId);
    
    /**
     * Được gọi khi trạng thái Device thay đổi
     */
    void onDeviceStatusChanged(Device device, String oldStatus, String newStatus);
} 