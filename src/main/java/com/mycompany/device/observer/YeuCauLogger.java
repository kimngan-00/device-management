package com.mycompany.device.observer;

import com.mycompany.device.model.YeuCau;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger Observer cho YeuCau
 * @author Kim Ngan - Observer Pattern Implementation
 */
public class YeuCauLogger implements YeuCauObserver {
    
    private static final Logger logger = LoggerFactory.getLogger(YeuCauLogger.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public void onYeuCauAdded(YeuCau yeuCau) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("[{}] YEUCAU_ADDED: ID={}, Thiết bị={}, Nhân viên={}, Trạng thái={}, Lý do='{}'", 
                   timestamp, yeuCau.getId(), yeuCau.getThietBiId(), yeuCau.getNhanVienId(), 
                   yeuCau.getTrangThai().getDisplayName(), yeuCau.getLyDo());
    }
    
    @Override
    public void onYeuCauDeleted(Long yeuCauId) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.warn("[{}] YEUCAU_DELETED: ID={}", timestamp, yeuCauId);
    }
    
    @Override
    public void onYeuCauUpdated(YeuCau yeuCau, YeuCau oldYeuCau) {
        String timestamp = LocalDateTime.now().format(formatter);
        
        StringBuilder changes = new StringBuilder();
        
        if (!oldYeuCau.getTrangThai().equals(yeuCau.getTrangThai())) {
            changes.append(String.format("Trạng thái: '%s' -> '%s'", 
                oldYeuCau.getTrangThai().getDisplayName(), yeuCau.getTrangThai().getDisplayName()));
        }
        
        if (!oldYeuCau.getLyDo().equals(yeuCau.getLyDo())) {
            if (changes.length() > 0) changes.append(", ");
            changes.append(String.format("Lý do: '%s' -> '%s'", oldYeuCau.getLyDo(), yeuCau.getLyDo()));
        }
        
        if (!oldYeuCau.getThietBiId().equals(yeuCau.getThietBiId())) {
            if (changes.length() > 0) changes.append(", ");
            changes.append(String.format("Thiết bị: %d -> %d", oldYeuCau.getThietBiId(), yeuCau.getThietBiId()));
        }
        
        if (!oldYeuCau.getNhanVienId().equals(yeuCau.getNhanVienId())) {
            if (changes.length() > 0) changes.append(", ");
            changes.append(String.format("Nhân viên: %d -> %d", oldYeuCau.getNhanVienId(), yeuCau.getNhanVienId()));
        }
        
        if (changes.length() > 0) {
            logger.info("[{}] YEUCAU_UPDATED: ID={}, Thay đổi: {}", 
                       timestamp, yeuCau.getId(), changes.toString());
        } else {
            logger.info("[{}] YEUCAU_UPDATED: ID={}, Không có thay đổi", 
                       timestamp, yeuCau.getId());
        }
    }
    
    @Override
    public void onYeuCauStatusChanged(YeuCau yeuCau, YeuCau.TrangThaiYeuCau oldStatus, YeuCau.TrangThaiYeuCau newStatus) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("[{}] YEUCAU_STATUS_CHANGED: ID={}, Trạng thái: '{}' -> '{}'", 
                   timestamp, yeuCau.getId(), oldStatus.getDisplayName(), newStatus.getDisplayName());
    }
    
    @Override
    public void onYeuCauApproved(YeuCau yeuCau) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("[{}] YEUCAU_APPROVED: ID={}, Thiết bị={}, Nhân viên={}", 
                   timestamp, yeuCau.getId(), yeuCau.getThietBiId(), yeuCau.getNhanVienId());
    }
    
    @Override
    public void onYeuCauRejected(YeuCau yeuCau) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.warn("[{}] YEUCAU_REJECTED: ID={}, Thiết bị={}, Nhân viên={}, Lý do='{}'", 
                   timestamp, yeuCau.getId(), yeuCau.getThietBiId(), yeuCau.getNhanVienId(), yeuCau.getLyDo());
    }
    
    @Override
    public void onYeuCauAllocated(YeuCau yeuCau) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("[{}] YEUCAU_ALLOCATED: ID={}, Thiết bị={}, Nhân viên={}", 
                   timestamp, yeuCau.getId(), yeuCau.getThietBiId(), yeuCau.getNhanVienId());
    }
    
    @Override
    public void onYeuCauCancelled(YeuCau yeuCau) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.warn("[{}] YEUCAU_CANCELLED: ID={}, Thiết bị={}, Nhân viên={}, Lý do='{}'", 
                   timestamp, yeuCau.getId(), yeuCau.getThietBiId(), yeuCau.getNhanVienId(), yeuCau.getLyDo());
    }
} 