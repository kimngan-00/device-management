package com.mycompany.device;

import com.mycompany.device.model.PhongBan;
import com.mycompany.device.model.NhanVien;
import com.mycompany.device.ui.PhongBanController;
import com.mycompany.device.service.PhongBanService;
import com.mycompany.device.service.impl.PhongBanServiceImpl;

/**
 * Ứng dụng chính quản lý phòng ban và nhân viên
 * @author Kim Ngan
 */
public class DeviceManagementApp {
    
    public static void main(String[] args) {
        System.out.println("=== HỆ THỐNG QUẢN LÝ PHÒNG BAN VÀ NHÂN VIÊN ===");
        
        // Test model cơ bản
        testModels();
        
        // Test database
        testDatabase();
        
        // Chạy giao diện quản lý phòng ban
        runPhongBanManagement();
    }
    
    private static void testModels() {
        System.out.println("\n--- Test Models ---");
        
        // Test PhongBan
        PhongBan pb1 = new PhongBan("PB001", "Phòng Kỹ thuật", "Quản lý kỹ thuật và công nghệ");
        System.out.println("Phòng ban 1: " + pb1);
        
        // Test NhanVien
        NhanVien nv1 = new NhanVien("NV001", "Nguyễn Văn A", "nva@company.com", "password123");
        nv1.setSoDienThoai("0123456789");
        nv1.setMaPhongBan("PB001");
        System.out.println("Nhân viên 1: " + nv1);
        
        System.out.println("✓ Test Models thành công!");
    }
    
    private static void testDatabase() {
        System.out.println("\n--- Test Database ---");
        
        try {
            PhongBanService service = new PhongBanServiceImpl();
            
            // Test 1: Tạo phòng ban
            System.out.println("1. Test tạo phòng ban:");
            boolean result1 = service.taoPhongBan("PB001", "Phòng Kỹ thuật", "Quản lý kỹ thuật và công nghệ");
            System.out.println("   Kết quả: " + (result1 ? "✓ Thành công" : "✗ Thất bại"));
            
            // Test 2: Tạo phòng ban thứ 2
            System.out.println("\n2. Test tạo phòng ban thứ 2:");
            boolean result2 = service.taoPhongBan("PB002", "Phòng Nhân sự", "Quản lý nhân sự và tuyển dụng");
            System.out.println("   Kết quả: " + (result2 ? "✓ Thành công" : "✗ Thất bại"));
            
            // Test 3: Xem danh sách
            System.out.println("\n3. Test xem danh sách phòng ban:");
            var danhSach = service.xemDanhSachPhongBan();
            System.out.println("   Số lượng phòng ban: " + danhSach.size());
            for (var pb : danhSach) {
                System.out.println("   - " + pb);
            }
            
            System.out.println("\n✓ Test Database thành công!");
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi Database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void runPhongBanManagement() {
        System.out.println("\n--- QUẢN LÝ PHÒNG BAN ---");
        
        PhongBanController controller = new PhongBanController();
        
        try {
            controller.xuLyMenu();
        } finally {
            controller.dong();
        }
    }
}
