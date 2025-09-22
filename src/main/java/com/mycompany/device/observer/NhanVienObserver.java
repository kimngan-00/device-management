package com.mycompany.device.observer;

import com.mycompany.device.model.NhanVien;

/**
 * Observer interface cho NhanVien
 * @author Kim Ngan - Observer Pattern
 */
public interface NhanVienObserver {
    
    void onNhanVienAdded(NhanVien nhanVien);
    
    void onNhanVienDeleted(String maNhanVien);
    
    void onNhanVienUpdated(NhanVien nhanVien, NhanVien oldNhanVien);
    
    void onNhanVienLoggedIn(NhanVien nhanVien);
    
    void onNhanVienLoggedOut(NhanVien nhanVien);
}
