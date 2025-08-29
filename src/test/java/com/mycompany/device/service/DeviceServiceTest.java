package com.mycompany.device.service;

import com.mycompany.device.model.Device;
import com.mycompany.device.service.impl.DeviceServiceImpl;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Optional;

/**
 * Test cases cho DeviceService
 * @author Team Device Management
 */
public class DeviceServiceTest {
    
    private DeviceService deviceService;
    
    @Before
    public void setUp() {
        deviceService = new DeviceServiceImpl();
    }
    
    @Test
    public void testAddDevice() {
        Device device = new Device();
        device.setDeviceName("Test Device");
        device.setDeviceType("Test Type");
        device.setManufacturer("Test Manufacturer");
        
        boolean result = deviceService.addDevice(device);
        assertTrue("Thêm thiết bị phải thành công", result);
        assertNotNull("ID thiết bị phải được tạo", device.getDeviceId());
    }
    
    @Test
    public void testAddDeviceWithInvalidData() {
        Device device = new Device();
        // Không set tên thiết bị - validation sẽ fail
        
        boolean result = deviceService.addDevice(device);
        assertFalse("Thêm thiết bị không hợp lệ phải thất bại", result);
    }
    
    @Test
    public void testFindDeviceById() {
        // Thêm thiết bị trước
        Device device = new Device();
        device.setDeviceName("Test Device");
        device.setDeviceType("Test Type");
        deviceService.addDevice(device);
        
        String deviceId = device.getDeviceId();
        Optional<Device> found = deviceService.findDeviceById(deviceId);
        
        assertTrue("Phải tìm thấy thiết bị", found.isPresent());
        assertEquals("Tên thiết bị phải khớp", "Test Device", found.get().getDeviceName());
    }
    
    @Test
    public void testSearchDevicesByName() {
        // Thêm thiết bị test
        Device device = new Device();
        device.setDeviceName("Test Laptop");
        device.setDeviceType("Laptop");
        deviceService.addDevice(device);
        
        List<Device> results = deviceService.searchDevicesByName("Laptop");
        assertFalse("Phải tìm thấy ít nhất 1 thiết bị", results.isEmpty());
    }
    
    @Test
    public void testGetDevicesByType() {
        List<Device> laptops = deviceService.getDevicesByType("Laptop");
        assertNotNull("Danh sách không được null", laptops);
    }
    
    @Test
    public void testGetDevicesByStatus() {
        List<Device> availableDevices = deviceService.getDevicesByStatus(Device.DeviceStatus.AVAILABLE);
        assertNotNull("Danh sách không được null", availableDevices);
    }
    
    @Test
    public void testAssignDeviceToUser() {
        // Thêm thiết bị trước
        Device device = new Device();
        device.setDeviceName("Test Device");
        device.setDeviceType("Test Type");
        deviceService.addDevice(device);
        
        String deviceId = device.getDeviceId();
        boolean result = deviceService.assignDeviceToUser(deviceId, "USER001");
        
        assertTrue("Giao thiết bị phải thành công", result);
        
        Optional<Device> updatedDevice = deviceService.findDeviceById(deviceId);
        assertTrue("Phải tìm thấy thiết bị", updatedDevice.isPresent());
        assertEquals("Trạng thái phải là IN_USE", Device.DeviceStatus.IN_USE, updatedDevice.get().getStatus());
        assertEquals("Người được giao phải khớp", "USER001", updatedDevice.get().getAssignedTo());
    }
    
    @Test
    public void testUnassignDevice() {
        // Thêm và giao thiết bị trước
        Device device = new Device();
        device.setDeviceName("Test Device");
        device.setDeviceType("Test Type");
        deviceService.addDevice(device);
        
        String deviceId = device.getDeviceId();
        deviceService.assignDeviceToUser(deviceId, "USER001");
        
        // Thu hồi thiết bị
        boolean result = deviceService.unassignDevice(deviceId);
        assertTrue("Thu hồi thiết bị phải thành công", result);
        
        Optional<Device> updatedDevice = deviceService.findDeviceById(deviceId);
        assertTrue("Phải tìm thấy thiết bị", updatedDevice.isPresent());
        assertEquals("Trạng thái phải là AVAILABLE", Device.DeviceStatus.AVAILABLE, updatedDevice.get().getStatus());
        assertNull("Người được giao phải null", updatedDevice.get().getAssignedTo());
    }
    
    @Test
    public void testChangeDeviceStatus() {
        // Thêm thiết bị trước
        Device device = new Device();
        device.setDeviceName("Test Device");
        device.setDeviceType("Test Type");
        deviceService.addDevice(device);
        
        String deviceId = device.getDeviceId();
        boolean result = deviceService.changeDeviceStatus(deviceId, Device.DeviceStatus.MAINTENANCE);
        
        assertTrue("Thay đổi trạng thái phải thành công", result);
        
        Optional<Device> updatedDevice = deviceService.findDeviceById(deviceId);
        assertTrue("Phải tìm thấy thiết bị", updatedDevice.isPresent());
        assertEquals("Trạng thái phải là MAINTENANCE", Device.DeviceStatus.MAINTENANCE, updatedDevice.get().getStatus());
    }
    
    @Test
    public void testGetDeviceStatistics() {
        DeviceStatistics stats = deviceService.getDeviceStatistics();
        assertNotNull("Thống kê không được null", stats);
        assertTrue("Tổng số thiết bị phải >= 0", stats.getTotalDevices() >= 0);
    }
    
    @Test
    public void testValidateDevice() {
        Device validDevice = new Device();
        validDevice.setDeviceName("Valid Device");
        validDevice.setDeviceType("Valid Type");
        
        assertTrue("Thiết bị hợp lệ phải pass validation", deviceService.validateDevice(validDevice));
        
        Device invalidDevice = new Device();
        // Không set tên thiết bị
        
        assertFalse("Thiết bị không hợp lệ phải fail validation", deviceService.validateDevice(invalidDevice));
    }
    
    @Test
    public void testGenerateDeviceId() {
        String deviceId1 = deviceService.generateDeviceId();
        String deviceId2 = deviceService.generateDeviceId();
        
        assertNotNull("ID thiết bị không được null", deviceId1);
        assertNotNull("ID thiết bị không được null", deviceId2);
        assertNotEquals("Hai ID phải khác nhau", deviceId1, deviceId2);
        assertTrue("ID phải bắt đầu bằng DEV", deviceId1.startsWith("DEV"));
    }
} 