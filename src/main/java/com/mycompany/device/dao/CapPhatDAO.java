package com.mycompany.device.dao;

import com.mycompany.device.model.CapPhat;
import java.util.List;
import java.util.Optional;

/**
 * DAO interface cho CapPhat (Cấp phát thiết bị)
 * @author Kim Ngan - Data Access Layer
 */
public interface CapPhatDAO {
    
    /**
     * Tạo record cấp phát mới
     */
    boolean createCapPhat(CapPhat capPhat);
    
    /**
     * Cập nhật thông tin cấp phát
     */
    boolean updateCapPhat(CapPhat capPhat);
    
    /**
     * Xóa record cấp phát
     */
    boolean deleteCapPhat(Long capPhatId);
    
    /**
     * Tìm cấp phát theo ID
     */
    Optional<CapPhat> findCapPhatById(Long capPhatId);
    
    /**
     * Lấy tất cả record cấp phát
     */
    List<CapPhat> getAllCapPhat();
    
    /**
     * Tìm cấp phát theo yêu cầu
     */
    List<CapPhat> findCapPhatByYeuCau(Long yeuCauId);
    
    /**
     * Tìm cấp phát đang hoạt động (chưa trả)
     */
    List<CapPhat> findActiveCapPhat();
    
    /**
     * Tìm cấp phát đã trả
     */
    List<CapPhat> findReturnedCapPhat();
    
    /**
     * Kiểm tra thiết bị có đang được cấp phát không
     */
    boolean isThietBiBeingAllocated(Long thietBiId);
    
    /**
     * Tìm cấp phát hiện tại của thiết bị (chưa trả)
     */
    Optional<CapPhat> findCurrentCapPhatByThietBi(Long thietBiId);
    
    /**
     * Đếm số lượng cấp phát
     */
    int countCapPhat();
    
    /**
     * Đếm cấp phát đang hoạt động
     */
    int countActiveCapPhat();
    
    /**
     * Đếm cấp phát đã trả
     */
    int countReturnedCapPhat();
    
    /**
     * Cập nhật thông tin trả thiết bị
     */
    boolean updateReturnInfo(Long capPhatId, CapPhat.TinhTrangTra tinhTrangTra, String ghiChu);
}
