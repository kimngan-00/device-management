package com.mycompany.device.service.impl;

import com.mycompany.device.dao.YeuCauDAO;
import com.mycompany.device.dao.impl.YeuCauDAOMySQLImpl;
import com.mycompany.device.model.YeuCau;
import com.mycompany.device.observer.YeuCauObserver;
import com.mycompany.device.observer.YeuCauSubject;
import com.mycompany.device.service.YeuCauService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Implementation của YeuCauService với Observer Pattern
 * @author Kim Ngan - Service Implementation Layer
 */
public class YeuCauServiceImpl implements YeuCauService {
    
    private static final Logger logger = LoggerFactory.getLogger(YeuCauServiceImpl.class);
    
    private final YeuCauDAO yeuCauDAO;
    private final YeuCauSubject subject;
    
    public YeuCauServiceImpl() {
        this.yeuCauDAO = new YeuCauDAOMySQLImpl();
        this.subject = new YeuCauSubject();
        
        // Thêm logger observer mặc định
        this.subject.addObserver(new com.mycompany.device.observer.YeuCauLogger());
        
        logger.info("Khởi tạo YeuCauServiceImpl với MySQL database và Observer pattern");
    }
    
    public YeuCauServiceImpl(YeuCauDAO yeuCauDAO) {
        this.yeuCauDAO = yeuCauDAO;
        this.subject = new YeuCauSubject();
        
        // Thêm logger observer mặc định
        this.subject.addObserver(new com.mycompany.device.observer.YeuCauLogger());
        
        logger.info("Khởi tạo YeuCauServiceImpl với DAO được inject và Observer pattern");
    }
    
    /**
     * Thêm observer
     */
    public void addObserver(YeuCauObserver observer) {
        subject.addObserver(observer);
        logger.info("Đã thêm observer: {}", observer.getClass().getSimpleName());
    }
    
    /**
     * Xóa observer
     */
    public void removeObserver(YeuCauObserver observer) {
        subject.removeObserver(observer);
        logger.info("Đã xóa observer: {}", observer.getClass().getSimpleName());
    }
    
    @Override
    public boolean taoYeuCau(Long thietBiId, String nhanVienId, String lyDo) {
        logger.info("Bắt đầu tạo yêu cầu: thietBiId={}, nhanVienId={}", thietBiId, nhanVienId);
        
        // Validate input
        if (!validateYeuCauInput(thietBiId, nhanVienId)) {
            logger.warn("Thông tin yêu cầu không hợp lệ");
            return false;
        }
        
        // Tạo object YeuCau
        YeuCau yeuCau = new YeuCau(thietBiId, nhanVienId, lyDo);
        
        // Validate object
        if (!validateYeuCau(yeuCau)) {
            logger.warn("Object yêu cầu không hợp lệ");
            return false;
        }
        System.out.println("nhanVienId: " + yeuCau);
        // Gọi DAO để tạo
        boolean result = yeuCauDAO.createYeuCau(yeuCau);
        
        if (result) {
            logger.info("Tạo yêu cầu thành công: ID={}", yeuCau.getId());
            // Thông báo cho observers
            subject.notifyYeuCauAdded(yeuCau);
        } else {
            logger.error("Tạo yêu cầu thất bại");
        }
        
        return result;
    }
    
    @Override
    public boolean taoYeuCau(YeuCau yeuCau) {
        if (yeuCau == null) {
            logger.warn("Không thể tạo yêu cầu: object null");
            return false;
        }
        System.out.println("yeuCau line 101: " + yeuCau);
        return taoYeuCau(yeuCau.getThietBiId(), yeuCau.getNhanVienId(), yeuCau.getLyDo());
    }
    
    @Override
    public boolean capNhatYeuCau(Long yeuCauId, Long thietBiId, String nhanVienId, String lyDo) {
        logger.info("Bắt đầu cập nhật yêu cầu: ID={}", yeuCauId);
        
        // Validate input
        if (!validateYeuCauInput(thietBiId, nhanVienId)) {
            logger.warn("Thông tin yêu cầu không hợp lệ");
            return false;
        }
        
        // Kiểm tra yêu cầu có tồn tại không
        if (!yeuCauDAO.existsYeuCau(yeuCauId)) {
            logger.warn("Yêu cầu với ID '{}' không tồn tại", yeuCauId);
            return false;
        }
        
        // Lấy thông tin cũ để so sánh
        Optional<YeuCau> oldYeuCauOpt = yeuCauDAO.findYeuCauById(yeuCauId);
        if (!oldYeuCauOpt.isPresent()) {
            logger.warn("Không thể lấy thông tin yêu cầu cũ: ID={}", yeuCauId);
            return false;
        }
        YeuCau oldYeuCau = oldYeuCauOpt.get();
        
        // Tạo object YeuCau mới
        YeuCau yeuCau = new YeuCau(yeuCauId, thietBiId, nhanVienId, oldYeuCau.getTrangThai(), lyDo, 
                                  oldYeuCau.getNgayTao(), oldYeuCau.getNgayCapNhat());
        
        // Validate object
        if (!validateYeuCau(yeuCau)) {
            logger.warn("Object yêu cầu không hợp lệ");
            return false;
        }
        
        // Gọi DAO để cập nhật
        boolean result = yeuCauDAO.updateYeuCau(yeuCau);
        
        if (result) {
            logger.info("Cập nhật yêu cầu thành công: ID={}", yeuCauId);
            // Thông báo cho observers
            subject.notifyYeuCauUpdated(yeuCau, oldYeuCau);
        } else {
            logger.error("Cập nhật yêu cầu thất bại: ID={}", yeuCauId);
        }
        
        return result;
    }
    
    @Override
    public boolean capNhatYeuCau(YeuCau yeuCau) {
        if (yeuCau == null) {
            logger.warn("Không thể cập nhật yêu cầu: object null");
            return false;
        }
        
        return capNhatYeuCau(yeuCau.getId(), yeuCau.getThietBiId(), yeuCau.getNhanVienId(), yeuCau.getLyDo());
    }
    
    @Override
    public boolean capNhatTrangThai(Long yeuCauId, YeuCau.TrangThaiYeuCau trangThai) {
        logger.info("Cập nhật trạng thái yêu cầu: ID={}, Trạng thái={}", yeuCauId, trangThai);
        
        Optional<YeuCau> yeuCauOpt = yeuCauDAO.findYeuCauById(yeuCauId);
        if (!yeuCauOpt.isPresent()) {
            logger.warn("Không tìm thấy yêu cầu: ID={}", yeuCauId);
            return false;
        }
        
        YeuCau yeuCau = yeuCauOpt.get();
        YeuCau.TrangThaiYeuCau oldStatus = yeuCau.getTrangThai();
        
        // Cập nhật trạng thái
        yeuCau.updateTrangThai(trangThai);
        
        boolean result = yeuCauDAO.updateYeuCau(yeuCau);
        
        if (result) {
            logger.info("Cập nhật trạng thái thành công: ID={}, {} -> {}", 
                       yeuCauId, oldStatus.getDisplayName(), trangThai.getDisplayName());
            
            // Thông báo cho observers
            subject.notifyYeuCauStatusChanged(yeuCau, oldStatus, trangThai);
            
            // Thông báo specific events
            switch (trangThai) {
                case DA_DUYET:
                    subject.notifyYeuCauApproved(yeuCau);
                    break;
                case TU_CHOI:
                    subject.notifyYeuCauRejected(yeuCau);
                    break;
                case DA_CAP_PHAT:
                    subject.notifyYeuCauAllocated(yeuCau);
                    break;
                case DA_HUY:
                    subject.notifyYeuCauCancelled(yeuCau);
                    break;
            }
        }
        
        return result;
    }
    
    @Override
    public List<YeuCau> xemDanhSachYeuCau() {
        logger.info("Lấy danh sách tất cả yêu cầu");
        
        List<YeuCau> result = yeuCauDAO.getAllYeuCau();
        
        logger.info("Lấy danh sách yêu cầu thành công: {} yêu cầu", result.size());
        return result;
    }
    
    @Override
    public Optional<YeuCau> timYeuCauTheoId(Long yeuCauId) {
        if (yeuCauId == null) {
            logger.warn("Không thể tìm yêu cầu: ID không hợp lệ");
            return Optional.empty();
        }
        
        logger.info("Tìm yêu cầu theo ID: {}", yeuCauId);
        
        Optional<YeuCau> result = yeuCauDAO.findYeuCauById(yeuCauId);
        
        if (result.isPresent()) {
            logger.info("Tìm thấy yêu cầu: ID={}", yeuCauId);
        } else {
            logger.info("Không tìm thấy yêu cầu: ID={}", yeuCauId);
        }
        
        return result;
    }
    
    @Override
    public List<YeuCau> timYeuCauTheoThietBi(Long thietBiId) {
        if (thietBiId == null) {
            logger.warn("Không thể tìm yêu cầu: ID thiết bị không hợp lệ");
            return List.of();
        }
        
        logger.info("Tìm yêu cầu theo thiết bị: {}", thietBiId);
        
        List<YeuCau> result = yeuCauDAO.findYeuCauByThietBi(thietBiId);
        
        logger.info("Tìm yêu cầu theo thiết bị thành công: {} kết quả", result.size());
        return result;
    }
    
    @Override
    public List<YeuCau> timYeuCauTheoNhanVien(String nhanVienId) {
        if (nhanVienId == null) {
            logger.warn("Không thể tìm yêu cầu: ID nhân viên không hợp lệ");
            return List.of();
        }
        
        logger.info("Tìm yêu cầu theo nhân viên: {}", nhanVienId);
        
        List<YeuCau> result = yeuCauDAO.findYeuCauByNhanVien(nhanVienId);
        
        logger.info("Tìm yêu cầu theo nhân viên thành công: {} kết quả", result.size());
        return result;
    }
    
    @Override
    public List<YeuCau> timYeuCauTheoTrangThai(YeuCau.TrangThaiYeuCau trangThai) {
        if (trangThai == null) {
            logger.warn("Không thể tìm yêu cầu: trạng thái không hợp lệ");
            return List.of();
        }
        
        logger.info("Tìm yêu cầu theo trạng thái: {}", trangThai);
        
        List<YeuCau> result = yeuCauDAO.findYeuCauByTrangThai(trangThai);
        
        logger.info("Tìm yêu cầu theo trạng thái thành công: {} kết quả", result.size());
        return result;
    }
    
    @Override
    public List<YeuCau> timKiemYeuCauTheoLyDo(String lyDo) {
        if (lyDo == null || lyDo.trim().isEmpty()) {
            logger.warn("Không thể tìm kiếm yêu cầu: lý do không hợp lệ");
            return List.of();
        }
        
        logger.info("Tìm kiếm yêu cầu theo lý do: {}", lyDo);
        
        List<YeuCau> result = yeuCauDAO.searchYeuCauByLyDo(lyDo.trim());
        
        logger.info("Tìm kiếm yêu cầu thành công: {} kết quả", result.size());
        return result;
    }
    
    @Override
    public boolean xoaYeuCau(Long yeuCauId) {
        if (yeuCauId == null) {
            logger.warn("Không thể xóa yêu cầu: ID không hợp lệ");
            return false;
        }
        
        logger.info("Bắt đầu xóa yêu cầu: ID={}", yeuCauId);
        
        // Kiểm tra yêu cầu có tồn tại không
        if (!yeuCauDAO.existsYeuCau(yeuCauId)) {
            logger.warn("Yêu cầu với ID '{}' không tồn tại", yeuCauId);
            return false;
        }
        
        // Gọi DAO để xóa
        boolean result = yeuCauDAO.deleteYeuCau(yeuCauId);
        
        if (result) {
            logger.info("Xóa yêu cầu thành công: ID={}", yeuCauId);
            // Thông báo cho observers
            subject.notifyYeuCauDeleted(yeuCauId);
        } else {
            logger.error("Xóa yêu cầu thất bại: ID={}", yeuCauId);
        }
        
        return result;
    }
    
    @Override
    public boolean kiemTraYeuCauTonTai(Long yeuCauId) {
        if (yeuCauId == null) {
            return false;
        }
        
        return yeuCauDAO.existsYeuCau(yeuCauId);
    }
    
    @Override
    public int demSoLuongYeuCau() {
        int count = yeuCauDAO.countYeuCau();
        logger.info("Đếm số lượng yêu cầu: {}", count);
        return count;
    }
    
    @Override
    public int demSoLuongYeuCauTheoTrangThai(YeuCau.TrangThaiYeuCau trangThai) {
        if (trangThai == null) {
            return 0;
        }
        
        int count = yeuCauDAO.countYeuCauByTrangThai(trangThai);
        logger.info("Đếm số lượng yêu cầu theo trạng thái '{}': {}", trangThai, count);
        return count;
    }
    
    @Override
    public int demSoLuongYeuCauCuaNhanVien(String nhanVienId) {
        if (nhanVienId == null) {
            return 0;
        }
        
        int count = yeuCauDAO.countYeuCauByNhanVien(nhanVienId);
        logger.info("Đếm số lượng yêu cầu của nhân viên '{}': {}", nhanVienId, count);
        return count;
    }
    
    @Override
    public boolean validateYeuCau(YeuCau yeuCau) {
        if (yeuCau == null) {
            return false;
        }
        
        return validateYeuCauInput(yeuCau.getThietBiId(), yeuCau.getNhanVienId());
    }
    
    @Override
    public boolean duyetYeuCau(Long yeuCauId) {
        return capNhatTrangThai(yeuCauId, YeuCau.TrangThaiYeuCau.DA_DUYET);
    }
    
    @Override
    public boolean tuChoiYeuCau(Long yeuCauId, String lyDoTuChoi) {
        // Cập nhật lý do từ chối
        Optional<YeuCau> yeuCauOpt = yeuCauDAO.findYeuCauById(yeuCauId);
        if (yeuCauOpt.isPresent()) {
            YeuCau yeuCau = yeuCauOpt.get();
            yeuCau.setLyDo(lyDoTuChoi);
            yeuCauDAO.updateYeuCau(yeuCau);
        }
        
        return capNhatTrangThai(yeuCauId, YeuCau.TrangThaiYeuCau.TU_CHOI);
    }
    
    @Override
    public boolean capPhatThietBi(Long yeuCauId) {
        return capNhatTrangThai(yeuCauId, YeuCau.TrangThaiYeuCau.DA_CAP_PHAT);
    }
    
    @Override
    public boolean huyYeuCau(Long yeuCauId, String lyDoHuy) {
        // Cập nhật lý do hủy
        Optional<YeuCau> yeuCauOpt = yeuCauDAO.findYeuCauById(yeuCauId);
        if (yeuCauOpt.isPresent()) {
            YeuCau yeuCau = yeuCauOpt.get();
            yeuCau.setLyDo(lyDoHuy);
            yeuCauDAO.updateYeuCau(yeuCau);
        }
        
        return capNhatTrangThai(yeuCauId, YeuCau.TrangThaiYeuCau.DA_HUY);
    }
    
    /**
     * Validate input cơ bản
     */
    private boolean validateYeuCauInput(Long thietBiId, String nhanVienId) {
        if (thietBiId == null || thietBiId <= 0) {
            logger.warn("ID thiết bị không hợp lệ");
            return false;
        }
        
        if (nhanVienId == null) {
            logger.warn("ID nhân viên không hợp lệ");
            return false;
        }
        
        return true;
    }
} 