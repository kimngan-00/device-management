package com.mycompany.device.observer;

import com.mycompany.device.model.NhanVien;

/**
 * Observer interface cho NhanVien
 * Theo dõi và phản ứng với các thay đổi của NhanVien objects
 * @author Kim Ngan - Observer Pattern
 */
public interface NhanVienObserver {
    
    /**
     * Được gọi khi có nhân viên mới được thêm
     * @param nhanVien Nhân viên vừa được thêm
     */
    void onNhanVienAdded(NhanVien nhanVien);
    
    /**
     * Được gọi khi nhân viên bị xóa
     * @param maNhanVien Mã nhân viên bị xóa
     */
    void onNhanVienDeleted(String maNhanVien);
    
    /**
     * Được gọi khi thông tin nhân viên được cập nhật
     * @param nhanVien Nhân viên sau khi cập nhật
     * @param oldNhanVien Nhân viên trước khi cập nhật
     */
    void onNhanVienUpdated(NhanVien nhanVien, NhanVien oldNhanVien);
    
    /**
     * Được gọi khi nhân viên đăng nhập
     * @param nhanVien Nhân viên đăng nhập
     */
    void onNhanVienLoggedIn(NhanVien nhanVien);
    
    /**
     * Được gọi khi nhân viên đăng xuất
     * @param nhanVien Nhân viên đăng xuất
     */
    void onNhanVienLoggedOut(NhanVien nhanVien);
}
