package com.mycompany.device.model;

/**
 * Builder class cho Device (Builder Pattern)
 * @author Team Device Management
 */
public class DeviceBuilder {
    
    private String deviceId;
    private String deviceName;
    private String deviceType;
    private Device.DeviceStatus status = Device.DeviceStatus.AVAILABLE;
    private String description;
    private float price = 0.0f;
    private String manufacturer;
    private String serialNumber;
    
    public DeviceBuilder() {}
    
    public DeviceBuilder(String deviceId, String deviceName, String deviceType) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
    }
    
    public DeviceBuilder deviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }
    
    public DeviceBuilder deviceName(String deviceName) {
        this.deviceName = deviceName;
        return this;
    }
    
    public DeviceBuilder deviceType(String deviceType) {
        this.deviceType = deviceType;
        return this;
    }
    
    public DeviceBuilder status(Device.DeviceStatus status) {
        this.status = status;
        return this;
    }
    
    public DeviceBuilder description(String description) {
        this.description = description;
        return this;
    }
    
    public DeviceBuilder price(float price) {
        this.price = price;
        return this;
    }
    
    public DeviceBuilder manufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
        return this;
    }
    
    public DeviceBuilder serialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
        return this;
    }
    
    /**
     * Build và trả về Device object
     */
    public Device build() {
        Device device = new Device(deviceId, deviceName, deviceType);
        device.setStatus(status);
        device.setDescription(description);
        device.setPrice(price);
        device.setManufacturer(manufacturer);
        device.setSerialNumber(serialNumber);
        return device;
    }
    
    /**
     * Tạo builder với thông tin cơ bản
     */
    public static DeviceBuilder create(String deviceId, String deviceName, String deviceType) {
        return new DeviceBuilder(deviceId, deviceName, deviceType);
    }
    
    /**
     * Tạo builder cho laptop
     */
    public static DeviceBuilder createLaptop(String deviceId, String deviceName) {
        return new DeviceBuilder(deviceId, deviceName, "Laptop");
    }
    
    /**
     * Tạo builder cho máy in
     */
    public static DeviceBuilder createPrinter(String deviceId, String deviceName) {
        return new DeviceBuilder(deviceId, deviceName, "Máy in");
    }
    
    /**
     * Tạo builder cho màn hình
     */
    public static DeviceBuilder createMonitor(String deviceId, String deviceName) {
        return new DeviceBuilder(deviceId, deviceName, "Màn hình");
    }
    
    /**
     * Tạo builder cho điện thoại
     */
    public static DeviceBuilder createPhone(String deviceId, String deviceName) {
        return new DeviceBuilder(deviceId, deviceName, "Điện thoại");
    }
} 