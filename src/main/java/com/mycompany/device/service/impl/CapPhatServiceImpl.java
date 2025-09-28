package com.mycompany.device.service.impl;

import com.mycompany.device.dao.CapPhatDAO;
import com.mycompany.device.dao.YeuCauDAO;
import com.mycompany.device.dao.impl.CapPhatDAOMySQLImpl;
import com.mycompany.device.dao.impl.YeuCauDAOMySQLImpl;
import com.mycompany.device.model.CapPhat;
import com.mycompany.device.model.YeuCau;
import com.mycompany.device.service.CapPhatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation của CapPhatService
 * @author Kim Ngan - Service Implementation Layer
 */
public class CapPhatServiceImpl implements CapPhatService {
    
    private static final Logger logger = LoggerFactory.getLogger(CapPhatServiceImpl.class);
    
    private final CapPhatDAO capPhatDAO;
    private final YeuCauDAO yeuCauDAO;
    
    public CapPhatServiceImpl() {
        this.capPhatDAO = new CapPhatDAOMySQLImpl();
        this.yeuCauDAO = new YeuCauDAOMySQLImpl();
    }
    
    @Override
    public boolean taoCapPhat(Long yeuCauId) {
        logger.info("Tạo cấp phát cho yêu cầu: {}", yeuCauId);
        
        // Kiểm tra yêu cầu có tồn tại không
        Optional<YeuCau> yeuCauOpt = yeuCauDAO.findYeuCauById(yeuCauId);
        if (!yeuCauOpt.isPresent()) {
            logger.warn("Không tìm thấy yêu cầu: {}", yeuCauId);
            return false;
        }
        
        YeuCau yeuCau = yeuCauOpt.get();
        
        // Kiểm tra trạng thái yêu cầu phải là đã duyệt
        if (yeuCau.getTrangThai() != YeuCau.TrangThaiYeuCau.DA_DUYET) {
            logger.warn("Yêu cầu chưa được duyệt: {} - Trạng thái: {}", 
                       yeuCauId, yeuCau.getTrangThai());
            return false;
        }
        
        // Kiểm tra thiết bị có đang được cấp phát không
        if (capPhatDAO.isThietBiBeingAllocated(yeuCau.getThietBiId())) {
            logger.warn("Thiết bị {} đang được cấp phát, không thể tạo cấp phát mới", 
                       yeuCau.getThietBiId());
            return false;
        }
        
        // Tạo record cấp phát mới
        CapPhat capPhat = new CapPhat();
        capPhat.setYeuCauId(yeuCauId);
        capPhat.setNgayCap(LocalDateTime.now());
        capPhat.setNgayTra(null); // Chưa trả
        capPhat.setTinhTrangTra(null); // Chưa có tình trạng trả
        capPhat.setGhiChu("Thiết bị đã được cấp phát cho nhân viên");
        
        boolean result = capPhatDAO.createCapPhat(capPhat);
        
        if (result) {
            logger.info("Tạo cấp phát thành công: ID={}, YeuCau ID={}", 
                       capPhat.getId(), yeuCauId);
            
            // Cập nhật trạng thái yêu cầu thành DA_CAP_PHAT
            yeuCau.setTrangThai(YeuCau.TrangThaiYeuCau.DA_CAP_PHAT);
            yeuCau.setNgayCapNhat(LocalDateTime.now());
            yeuCauDAO.updateYeuCau(yeuCau);
        }
        
        return result;
    }
    
    @Override
    public boolean taoCapPhat(CapPhat capPhat) {
        logger.info("Tạo cấp phát với thông tin chi tiết: YeuCau ID={}", 
                   capPhat.getYeuCauId());
        
        if (capPhat.getYeuCauId() == null) {
            logger.warn("Không thể tạo cấp phát: thiếu thông tin yêu cầu");
            return false;
        }
        
        return capPhatDAO.createCapPhat(capPhat);
    }
    
    @Override
    public boolean traThietBi(Long capPhatId, CapPhat.TinhTrangTra tinhTrangTra, String ghiChu) {
        logger.info("Trả thiết bị: CapPhat ID={}, Tình trạng={}", 
                   capPhatId, tinhTrangTra);
        
        // Kiểm tra cấp phát có tồn tại không
        Optional<CapPhat> capPhatOpt = capPhatDAO.findCapPhatById(capPhatId);
        if (!capPhatOpt.isPresent()) {
            logger.warn("Không tìm thấy cấp phát: {}", capPhatId);
            return false;
        }
        
        CapPhat capPhat = capPhatOpt.get();
        
        // Kiểm tra thiết bị đã được trả chưa
        if (capPhat.getNgayTra() != null) {
            logger.warn("Thiết bị đã được trả trước đó: CapPhat ID={}", capPhatId);
            return false;
        }
        
        // Cập nhật thông tin trả thiết bị
        boolean result = capPhatDAO.updateReturnInfo(capPhatId, tinhTrangTra, ghiChu);
        
        if (result) {
            logger.info("Trả thiết bị thành công: CapPhat ID={}", capPhatId);
        }
        
        return result;
    }
    
    @Override
    public Optional<CapPhat> timCapPhatTheoId(Long capPhatId) {
        return capPhatDAO.findCapPhatById(capPhatId);
    }
    
    @Override
    public List<CapPhat> layDanhSachCapPhat() {
        return capPhatDAO.getAllCapPhat();
    }
    
    @Override
    public List<CapPhat> layDanhSachCapPhatTheoYeuCau(Long yeuCauId) {
        return capPhatDAO.findCapPhatByYeuCau(yeuCauId);
    }
    
    @Override
    public List<CapPhat> layDanhSachCapPhatDangHoatDong() {
        return capPhatDAO.findActiveCapPhat();
    }
    
    @Override
    public List<CapPhat> layDanhSachCapPhatDaTra() {
        return capPhatDAO.findReturnedCapPhat();
    }
    
    @Override
    public boolean kiemTraThietBiDangCapPhat(Long thietBiId) {
        return capPhatDAO.isThietBiBeingAllocated(thietBiId);
    }
    
    @Override
    public Optional<CapPhat> timCapPhatHienTaiCuaThietBi(Long thietBiId) {
        return capPhatDAO.findCurrentCapPhatByThietBi(thietBiId);
    }
    
    @Override
    public ThongKeCapPhat layThongKeCapPhat() {
        int tongSoCapPhat = capPhatDAO.countCapPhat();
        int soCapPhatDangHoatDong = capPhatDAO.countActiveCapPhat();
        int soCapPhatDaTra = capPhatDAO.countReturnedCapPhat();
        
        return new ThongKeCapPhat(tongSoCapPhat, soCapPhatDangHoatDong, soCapPhatDaTra);
    }
}
