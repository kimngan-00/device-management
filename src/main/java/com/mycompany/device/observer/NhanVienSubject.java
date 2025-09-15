package com.mycompany.device.observer;

import com.mycompany.device.model.NhanVien;
import java.util.ArrayList;
import java.util.List;

/**
 * Subject class cho Observer Pattern
 * Quản lý danh sách các observers và thông báo khi có thay đổi
 * @author Kim Ngan - Observer Pattern
 */
public class NhanVienSubject {
    
    private final List<NhanVienObserver> observers = new ArrayList<>();
    
    /**
     * Thêm observer vào danh sách
     * @param observer Observer cần thêm
     */
    public void addObserver(NhanVienObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    /**
     * Xóa observer khỏi danh sách
     * @param observer Observer cần xóa
     */
    public void removeObserver(NhanVienObserver observer) {
        observers.remove(observer);
    }
    
    /**
     * Thông báo khi có nhân viên mới được thêm
     * @param nhanVien Nhân viên vừa được thêm
     */
    public void notifyNhanVienAdded(NhanVien nhanVien) {
        for (NhanVienObserver observer : observers) {
            try {
                observer.onNhanVienAdded(nhanVien);
            } catch (Exception e) {
                System.err.println("Lỗi khi thông báo observer: " + e.getMessage());
            }
        }
    }
    
    /**
     * Thông báo khi nhân viên bị xóa
     * @param maNhanVien Mã nhân viên bị xóa
     */
    public void notifyNhanVienDeleted(String maNhanVien) {
        for (NhanVienObserver observer : observers) {
            try {
                observer.onNhanVienDeleted(maNhanVien);
            } catch (Exception e) {
                System.err.println("Lỗi khi thông báo observer: " + e.getMessage());
            }
        }
    }
    
    /**
     * Thông báo khi nhân viên được cập nhật
     * @param nhanVien Nhân viên sau khi cập nhật
     * @param oldNhanVien Nhân viên trước khi cập nhật
     */
    public void notifyNhanVienUpdated(NhanVien nhanVien, NhanVien oldNhanVien) {
        for (NhanVienObserver observer : observers) {
            try {
                observer.onNhanVienUpdated(nhanVien, oldNhanVien);
            } catch (Exception e) {
                System.err.println("Lỗi khi thông báo observer: " + e.getMessage());
            }
        }
    }
    
    /**
     * Thông báo khi nhân viên đăng nhập
     * @param nhanVien Nhân viên đăng nhập
     */
    public void notifyNhanVienLoggedIn(NhanVien nhanVien) {
        for (NhanVienObserver observer : observers) {
            try {
                observer.onNhanVienLoggedIn(nhanVien);
            } catch (Exception e) {
                System.err.println("Lỗi khi thông báo observer: " + e.getMessage());
            }
        }
    }
    
    /**
     * Thông báo khi nhân viên đăng xuất
     * @param nhanVien Nhân viên đăng xuất
     */
    public void notifyNhanVienLoggedOut(NhanVien nhanVien) {
        for (NhanVienObserver observer : observers) {
            try {
                observer.onNhanVienLoggedOut(nhanVien);
            } catch (Exception e) {
                System.err.println("Lỗi khi thông báo observer: " + e.getMessage());
            }
        }
    }
    
    /**
     * Lấy số lượng observers hiện tại
     * @return Số lượng observers
     */
    public int getObserverCount() {
        return observers.size();
    }
    
    /**
     * Xóa tất cả observers
     */
    public void clearObservers() {
        observers.clear();
    }
}
