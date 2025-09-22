package com.mycompany.device.observer;

import com.mycompany.device.model.PhongBan;
import java.util.ArrayList;
import java.util.List;

/**
 * Quản lý danh sách các observers và thông báo khi có thay đổi
 * @author Kim Ngan - Observer Pattern
 */
public class PhongBanSubject {
    
    private final List<PhongBanObserver> observers = new ArrayList<>();
    
    public void addObserver(PhongBanObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    public void removeObserver(PhongBanObserver observer) {
        observers.remove(observer);
    }
    
    public void notifyPhongBanAdded(PhongBan phongBan) {
        for (PhongBanObserver observer : observers) {
            try {
                observer.onPhongBanAdded(phongBan);
            } catch (Exception e) {
                System.err.println("Lỗi khi thông báo observer: " + e.getMessage());
            }
        }
    }
    
    public void notifyPhongBanDeleted(String maPhongBan) {
        for (PhongBanObserver observer : observers) {
            try {
                observer.onPhongBanDeleted(maPhongBan);
            } catch (Exception e) {
                System.err.println("Lỗi khi thông báo observer: " + e.getMessage());
            }
        }
    }
    
    public void notifyPhongBanUpdated(PhongBan phongBan, PhongBan oldPhongBan) {
        for (PhongBanObserver observer : observers) {
            try {
                observer.onPhongBanUpdated(phongBan, oldPhongBan);
            } catch (Exception e) {
                System.err.println("Lỗi khi thông báo observer: " + e.getMessage());
            }
        }
    }
    
    public int getObserverCount() {
        return observers.size();
    }

    public void clearObservers() {
        observers.clear();
    }
}
