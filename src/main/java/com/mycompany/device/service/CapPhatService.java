package com.mycompany.device.service;

import com.mycompany.device.model.CapPhat;
import java.util.List;
import java.util.Optional;

/**
 * Service interface cho CapPhat (Cấp phát thiết bị)
 * @author Kim Ngan - Service Layer
 */
public interface CapPhatService {
    
    /**
     * Tạo record cấp phát mới khi admin phê duyệt yêu cầu
     */
    boolean taoCapPhat(Long yeuCauId);
    
    /**
     * Tạo record cấp phát với thông tin chi tiết
     */
    boolean taoCapPhat(CapPhat capPhat);
    
    /**
     * Cập nhật thông tin trả thiết bị
     */
    boolean traThietBi(Long capPhatId, CapPhat.TinhTrangTra tinhTrangTra, String ghiChu);
    
    /**
     * Tìm cấp phát theo ID
     */
    Optional<CapPhat> timCapPhatTheoId(Long capPhatId);
    
    /**
     * Lấy danh sách tất cả cấp phát
     */
    List<CapPhat> layDanhSachCapPhat();
    
    /**
     * Lấy danh sách cấp phát theo yêu cầu
     */
    List<CapPhat> layDanhSachCapPhatTheoYeuCau(Long yeuCauId);
    
    /**
     * Lấy danh sách cấp phát đang hoạt động (chưa trả)
     */
    List<CapPhat> layDanhSachCapPhatDangHoatDong();
    
    /**
     * Lấy danh sách cấp phát đã trả
     */
    List<CapPhat> layDanhSachCapPhatDaTra();
    
    /**
     * Kiểm tra thiết bị có đang được cấp phát không
     */
    boolean kiemTraThietBiDangCapPhat(Long thietBiId);
    
    /**
     * Tìm cấp phát hiện tại của thiết bị (chưa trả)
     */
    Optional<CapPhat> timCapPhatHienTaiCuaThietBi(Long thietBiId);
    
    /**
     * Lấy thống kê cấp phát
     */
    ThongKeCapPhat layThongKeCapPhat();
    
    /**
     * Inner class cho thống kê
     */
    class ThongKeCapPhat {
        private int tongSoCapPhat;
        private int soCapPhatDangHoatDong;
        private int soCapPhatDaTra;
        
        public ThongKeCapPhat(int tongSoCapPhat, int soCapPhatDangHoatDong, int soCapPhatDaTra) {
            this.tongSoCapPhat = tongSoCapPhat;
            this.soCapPhatDangHoatDong = soCapPhatDangHoatDong;
            this.soCapPhatDaTra = soCapPhatDaTra;
        }
        
        // Getters
        public int getTongSoCapPhat() { return tongSoCapPhat; }
        public int getSoCapPhatDangHoatDong() { return soCapPhatDangHoatDong; }
        public int getSoCapPhatDaTra() { return soCapPhatDaTra; }
    }
}
