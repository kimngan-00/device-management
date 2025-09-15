package com.mycompany.device.observer;

import com.mycompany.device.model.NhanVien;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger Observer cho NhanVien
 * Ghi log tất cả các hoạt động liên quan đến nhân viên
 * @author Kim Ngan - Observer Pattern Implementation
 */
public class NhanVienLogger implements NhanVienObserver {
    
    private static final Logger logger = LoggerFactory.getLogger(NhanVienLogger.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public void onNhanVienAdded(NhanVien nhanVien) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("[{}] NHANVIEN_ADDED: Mã={}, Tên={}, Email={}, Vai trò={}, Phòng ban={}", 
                   timestamp, nhanVien.getMaNhanVien(), nhanVien.getTenNhanVien(), 
                   nhanVien.getEmail(), nhanVien.getRole().getDisplayName(), nhanVien.getMaPhongBan());
    }
    
    @Override
    public void onNhanVienDeleted(String maNhanVien) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.warn("[{}] NHANVIEN_DELETED: Mã={}", timestamp, maNhanVien);
    }
    
    @Override
    public void onNhanVienUpdated(NhanVien nhanVien, NhanVien oldNhanVien) {
        String timestamp = LocalDateTime.now().format(formatter);
        
        // Log các thay đổi chi tiết
        StringBuilder changes = new StringBuilder();
        
        if (!oldNhanVien.getTenNhanVien().equals(nhanVien.getTenNhanVien())) {
            changes.append(String.format("Tên: '%s' -> '%s'", oldNhanVien.getTenNhanVien(), nhanVien.getTenNhanVien()));
        }
        
        if (!oldNhanVien.getEmail().equals(nhanVien.getEmail())) {
            if (changes.length() > 0) changes.append(", ");
            changes.append(String.format("Email: '%s' -> '%s'", oldNhanVien.getEmail(), nhanVien.getEmail()));
        }
        
        if (!oldNhanVien.getRole().equals(nhanVien.getRole())) {
            if (changes.length() > 0) changes.append(", ");
            changes.append(String.format("Vai trò: '%s' -> '%s'", oldNhanVien.getRole().getDisplayName(), nhanVien.getRole().getDisplayName()));
        }
        
        if (!oldNhanVien.getMaPhongBan().equals(nhanVien.getMaPhongBan())) {
            if (changes.length() > 0) changes.append(", ");
            changes.append(String.format("Phòng ban: '%s' -> '%s'", oldNhanVien.getMaPhongBan(), nhanVien.getMaPhongBan()));
        }
        
        if (changes.length() > 0) {
            logger.info("[{}] NHANVIEN_UPDATED: Mã={}, Thay đổi: {}", 
                       timestamp, nhanVien.getMaNhanVien(), changes.toString());
        } else {
            logger.info("[{}] NHANVIEN_UPDATED: Mã={}, Không có thay đổi", 
                       timestamp, nhanVien.getMaNhanVien());
        }
    }
    
    @Override
    public void onNhanVienLoggedIn(NhanVien nhanVien) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("[{}] NHANVIEN_LOGIN: Mã={}, Tên={}, Email={}, Vai trò={}", 
                   timestamp, nhanVien.getMaNhanVien(), nhanVien.getTenNhanVien(), 
                   nhanVien.getEmail(), nhanVien.getRole().getDisplayName());
    }
    
    @Override
    public void onNhanVienLoggedOut(NhanVien nhanVien) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("[{}] NHANVIEN_LOGOUT: Mã={}, Tên={}, Email={}", 
                   timestamp, nhanVien.getMaNhanVien(), nhanVien.getTenNhanVien(), nhanVien.getEmail());
    }
}
