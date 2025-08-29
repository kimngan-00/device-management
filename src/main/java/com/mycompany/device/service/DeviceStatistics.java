package com.mycompany.device.service;

import com.mycompany.device.model.Device;
import java.util.Map;
import java.util.HashMap;

/**
 * Class chứa thống kê về thiết bị
 * @author Team Member 3 - Business Logic Layer
 */
public class DeviceStatistics {
    private int totalDevices;
    private Map<Device.DeviceStatus, Integer> devicesByStatus;
    private Map<String, Integer> devicesByType;
    private int assignedDevices;
    private int availableDevices;
    
    public DeviceStatistics() {
        this.devicesByStatus = new HashMap<>();
        this.devicesByType = new HashMap<>();
    }
    
    // Getters and Setters
    public int getTotalDevices() { return totalDevices; }
    public void setTotalDevices(int totalDevices) { this.totalDevices = totalDevices; }
    
    public Map<Device.DeviceStatus, Integer> getDevicesByStatus() { return devicesByStatus; }
    public void setDevicesByStatus(Map<Device.DeviceStatus, Integer> devicesByStatus) { 
        this.devicesByStatus = devicesByStatus; 
    }
    
    public Map<String, Integer> getDevicesByType() { return devicesByType; }
    public void setDevicesByType(Map<String, Integer> devicesByType) { 
        this.devicesByType = devicesByType; 
    }
    
    public int getAssignedDevices() { return assignedDevices; }
    public void setAssignedDevices(int assignedDevices) { this.assignedDevices = assignedDevices; }
    
    public int getAvailableDevices() { return availableDevices; }
    public void setAvailableDevices(int availableDevices) { this.availableDevices = availableDevices; }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== THỐNG KÊ THIẾT BỊ ===\n");
        sb.append("Tổng số thiết bị: ").append(totalDevices).append("\n");
        sb.append("Thiết bị có sẵn: ").append(availableDevices).append("\n");
        sb.append("Thiết bị đã giao: ").append(assignedDevices).append("\n");
        
        sb.append("\nTheo trạng thái:\n");
        devicesByStatus.forEach((status, count) -> 
            sb.append("- ").append(status.getDisplayName()).append(": ").append(count).append("\n"));
        
        sb.append("\nTheo loại:\n");
        devicesByType.forEach((type, count) -> 
            sb.append("- ").append(type).append(": ").append(count).append("\n"));
        
        return sb.toString();
    }
} 