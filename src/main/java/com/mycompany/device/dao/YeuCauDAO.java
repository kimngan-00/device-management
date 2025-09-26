package com.mycompany.device.dao;

import com.mycompany.device.model.YeuCau;
import java.util.List;
import java.util.Optional;

/**
 * DAO interface cho YeuCau
 * @author Kim Ngan - Data Access Layer
 */
public interface YeuCauDAO {
    
    /**
     * Tạo yêu cầu mới
     */
    boolean createYeuCau(YeuCau yeuCau);
    
    /**
     * Cập nhật yêu cầu
     */
    boolean updateYeuCau(YeuCau yeuCau);
    
    /**
     * Xóa yêu cầu
     */
    boolean deleteYeuCau(Long yeuCauId);
    
    /**
     * Tìm yêu cầu theo ID
     */
    Optional<YeuCau> findYeuCauById(Long yeuCauId);
    
    /**
     * Lấy tất cả yêu cầu
     */
    List<YeuCau> getAllYeuCau();
    
    /**
     * Tìm yêu cầu theo thiết bị
     */
    List<YeuCau> findYeuCauByThietBi(Long thietBiId);
    
    /**
     * Tìm yêu cầu theo nhân viên
     */
    List<YeuCau> findYeuCauByNhanVien(Long nhanVienId);
    
    /**
     * Tìm yêu cầu theo trạng thái
     */
    List<YeuCau> findYeuCauByTrangThai(YeuCau.TrangThaiYeuCau trangThai);
    
    /**
     * Tìm kiếm yêu cầu theo lý do
     */
    List<YeuCau> searchYeuCauByLyDo(String lyDo);
    
    /**
     * Kiểm tra yêu cầu có tồn tại
     */
    boolean existsYeuCau(Long yeuCauId);
    
    /**
     * Đếm số lượng yêu cầu
     */
    int countYeuCau();
    
    /**
     * Đếm số yêu cầu theo trạng thái
     */
    int countYeuCauByTrangThai(YeuCau.TrangThaiYeuCau trangThai);
    
    /**
     * Đếm số yêu cầu của nhân viên
     */
    int countYeuCauByNhanVien(Long nhanVienId);
} 