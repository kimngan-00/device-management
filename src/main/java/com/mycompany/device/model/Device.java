package com.mycompany.device.model;

import java.time.LocalDateTime;

/**
 * Model đại diện cho một thiết bị trong hệ thống
 * @author Kim Ngan - Model Layer
 */
public class Device {
    private String deviceId;
    private String deviceName;
    private String deviceType;
    private DeviceStatus status;
    private String description;
    private float price;
    private String manufacturer;
    private String serialNumber;
    
    public enum DeviceStatus {
        AVAILABLE("Có sẵn"),
        IN_USE("Đang sử dụng"),
        MAINTENANCE("Bảo trì");
        
        private final String displayName;
        
        DeviceStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    // Constructors
    public Device() {}
    
    public Device(String deviceId, String deviceName, String deviceType) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.status = DeviceStatus.AVAILABLE;
    }
    
    // Getters and Setters
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    
    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
    
    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
    
    public DeviceStatus getStatus() { return status; }
    public void setStatus(DeviceStatus status) { this.status = status; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public float getPrice() { return price; }
    public void setPrice(float price) { this.price = price; }
    
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    
    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    
    @Override
    public String toString() {
        return String.format("Device{id='%s', name='%s', type='%s', status='%s', price='%.0f', manufacturer='%s', serialNumber='%s'}", 
                           deviceId, deviceName, deviceType, status.getDisplayName(), price, manufacturer, serialNumber);
    }
} 