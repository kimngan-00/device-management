package com.mycompany.device.service.impl;

import com.mycompany.device.dao.PhongBanDAO;
import com.mycompany.device.dao.impl.PhongBanDAOMySQLImpl;
import com.mycompany.device.model.PhongBan;
import com.mycompany.device.observer.PhongBanObserver;
import com.mycompany.device.observer.PhongBanSubject;
import com.mycompany.device.service.PhongBanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Implementation của PhongBanService với Observer Pattern
 * @author Kim Ngan - Service Implementation Layer
 */
public class PhongBanServiceImpl implements PhongBanService {
    
    private static final Logger logger = LoggerFactory.getLogger(PhongBanServiceImpl.class);
    
    private final PhongBanDAO phongBanDAO;
    private final PhongBanSubject subject;
    
    public PhongBanServiceImpl() {
        // Sử dụng MySQL implementation thay vì in-memory
        this.phongBanDAO = new PhongBanDAOMySQLImpl();
        this.subject = new PhongBanSubject();
        
        // Thêm logger observer mặc định
        this.subject.addObserver(new com.mycompany.device.observer.PhongBanLogger());
        
        logger.info("Khởi tạo PhongBanServiceImpl với MySQL database và Observer pattern");
    }
    
    public PhongBanServiceImpl(PhongBanDAO phongBanDAO) {
        this.phongBanDAO = phongBanDAO;
        this.subject = new PhongBanSubject();
        
        // Thêm logger observer mặc định
        this.subject.addObserver(new com.mycompany.device.observer.PhongBanLogger());
        
        logger.info("Khởi tạo PhongBanServiceImpl với DAO được inject và Observer pattern");
    }
    
    /**
     * Thêm observer
     */
    public void addObserver(PhongBanObserver observer) {
        subject.addObserver(observer);
        logger.info("Đã thêm observer: {}", observer.getClass().getSimpleName());
    }
    
    /**
     * Xóa observer
     */
    public void removeObserver(PhongBanObserver observer) {
        subject.removeObserver(observer);
        logger.info("Đã xóa observer: {}", observer.getClass().getSimpleName());
    }
    
    @Override
    public boolean taoPhongBan(String maPhongBan, String tenPhongBan, String moTa) {
        logger.info("Bắt đầu tạo phòng ban: maPhongBan={}, tenPhongBan={}", maPhongBan, tenPhongBan);
        
        // Validate input
        if (!validateInput(maPhongBan, tenPhongBan)) {
            logger.warn("Thông tin phòng ban không hợp lệ");
            return false;
        }
        
        // Kiểm tra phòng ban đã tồn tại chưa
        if (phongBanDAO.existsPhongBan(maPhongBan.trim())) {
            logger.warn("Phòng ban với mã '{}' đã tồn tại", maPhongBan);
            return false;
        }
        
        // Tạo object PhongBan
        PhongBan phongBan = new PhongBan(maPhongBan.trim(), tenPhongBan.trim(), moTa);
        
        // Validate object
        if (!validatePhongBan(phongBan)) {
            logger.warn("Object phòng ban không hợp lệ");
            return false;
        }
        
        // Gọi DAO để tạo
        boolean result = phongBanDAO.createPhongBan(phongBan);
        
        if (result) {
            logger.info("Tạo phòng ban thành công: {}", maPhongBan);
            // Thông báo cho observers
            subject.notifyPhongBanAdded(phongBan);
        } else {
            logger.error("Tạo phòng ban thất bại: {}", maPhongBan);
        }
        
        return result;
    }
    
    @Override
    public boolean taoPhongBan(PhongBan phongBan) {
        if (phongBan == null) {
            logger.warn("Không thể tạo phòng ban: object null");
            return false;
        }
        
        return taoPhongBan(phongBan.getMaPhongBan(), phongBan.getTenPhongBan(), phongBan.getMoTa());
    }
    
    @Override
    public boolean capNhatPhongBan(String maPhongBan, String tenPhongBan, String moTa) {
        logger.info("Bắt đầu cập nhật phòng ban: maPhongBan={}, tenPhongBan={}", maPhongBan, tenPhongBan);
        
        // Validate input
        if (!validateInput(maPhongBan, tenPhongBan)) {
            logger.warn("Thông tin phòng ban không hợp lệ");
            return false;
        }
        
        // Kiểm tra phòng ban có tồn tại không
        if (!phongBanDAO.existsPhongBan(maPhongBan.trim())) {
            logger.warn("Phòng ban với mã '{}' không tồn tại", maPhongBan);
            return false;
        }
        
        // Lấy thông tin cũ để so sánh
        Optional<PhongBan> oldPhongBanOpt = phongBanDAO.findPhongBanByMa(maPhongBan.trim());
        if (!oldPhongBanOpt.isPresent()) {
            logger.warn("Không thể lấy thông tin phòng ban cũ: {}", maPhongBan);
            return false;
        }
        PhongBan oldPhongBan = oldPhongBanOpt.get();
        
        // Tạo object PhongBan mới
        PhongBan phongBan = new PhongBan(maPhongBan.trim(), tenPhongBan.trim(), moTa);
        
        // Validate object
        if (!validatePhongBan(phongBan)) {
            logger.warn("Object phòng ban không hợp lệ");
            return false;
        }
        
        // Gọi DAO để cập nhật
        boolean result = phongBanDAO.updatePhongBan(phongBan);
        
        if (result) {
            logger.info("Cập nhật phòng ban thành công: {}", maPhongBan);
            // Thông báo cho observers
            subject.notifyPhongBanUpdated(phongBan, oldPhongBan);
        } else {
            logger.error("Cập nhật phòng ban thất bại: {}", maPhongBan);
        }
        
        return result;
    }
    
    @Override
    public boolean capNhatPhongBan(PhongBan phongBan) {
        if (phongBan == null) {
            logger.warn("Không thể cập nhật phòng ban: object null");
            return false;
        }
        
        return capNhatPhongBan(phongBan.getMaPhongBan(), phongBan.getTenPhongBan(), phongBan.getMoTa());
    }
    
    @Override
    public List<PhongBan> xemDanhSachPhongBan() {
        logger.info("Lấy danh sách tất cả phòng ban");
        
        List<PhongBan> result = phongBanDAO.getAllPhongBan();
        
        logger.info("Lấy danh sách phòng ban thành công: {} phòng ban", result.size());
        return result;
    }
    
    @Override
    public Optional<PhongBan> timPhongBanTheoMa(String maPhongBan) {
        if (maPhongBan == null || maPhongBan.trim().isEmpty()) {
            logger.warn("Không thể tìm phòng ban: mã phòng ban không hợp lệ");
            return Optional.empty();
        }
        
        logger.info("Tìm phòng ban theo mã: {}", maPhongBan);
        
        Optional<PhongBan> result = phongBanDAO.findPhongBanByMa(maPhongBan.trim());
        
        if (result.isPresent()) {
            logger.info("Tìm thấy phòng ban: {}", maPhongBan);
        } else {
            logger.info("Không tìm thấy phòng ban: {}", maPhongBan);
        }
        
        return result;
    }
    
    @Override
    public List<PhongBan> timKiemPhongBanTheoTen(String tenPhongBan) {
        if (tenPhongBan == null || tenPhongBan.trim().isEmpty()) {
            logger.warn("Không thể tìm kiếm phòng ban: tên phòng ban không hợp lệ");
            return List.of();
        }
        
        logger.info("Tìm kiếm phòng ban theo tên: {}", tenPhongBan);
        
        List<PhongBan> result = phongBanDAO.searchPhongBanByTen(tenPhongBan.trim());
        
        logger.info("Tìm kiếm phòng ban thành công: {} kết quả", result.size());
        return result;
    }
    
    @Override
    public boolean xoaPhongBan(String maPhongBan) {
        if (maPhongBan == null || maPhongBan.trim().isEmpty()) {
            logger.warn("Không thể xóa phòng ban: mã phòng ban không hợp lệ");
            return false;
        }
        
        logger.info("Bắt đầu xóa phòng ban: {}", maPhongBan);
        
        // Kiểm tra phòng ban có tồn tại không
        if (!phongBanDAO.existsPhongBan(maPhongBan.trim())) {
            logger.warn("Phòng ban với mã '{}' không tồn tại", maPhongBan);
            return false;
        }
        
        // Gọi DAO để xóa
        boolean result = phongBanDAO.deletePhongBan(maPhongBan.trim());
        
        if (result) {
            logger.info("Xóa phòng ban thành công: {}", maPhongBan);
            // Thông báo cho observers
            subject.notifyPhongBanDeleted(maPhongBan.trim());
        } else {
            logger.error("Xóa phòng ban thất bại: {}", maPhongBan);
        }
        
        return result;
    }
    
    @Override
    public boolean kiemTraPhongBanTonTai(String maPhongBan) {
        if (maPhongBan == null || maPhongBan.trim().isEmpty()) {
            return false;
        }
        
        return phongBanDAO.existsPhongBan(maPhongBan.trim());
    }
    
    @Override
    public int demSoLuongPhongBan() {
        int count = phongBanDAO.countPhongBan();
        logger.info("Đếm số lượng phòng ban: {}", count);
        return count;
    }
    
    @Override
    public boolean validatePhongBan(PhongBan phongBan) {
        if (phongBan == null) {
            return false;
        }
        
        return validateInput(phongBan.getMaPhongBan(), phongBan.getTenPhongBan());
    }
    
    /**
     * Validate input cơ bản
     */
    private boolean validateInput(String maPhongBan, String tenPhongBan) {
        if (maPhongBan == null || maPhongBan.trim().isEmpty()) {
            logger.warn("Mã phòng ban không được để trống");
            return false;
        }
        
        if (tenPhongBan == null || tenPhongBan.trim().isEmpty()) {
            logger.warn("Tên phòng ban không được để trống");
            return false;
        }
        
        if (maPhongBan.trim().length() > 20) {
            logger.warn("Mã phòng ban không được vượt quá 20 ký tự");
            return false;
        }
        
        if (tenPhongBan.trim().length() > 255) {
            logger.warn("Tên phòng ban không được vượt quá 255 ký tự");
            return false;
        }
        
        return true;
    }
}
