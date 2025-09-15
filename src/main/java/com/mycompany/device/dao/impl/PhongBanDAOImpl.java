package com.mycompany.device.dao.impl;

import com.mycompany.device.dao.PhongBanDAO;
import com.mycompany.device.model.PhongBan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Implementation của PhongBanDAO sử dụng in-memory storage
 * @author Kim Ngan - DAO Implementation Layer
 */
public class PhongBanDAOImpl implements PhongBanDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(PhongBanDAOImpl.class);
    
    // Sử dụng ConcurrentHashMap để thread-safe
    private final Map<String, PhongBan> phongBanStorage;
    
    public PhongBanDAOImpl() {
        this.phongBanStorage = new ConcurrentHashMap<>();
        logger.info("Khởi tạo PhongBanDAOImpl với in-memory storage");
    }
    
    @Override
    public boolean createPhongBan(PhongBan phongBan) {
        if (phongBan == null || phongBan.getMaPhongBan() == null || phongBan.getMaPhongBan().trim().isEmpty()) {
            logger.warn("Không thể tạo phòng ban: thông tin không hợp lệ");
            return false;
        }
        
        String maPhongBan = phongBan.getMaPhongBan().trim();
        
        if (phongBanStorage.containsKey(maPhongBan)) {
            logger.warn("Không thể tạo phòng ban: mã phòng ban '{}' đã tồn tại", maPhongBan);
            return false;
        }
        
        try {
            phongBanStorage.put(maPhongBan, phongBan);
            logger.info("Tạo phòng ban thành công: {}", maPhongBan);
            return true;
        } catch (Exception e) {
            logger.error("Lỗi khi tạo phòng ban: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean updatePhongBan(PhongBan phongBan) {
        if (phongBan == null || phongBan.getMaPhongBan() == null || phongBan.getMaPhongBan().trim().isEmpty()) {
            logger.warn("Không thể cập nhật phòng ban: thông tin không hợp lệ");
            return false;
        }
        
        String maPhongBan = phongBan.getMaPhongBan().trim();
        
        if (!phongBanStorage.containsKey(maPhongBan)) {
            logger.warn("Không thể cập nhật phòng ban: mã phòng ban '{}' không tồn tại", maPhongBan);
            return false;
        }
        
        try {
            phongBanStorage.put(maPhongBan, phongBan);
            logger.info("Cập nhật phòng ban thành công: {}", maPhongBan);
            return true;
        } catch (Exception e) {
            logger.error("Lỗi khi cập nhật phòng ban: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean deletePhongBan(String maPhongBan) {
        if (maPhongBan == null || maPhongBan.trim().isEmpty()) {
            logger.warn("Không thể xóa phòng ban: mã phòng ban không hợp lệ");
            return false;
        }
        
        String ma = maPhongBan.trim();
        
        if (!phongBanStorage.containsKey(ma)) {
            logger.warn("Không thể xóa phòng ban: mã phòng ban '{}' không tồn tại", ma);
            return false;
        }
        
        try {
            phongBanStorage.remove(ma);
            logger.info("Xóa phòng ban thành công: {}", ma);
            return true;
        } catch (Exception e) {
            logger.error("Lỗi khi xóa phòng ban: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public Optional<PhongBan> findPhongBanByMa(String maPhongBan) {
        if (maPhongBan == null || maPhongBan.trim().isEmpty()) {
            logger.warn("Không thể tìm phòng ban: mã phòng ban không hợp lệ");
            return Optional.empty();
        }
        
        String ma = maPhongBan.trim();
        PhongBan phongBan = phongBanStorage.get(ma);
        
        if (phongBan != null) {
            logger.debug("Tìm thấy phòng ban: {}", ma);
        } else {
            logger.debug("Không tìm thấy phòng ban: {}", ma);
        }
        
        return Optional.ofNullable(phongBan);
    }
    
    @Override
    public List<PhongBan> getAllPhongBan() {
        List<PhongBan> result = new ArrayList<>(phongBanStorage.values());
        logger.debug("Lấy danh sách tất cả phòng ban: {} phòng ban", result.size());
        return result;
    }
    
    @Override
    public List<PhongBan> searchPhongBanByTen(String tenPhongBan) {
        if (tenPhongBan == null || tenPhongBan.trim().isEmpty()) {
            logger.warn("Không thể tìm kiếm phòng ban: tên phòng ban không hợp lệ");
            return new ArrayList<>();
        }
        
        String ten = tenPhongBan.trim().toLowerCase();
        
        List<PhongBan> result = phongBanStorage.values().stream()
                .filter(pb -> pb.getTenPhongBan() != null && 
                             pb.getTenPhongBan().toLowerCase().contains(ten))
                .collect(Collectors.toList());
        
        logger.debug("Tìm kiếm phòng ban theo tên '{}': {} kết quả", tenPhongBan, result.size());
        return result;
    }
    
    @Override
    public boolean existsPhongBan(String maPhongBan) {
        if (maPhongBan == null || maPhongBan.trim().isEmpty()) {
            return false;
        }
        
        boolean exists = phongBanStorage.containsKey(maPhongBan.trim());
        logger.debug("Kiểm tra tồn tại phòng ban '{}': {}", maPhongBan, exists);
        return exists;
    }
    
    @Override
    public int countPhongBan() {
        int count = phongBanStorage.size();
        logger.debug("Đếm số lượng phòng ban: {}", count);
        return count;
    }
}
