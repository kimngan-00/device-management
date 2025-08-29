package com.mycompany.device;

import com.mycompany.device.ui.DeviceManagementUI;
import com.mycompany.device.util.ConfigLoader;

/**
 * Ứng dụng chính quản lý thiết bị
 * @author Team Device Management
 */
public class DeviceManagementApp {
    
    public static void main(String[] args) {
        System.out.println("=== " + ConfigLoader.getAppName() + " ===");
        System.out.println("Phiên bản: " + ConfigLoader.getAppVersion());
        System.out.println("Môi trường: " + ConfigLoader.getAppEnv());
        System.out.println("Tác giả: Team Device Management");
        System.out.println("=====================================");
        
        DeviceManagementUI ui = new DeviceManagementUI();
        ui.startApplication();
    }
} 