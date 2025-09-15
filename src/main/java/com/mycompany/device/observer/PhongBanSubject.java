package com.mycompany.device.observer;

import com.mycompany.device.model.PhongBan;
import java.util.ArrayList;
import java.util.List;

/**
 * Subject class cho Observer Pattern của PhongBan
 * Quản lý danh sách các observers và thông báo khi có thay đổi
 * @author Kim Ngan - Observer Pattern
 */
public class PhongBanSubject {
    
    private final List<PhongBanObserver> observers = new ArrayList<>();
    
    /**
     * Thêm observer vào danh sách
     * @param observer Observer cần thêm
     */
    public void addObserver(PhongBanObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    /**
     * Xóa observer khỏi danh sách
     * @param observer Observer cần xóa
     */
    public void removeObserver(PhongBanObserver observer) {
        observers.remove(observer);
    }
    
    /**
     * Thông báo khi có phòng ban mới được thêm
     * @param phongBan Phòng ban vừa được thêm
     */
    public void notifyPhongBanAdded(PhongBan phongBan) {
        for (PhongBanObserver observer : observers) {
            try {
                observer.onPhongBanAdded(phongBan);
            } catch (Exception e) {
                System.err.println("Lỗi khi thông báo observer: " + e.getMessage());
            }
        }
    }
    
    /**
     * Thông báo khi phòng ban bị xóa
     * @param maPhongBan Mã phòng ban bị xóa
     */
    public void notifyPhongBanDeleted(String maPhongBan) {
        for (PhongBanObserver observer : observers) {
            try {
                observer.onPhongBanDeleted(maPhongBan);
            } catch (Exception e) {
                System.err.println("Lỗi khi thông báo observer: " + e.getMessage());
            }
        }
    }
    
    /**
     * Thông báo khi phòng ban được cập nhật
     * @param phongBan Phòng ban sau khi cập nhật
     * @param oldPhongBan Phòng ban trước khi cập nhật
     */
    public void notifyPhongBanUpdated(PhongBan phongBan, PhongBan oldPhongBan) {
        for (PhongBanObserver observer : observers) {
            try {
                observer.onPhongBanUpdated(phongBan, oldPhongBan);
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
