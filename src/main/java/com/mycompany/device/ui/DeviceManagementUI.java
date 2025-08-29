package com.mycompany.device.ui;

import com.mycompany.device.model.Device;
import com.mycompany.device.model.User;
import com.mycompany.device.service.DeviceService;
import com.mycompany.device.service.DeviceStatistics;
import com.mycompany.device.service.impl.DeviceServiceImpl;
import java.util.List;
import java.util.Scanner;
import java.util.Optional;

/**
 * Giao diện người dùng cho hệ thống quản lý thiết bị
 * @author Team Member 3 - Presentation Layer
 */
public class DeviceManagementUI {
    
    private final DeviceService deviceService;
    private final Scanner scanner;
    private User currentUser;
    
    public DeviceManagementUI() {
        this.deviceService = new DeviceServiceImpl();
        this.scanner = new Scanner(System.in);
        this.currentUser = null;
    }
    
    public void startApplication() {
        System.out.println("Chào mừng đến với Hệ thống Quản lý Thiết bị!");
        
        // Đăng nhập đơn giản
        login();
        
        if (currentUser != null) {
            showMainMenu();
        } else {
            System.out.println("Đăng nhập thất bại. Tạm biệt!");
        }
        
        scanner.close();
    }
    
    private void login() {
        System.out.println("\n=== ĐĂNG NHẬP ===");
        System.out.print("Nhập username: ");
        String username = scanner.nextLine();
        
        // Đăng nhập đơn giản - trong thực tế sẽ có authentication
        if ("admin".equals(username)) {
            currentUser = new User("ADM001", "admin", "Quản trị viên", User.UserRole.ADMIN);
        } else if ("manager".equals(username)) {
            currentUser = new User("MGR001", "manager", "Quản lý", User.UserRole.MANAGER);
        } else {
            currentUser = new User("USR001", username, "Người dùng", User.UserRole.USER);
        }
        
        System.out.println("Đăng nhập thành công! Xin chào " + currentUser.getFullName());
    }
    
    private void showMainMenu() {
        while (true) {
            System.out.println("\n=== MENU CHÍNH ===");
            System.out.println("1. Quản lý thiết bị");
            System.out.println("2. Tìm kiếm thiết bị");
            System.out.println("3. Quản lý trạng thái thiết bị");
            System.out.println("4. Thống kê");
            System.out.println("5. Thoát");
            System.out.print("Chọn chức năng (1-5): ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    showDeviceManagementMenu();
                    break;
                case "2":
                    showSearchMenu();
                    break;
                case "3":
                    showAssignmentMenu();
                    break;
                case "4":
                    showStatistics();
                    break;
                case "5":
                    System.out.println("Cảm ơn bạn đã sử dụng hệ thống!");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }
    
    private void showDeviceManagementMenu() {
        while (true) {
            System.out.println("\n=== QUẢN LÝ THIẾT BỊ ===");
            System.out.println("1. Thêm thiết bị mới");
            System.out.println("2. Cập nhật thiết bị");
            System.out.println("3. Xóa thiết bị");
            System.out.println("4. Xem danh sách thiết bị");
            System.out.println("5. Quay lại menu chính");
            System.out.print("Chọn chức năng (1-5): ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    addNewDevice();
                    break;
                case "2":
                    updateDevice();
                    break;
                case "3":
                    deleteDevice();
                    break;
                case "4":
                    displayAllDevices();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }
    
    private void addNewDevice() {
        System.out.println("\n=== THÊM THIẾT BỊ MỚI ===");
        
        Device device = new Device();
        device.setDeviceId(deviceService.generateDeviceId());
        
        System.out.print("Tên thiết bị: ");
        device.setDeviceName(scanner.nextLine());
        
        System.out.print("Loại thiết bị: ");
        device.setDeviceType(scanner.nextLine());
        
        System.out.print("Mô tả: ");
        device.setDescription(scanner.nextLine());
        
        System.out.print("Giá (VND): ");
        try {
            float price = Float.parseFloat(scanner.nextLine());
            device.setPrice(price);
        } catch (NumberFormatException e) {
            device.setPrice(0.0f);
            System.out.println("Giá không hợp lệ, đặt mặc định là 0");
        }
        
        System.out.print("Nhà sản xuất: ");
        device.setManufacturer(scanner.nextLine());
        
        System.out.print("Số serial: ");
        device.setSerialNumber(scanner.nextLine());
        
        if (deviceService.addDevice(device)) {
            System.out.println("Thêm thiết bị thành công!");
        } else {
            System.out.println("Thêm thiết bị thất bại!");
        }
    }
    
    private void updateDevice() {
        System.out.println("\n=== CẬP NHẬT THIẾT BỊ ===");
        System.out.print("Nhập ID thiết bị: ");
        String deviceId = scanner.nextLine();
        
        Optional<Device> deviceOpt = deviceService.findDeviceById(deviceId);
        if (deviceOpt.isPresent()) {
            Device device = deviceOpt.get();
            System.out.println("Thiết bị hiện tại: " + device);
            
            System.out.print("Tên thiết bị mới (Enter để giữ nguyên): ");
            String newName = scanner.nextLine();
            if (!newName.isEmpty()) {
                device.setDeviceName(newName);
            }
            
            System.out.print("Mô tả mới (Enter để giữ nguyên): ");
            String newDescription = scanner.nextLine();
            if (!newDescription.isEmpty()) {
                device.setDescription(newDescription);
            }
            
            System.out.print("Giá mới (VND, Enter để giữ nguyên): ");
            String priceStr = scanner.nextLine();
            if (!priceStr.isEmpty()) {
                try {
                    float newPrice = Float.parseFloat(priceStr);
                    device.setPrice(newPrice);
                } catch (NumberFormatException e) {
                    System.out.println("Giá không hợp lệ, giữ nguyên giá cũ");
                }
            }
            
            System.out.print("Nhà sản xuất mới (Enter để giữ nguyên): ");
            String newManufacturer = scanner.nextLine();
            if (!newManufacturer.isEmpty()) {
                device.setManufacturer(newManufacturer);
            }
            
            System.out.print("Số serial mới (Enter để giữ nguyên): ");
            String newSerialNumber = scanner.nextLine();
            if (!newSerialNumber.isEmpty()) {
                device.setSerialNumber(newSerialNumber);
            }
            
            if (deviceService.updateDevice(device)) {
                System.out.println("Cập nhật thành công!");
            } else {
                System.out.println("Cập nhật thất bại!");
            }
        } else {
            System.out.println("Không tìm thấy thiết bị!");
        }
    }
    
    private void deleteDevice() {
        System.out.println("\n=== XÓA THIẾT BỊ ===");
        System.out.print("Nhập ID thiết bị: ");
        String deviceId = scanner.nextLine();
        
        if (deviceService.deleteDevice(deviceId)) {
            System.out.println("Xóa thiết bị thành công!");
        } else {
            System.out.println("Xóa thiết bị thất bại!");
        }
    }
    
    private void displayAllDevices() {
        System.out.println("\n=== DANH SÁCH THIẾT BỊ ===");
        List<Device> devices = deviceService.getAllDevices();
        
        if (devices.isEmpty()) {
            System.out.println("Không có thiết bị nào!");
            return;
        }
        
        System.out.printf("%-10s %-20s %-15s %-15s %-15s %-15s %-20s%n", 
                         "ID", "Tên", "Loại", "Trạng thái", "Giá (VND)", "NSX", "Số Serial");
        System.out.println("=".repeat(110));
        
        for (Device device : devices) {
            System.out.printf("%-10s %-20s %-15s %-15s %-15s %-15s %-20s%n",
                             device.getDeviceId(),
                             device.getDeviceName(),
                             device.getDeviceType(),
                             device.getStatus().getDisplayName(),
                             String.format("%,.0f", device.getPrice()),
                             device.getManufacturer() != null ? device.getManufacturer() : "",
                             device.getSerialNumber() != null ? device.getSerialNumber() : "");
        }
    }
    
    private void showSearchMenu() {
        System.out.println("\n=== TÌM KIẾM THIẾT BỊ ===");
        System.out.println("1. Tìm theo tên");
        System.out.println("2. Tìm theo loại");
        System.out.println("3. Tìm theo trạng thái");
        System.out.print("Chọn loại tìm kiếm (1-3): ");
        
        String choice = scanner.nextLine();
        List<Device> results = null;
        
        switch (choice) {
            case "1":
                System.out.print("Nhập tên thiết bị: ");
                results = deviceService.searchDevicesByName(scanner.nextLine());
                break;
            case "2":
                System.out.print("Nhập loại thiết bị: ");
                results = deviceService.getDevicesByType(scanner.nextLine());
                break;
            case "3":
                System.out.println("Chọn trạng thái:");
                Device.DeviceStatus[] statuses = Device.DeviceStatus.values();
                for (int i = 0; i < statuses.length; i++) {
                    System.out.println((i + 1) + ". " + statuses[i].getDisplayName());
                }
                System.out.print("Nhập số (1-" + statuses.length + "): ");
                try {
                    int statusIndex = Integer.parseInt(scanner.nextLine()) - 1;
                    if (statusIndex >= 0 && statusIndex < statuses.length) {
                        results = deviceService.getDevicesByStatus(statuses[statusIndex]);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Số không hợp lệ!");
                }
                break;

            default:
                System.out.println("Lựa chọn không hợp lệ!");
                return;
        }
        
        if (results != null) {
            displaySearchResults(results);
        }
    }
    
    private void displaySearchResults(List<Device> devices) {
        if (devices.isEmpty()) {
            System.out.println("Không tìm thấy thiết bị nào!");
            return;
        }
        
        System.out.println("\nKết quả tìm kiếm (" + devices.size() + " thiết bị):");
        System.out.printf("%-10s %-20s %-15s %-15s %-15s %-15s %-20s%n", 
                         "ID", "Tên", "Loại", "Trạng thái", "Giá (VND)", "NSX", "Số Serial");
        System.out.println("=".repeat(110));
        
        for (Device device : devices) {
            System.out.printf("%-10s %-20s %-15s %-15s %-15s %-15s %-20s%n",
                             device.getDeviceId(),
                             device.getDeviceName(),
                             device.getDeviceType(),
                             device.getStatus().getDisplayName(),
                             String.format("%,.0f", device.getPrice()),
                             device.getManufacturer() != null ? device.getManufacturer() : "",
                             device.getSerialNumber() != null ? device.getSerialNumber() : "");
        }
    }
    
    private void showAssignmentMenu() {
        System.out.println("\n=== QUẢN LÝ TRẠNG THÁI THIẾT BỊ ===");
        System.out.println("1. Thay đổi trạng thái");
        System.out.print("Chọn chức năng (1): ");
        
        String choice = scanner.nextLine();
        
        switch (choice) {
            case "1":
                changeDeviceStatus();
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
        }
    }
    

    
    private void changeDeviceStatus() {
        System.out.println("\n=== THAY ĐỔI TRẠNG THÁI THIẾT BỊ ===");
        System.out.print("Nhập ID thiết bị: ");
        String deviceId = scanner.nextLine();
        
        System.out.println("Chọn trạng thái mới:");
        Device.DeviceStatus[] statuses = Device.DeviceStatus.values();
        for (int i = 0; i < statuses.length; i++) {
            System.out.println((i + 1) + ". " + statuses[i].getDisplayName());
        }
        System.out.print("Nhập số (1-" + statuses.length + "): ");
        
        try {
            int statusIndex = Integer.parseInt(scanner.nextLine()) - 1;
            if (statusIndex >= 0 && statusIndex < statuses.length) {
                if (deviceService.changeDeviceStatus(deviceId, statuses[statusIndex])) {
                    System.out.println("Thay đổi trạng thái thành công!");
                } else {
                    System.out.println("Thay đổi trạng thái thất bại!");
                }
            } else {
                System.out.println("Số không hợp lệ!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Số không hợp lệ!");
        }
    }
    
    private void showStatistics() {
        System.out.println("\n=== THỐNG KÊ ===");
        DeviceStatistics stats = deviceService.getDeviceStatistics();
        System.out.println(stats);
    }
} 