package com.mycompany.device.dao.impl;

import com.mycompany.device.dao.DeviceDAO;
import com.mycompany.device.model.Device;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation của DeviceDAO sử dụng in-memory storage
 * @author Team Member 2 - Data Access Layer
 */
public class DeviceDAOImpl implements DeviceDAO {
    
    private final Map<String, Device> devices = new HashMap<>();
    
    @Override
    public boolean addDevice(Device device) {
        if (device == null || device.getDeviceId() == null) {
            return false;
        }
        
        if (devices.containsKey(device.getDeviceId())) {
            return false; // Device ID đã tồn tại
        }
        
        devices.put(device.getDeviceId(), device);
        return true;
    }
    
    @Override
    public boolean updateDevice(Device device) {
        if (device == null || device.getDeviceId() == null) {
            return false;
        }
        
        if (!devices.containsKey(device.getDeviceId())) {
            return false; // Device không tồn tại
        }
        
        devices.put(device.getDeviceId(), device);
        return true;
    }
    
    @Override
    public boolean deleteDevice(String deviceId) {
        if (deviceId == null) {
            return false;
        }
        
        return devices.remove(deviceId) != null;
    }
    
    @Override
    public Optional<Device> findById(String deviceId) {
        if (deviceId == null) {
            return Optional.empty();
        }
        
        return Optional.ofNullable(devices.get(deviceId));
    }
    
    @Override
    public List<Device> getAllDevices() {
        return new ArrayList<>(devices.values());
    }
    
    @Override
    public List<Device> findByName(String deviceName) {
        if (deviceName == null) {
            return new ArrayList<>();
        }
        
        return devices.values().stream()
                .filter(device -> device.getDeviceName() != null && 
                        device.getDeviceName().toLowerCase().contains(deviceName.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Device> findByType(String deviceType) {
        if (deviceType == null) {
            return new ArrayList<>();
        }
        
        return devices.values().stream()
                .filter(device -> device.getDeviceType() != null && 
                        device.getDeviceType().equalsIgnoreCase(deviceType))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Device> findByStatus(Device.DeviceStatus status) {
        if (status == null) {
            return new ArrayList<>();
        }
        
        return devices.values().stream()
                .filter(device -> device.getStatus() == status)
                .collect(Collectors.toList());
    }
    

    
    @Override
    public int getTotalDeviceCount() {
        return devices.size();
    }
    
    @Override
    public int getDeviceCountByStatus(Device.DeviceStatus status) {
        if (status == null) {
            return 0;
        }
        
        return (int) devices.values().stream()
                .filter(device -> device.getStatus() == status)
                .count();
    }
} 