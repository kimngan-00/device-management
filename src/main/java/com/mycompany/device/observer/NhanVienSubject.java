package com.mycompany.device.observer;

import com.mycompany.device.model.NhanVien;
import java.util.ArrayList;
import java.util.List;

/**
 * Subject class cho Observer Pattern
 * @author Kim Ngan - Observer Pattern
 */
public class NhanVienSubject {
    
    private final List<NhanVienObserver> observers = new ArrayList<>();
    
    public void addObserver(NhanVienObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    public void removeObserver(NhanVienObserver observer) {
        observers.remove(observer);
    }
    
    public void notifyNhanVienAdded(NhanVien nhanVien) {
        for (NhanVienObserver observer : observers) {
            try {
                observer.onNhanVienAdded(nhanVien);
            } catch (Exception e) {
                System.err.println("Lỗi khi thông báo observer: " + e.getMessage());
            }
        }
    }
    
    public void notifyNhanVienDeleted(String maNhanVien) {
        for (NhanVienObserver observer : observers) {
            try {
                observer.onNhanVienDeleted(maNhanVien);
            } catch (Exception e) {
                System.err.println("Lỗi khi thông báo observer: " + e.getMessage());
            }
        }
    }
    
    public void notifyNhanVienUpdated(NhanVien nhanVien, NhanVien oldNhanVien) {
        for (NhanVienObserver observer : observers) {
            try {
                observer.onNhanVienUpdated(nhanVien, oldNhanVien);
            } catch (Exception e) {
                System.err.println("Lỗi khi thông báo observer: " + e.getMessage());
            }
        }
    }
    
    public void notifyNhanVienLoggedIn(NhanVien nhanVien) {
        for (NhanVienObserver observer : observers) {
            try {
                observer.onNhanVienLoggedIn(nhanVien);
            } catch (Exception e) {
                System.err.println("Lỗi khi thông báo observer: " + e.getMessage());
            }
        }
    }
    
    public void notifyNhanVienLoggedOut(NhanVien nhanVien) {
        for (NhanVienObserver observer : observers) {
            try {
                observer.onNhanVienLoggedOut(nhanVien);
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
