package com.mycompany.device.observer;

import com.mycompany.device.model.PhongBan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger Observer cho PhongBan
 * @author Kim Ngan - Observer Pattern Implementation
 */
public class PhongBanLogger implements PhongBanObserver {
    
    private static final Logger logger = LoggerFactory.getLogger(PhongBanLogger.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public void onPhongBanAdded(PhongBan phongBan) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("[{}] PHONGBAN_ADDED: Mã={}, Tên={}, Mô tả={}", 
                   timestamp, phongBan.getMaPhongBan(), phongBan.getTenPhongBan(), 
                   phongBan.getMoTa() != null ? phongBan.getMoTa() : "Không có mô tả");
    }
    
    @Override
    public void onPhongBanDeleted(String maPhongBan) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.warn("[{}] PHONGBAN_DELETED: Mã={}", timestamp, maPhongBan);
    }
    
    @Override
    public void onPhongBanUpdated(PhongBan phongBan, PhongBan oldPhongBan) {
        String timestamp = LocalDateTime.now().format(formatter);
        
        StringBuilder changes = new StringBuilder();
        
        if (!oldPhongBan.getTenPhongBan().equals(phongBan.getTenPhongBan())) {
            changes.append(String.format("Tên: '%s' -> '%s'", oldPhongBan.getTenPhongBan(), phongBan.getTenPhongBan()));
        }
        
        if (!java.util.Objects.equals(oldPhongBan.getMoTa(), phongBan.getMoTa())) {
            if (changes.length() > 0) changes.append(", ");
            changes.append(String.format("Mô tả: '%s' -> '%s'", 
                oldPhongBan.getMoTa() != null ? oldPhongBan.getMoTa() : "null",
                phongBan.getMoTa() != null ? phongBan.getMoTa() : "null"));
        }
        
        if (changes.length() > 0) {
            logger.info("[{}] PHONGBAN_UPDATED: Mã={}, Thay đổi: {}", 
                       timestamp, phongBan.getMaPhongBan(), changes.toString());
        } else {
            logger.info("[{}] PHONGBAN_UPDATED: Mã={}, Không có thay đổi", 
                       timestamp, phongBan.getMaPhongBan());
        }
    }
}
