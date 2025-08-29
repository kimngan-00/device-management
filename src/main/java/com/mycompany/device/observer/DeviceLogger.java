package com.mycompany.device.observer;

import com.mycompany.device.model.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logger observer để ghi log các thay đổi của Device (Observer Pattern)
 * @author Team Device Management
 */
public class DeviceLogger implements DeviceObserver {
    
    private static final Logger logger = LoggerFactory.getLogger(DeviceLogger.class);
    
    @Override
    public void onDeviceChanged(Device device, String action, String oldValue, String newValue) {
        logger.info("Device {} {}: {} -> {}", 
                   device.getDeviceId(), action, oldValue, newValue);
    }
    
    @Override
    public void onDeviceAdded(Device device) {
        logger.info("Device added: {} - {} ({})", 
                   device.getDeviceId(), device.getDeviceName(), device.getDeviceType());
    }
    
    @Override
    public void onDeviceDeleted(String deviceId) {
        logger.info("Device deleted: {}", deviceId);
    }
    
    @Override
    public void onDeviceStatusChanged(Device device, String oldStatus, String newStatus) {
        logger.info("Device {} status changed: {} -> {}", 
                   device.getDeviceId(), oldStatus, newStatus);
    }
} 