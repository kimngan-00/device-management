package com.mycompany.device.ui.console;

import com.mycompany.device.model.PhongBan;
import com.mycompany.device.service.PhongBanService;
import com.mycompany.device.service.impl.PhongBanServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Controller cho giao diện quản lý phòng ban
 * @author Kim Ngan - UI Controller Layer
 * File mẫu để test tính năng thôi, khi nào làm UI xóa file này làm lại 
 */
public class PhongBanController {
    
    private static final Logger logger = LoggerFactory.getLogger(PhongBanController.class);
    
    private final PhongBanService phongBanService;
    private final Scanner scanner;
    
    public PhongBanController() {
        this.phongBanService = new PhongBanServiceImpl();
        this.scanner = new Scanner(System.in);
        logger.info("Khởi tạo PhongBanController");
    }
    
    public PhongBanController(PhongBanService phongBanService) {
        this.phongBanService = phongBanService;
        this.scanner = new Scanner(System.in);
        logger.info("Khởi tạo PhongBanController với service được inject");
    }
    
    /**
     * Hiển thị menu chính
     */
    public void hienThiMenu() {
        System.out.println("\n=== QUẢN LÝ PHÒNG BAN ===");
        System.out.println("1. Tạo phòng ban mới");
        System.out.println("2. Cập nhật thông tin phòng ban");
        System.out.println("3. Xem danh sách phòng ban");
        System.out.println("4. Tìm phòng ban theo mã");
        System.out.println("5. Tìm kiếm phòng ban theo tên");
        System.out.println("6. Xóa phòng ban");
        System.out.println("7. Thống kê phòng ban");
        System.out.println("0. Thoát");
        System.out.print("Chọn chức năng: ");
    }
    
    /**
     * Xử lý menu chính
     */
    public void xuLyMenu() {
        boolean running = true;
        
        while (running) {
            hienThiMenu();
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    taoPhongBanMoi();
                    break;
                case "2":
                    capNhatPhongBan();
                    break;
                case "3":
                    xemDanhSachPhongBan();
                    break;
                case "4":
                    timPhongBanTheoMa();
                    break;
                case "5":
                    timKiemPhongBanTheoTen();
                    break;
                case "6":
                    xoaPhongBan();
                    break;
                case "7":
                    thongKePhongBan();
                    break;
                case "0":
                    System.out.println("Tạm biệt!");
                    running = false;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
            }
            
            if (running) {
                System.out.println("\nNhấn Enter để tiếp tục...");
                scanner.nextLine();
            }
        }
    }
    
    /**
     * Tạo phòng ban mới
     */
    private void taoPhongBanMoi() {
        System.out.println("\n--- TẠO PHÒNG BAN MỚI ---");
        
        System.out.print("Nhập mã phòng ban: ");
        String maPhongBan = scanner.nextLine().trim();
        
        System.out.print("Nhập tên phòng ban: ");
        String tenPhongBan = scanner.nextLine().trim();
        
        System.out.print("Nhập mô tả (có thể để trống): ");
        String moTa = scanner.nextLine().trim();
        if (moTa.isEmpty()) {
            moTa = null;
        }
        
        boolean result = phongBanService.taoPhongBan(maPhongBan, tenPhongBan, moTa);
        
        if (result) {
            System.out.println("✓ Tạo phòng ban thành công!");
        } else {
            System.out.println("✗ Tạo phòng ban thất bại!");
        }
    }
    
    /**
     * Cập nhật thông tin phòng ban
     */
    private void capNhatPhongBan() {
        System.out.println("\n--- CẬP NHẬT THÔNG TIN PHÒNG BAN ---");
        
        System.out.print("Nhập mã phòng ban cần cập nhật: ");
        String maPhongBan = scanner.nextLine().trim();
        
        // Kiểm tra phòng ban có tồn tại không
        if (!phongBanService.kiemTraPhongBanTonTai(maPhongBan)) {
            System.out.println("✗ Phòng ban với mã '" + maPhongBan + "' không tồn tại!");
            return;
        }
        
        // Hiển thị thông tin hiện tại
        Optional<PhongBan> phongBanHienTai = phongBanService.timPhongBanTheoMa(maPhongBan);
        if (phongBanHienTai.isPresent()) {
            System.out.println("Thông tin hiện tại: " + phongBanHienTai.get());
        }
        
        System.out.print("Nhập tên phòng ban mới: ");
        String tenPhongBan = scanner.nextLine().trim();
        
        System.out.print("Nhập mô tả mới (có thể để trống): ");
        String moTa = scanner.nextLine().trim();
        if (moTa.isEmpty()) {
            moTa = null;
        }
        
        boolean result = phongBanService.capNhatPhongBan(maPhongBan, tenPhongBan, moTa);
        
        if (result) {
            System.out.println("✓ Cập nhật phòng ban thành công!");
        } else {
            System.out.println("✗ Cập nhật phòng ban thất bại!");
        }
    }
    
    /**
     * Xem danh sách phòng ban
     */
    private void xemDanhSachPhongBan() {
        System.out.println("\n--- DANH SÁCH PHÒNG BAN ---");
        
        List<PhongBan> danhSach = phongBanService.xemDanhSachPhongBan();
        
        if (danhSach.isEmpty()) {
            System.out.println("Không có phòng ban nào trong hệ thống.");
        } else {
            System.out.printf("%-15s %-30s %-50s%n", "Mã PB", "Tên Phòng Ban", "Mô Tả");
            System.out.println("-".repeat(95));
            
            for (PhongBan pb : danhSach) {
                String moTa = pb.getMoTa() != null ? pb.getMoTa() : "";
                if (moTa.length() > 47) {
                    moTa = moTa.substring(0, 44) + "...";
                }
                System.out.printf("%-15s %-30s %-50s%n", 
                    pb.getMaPhongBan(), 
                    pb.getTenPhongBan(), 
                    moTa);
            }
            
            System.out.println("-".repeat(95));
            System.out.println("Tổng cộng: " + danhSach.size() + " phòng ban");
        }
    }
    
    /**
     * Tìm phòng ban theo mã
     */
    private void timPhongBanTheoMa() {
        System.out.println("\n--- TÌM PHÒNG BAN THEO MÃ ---");
        
        System.out.print("Nhập mã phòng ban: ");
        String maPhongBan = scanner.nextLine().trim();
        
        Optional<PhongBan> phongBan = phongBanService.timPhongBanTheoMa(maPhongBan);
        
        if (phongBan.isPresent()) {
            System.out.println("✓ Tìm thấy phòng ban:");
            System.out.println(phongBan.get());
        } else {
            System.out.println("✗ Không tìm thấy phòng ban với mã: " + maPhongBan);
        }
    }
    
    /**
     * Tìm kiếm phòng ban theo tên
     */
    private void timKiemPhongBanTheoTen() {
        System.out.println("\n--- TÌM KIẾM PHÒNG BAN THEO TÊN ---");
        
        System.out.print("Nhập tên phòng ban cần tìm: ");
        String tenPhongBan = scanner.nextLine().trim();
        
        List<PhongBan> ketQua = phongBanService.timKiemPhongBanTheoTen(tenPhongBan);
        
        if (ketQua.isEmpty()) {
            System.out.println("✗ Không tìm thấy phòng ban nào với tên: " + tenPhongBan);
        } else {
            System.out.println("✓ Tìm thấy " + ketQua.size() + " phòng ban:");
            for (PhongBan pb : ketQua) {
                System.out.println("- " + pb);
            }
        }
    }
    
    /**
     * Xóa phòng ban
     */
    private void xoaPhongBan() {
        System.out.println("\n--- XÓA PHÒNG BAN ---");
        
        System.out.print("Nhập mã phòng ban cần xóa: ");
        String maPhongBan = scanner.nextLine().trim();
        
        // Kiểm tra phòng ban có tồn tại không
        if (!phongBanService.kiemTraPhongBanTonTai(maPhongBan)) {
            System.out.println("✗ Phòng ban với mã '" + maPhongBan + "' không tồn tại!");
            return;
        }
        
        // Hiển thị thông tin phòng ban sẽ xóa
        Optional<PhongBan> phongBan = phongBanService.timPhongBanTheoMa(maPhongBan);
        if (phongBan.isPresent()) {
            System.out.println("Phòng ban sẽ bị xóa: " + phongBan.get());
        }
        
        System.out.print("Bạn có chắc chắn muốn xóa? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if ("y".equals(confirm) || "yes".equals(confirm)) {
            boolean result = phongBanService.xoaPhongBan(maPhongBan);
            
            if (result) {
                System.out.println("✓ Xóa phòng ban thành công!");
            } else {
                System.out.println("✗ Xóa phòng ban thất bại!");
            }
        } else {
            System.out.println("Đã hủy thao tác xóa.");
        }
    }
    
    /**
     * Thống kê phòng ban
     */
    private void thongKePhongBan() {
        System.out.println("\n--- THỐNG KÊ PHÒNG BAN ---");
        
        int tongSo = phongBanService.demSoLuongPhongBan();
        System.out.println("Tổng số phòng ban: " + tongSo);
        
        if (tongSo > 0) {
            List<PhongBan> danhSach = phongBanService.xemDanhSachPhongBan();
            System.out.println("\nDanh sách phòng ban:");
            for (int i = 0; i < danhSach.size(); i++) {
                System.out.println((i + 1) + ". " + danhSach.get(i).getTenPhongBan() + 
                                 " (" + danhSach.get(i).getMaPhongBan() + ")");
            }
        }
    }
    
    /**
     * Đóng scanner
     */
    public void dong() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
