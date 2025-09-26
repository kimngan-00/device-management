package com.mycompany.device.observer;

import com.mycompany.device.model.YeuCau;
import java.util.ArrayList;
import java.util.List;

/**
 * Subject class cho Observer Pattern của YeuCau
 * @author Kim Ngan - Observer Pattern
 */
public class YeuCauSubject {
    
    private final List<YeuCauObserver> observers = new ArrayList<>();
    
    /**
     * Thêm observer
     */
    public void addObserver(YeuCauObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    /**
     * Xóa observer
     */
    public void removeObserver(YeuCauObserver observer) {
        observers.remove(observer);
    }
    
    /**
     * Thông báo yêu cầu mới được tạo
     */
    public void notifyYeuCauAdded(YeuCau yeuCau) {
        for (YeuCauObserver observer : observers) {
            try {
                observer.onYeuCauAdded(yeuCau);
            } catch (Exception e) {
                System.err.println("Lỗi khi thông báo observer: " + e.getMessage());
            }
        }
    }
    
    /**
     * Thông báo yêu cầu bị xóa
     */
    public void notifyYeuCauDeleted(Long yeuCauId) {
        for (YeuCauObserver observer : observers) {
            try {
                observer.onYeuCauDeleted(yeuCauId);
            } catch (Exception e) {
                System.err.println("Lỗi khi thông báo observer: " + e.getMessage());
            }
        }
    }
    
    /**
     * Thông báo yêu cầu được cập nhật
     */
    public void notifyYeuCauUpdated(YeuCau yeuCau, YeuCau oldYeuCau) {
        for (YeuCauObserver observer : observers) {
            try {
                observer.onYeuCauUpdated(yeuCau, oldYeuCau);
            } catch (Exception e) {
                System.err.println("Lỗi khi thông báo observer: " + e.getMessage());
            }
        }
    }
    
    /**
     * Thông báo trạng thái yêu cầu thay đổi
     */
    public void notifyYeuCauStatusChanged(YeuCau yeuCau, YeuCau.TrangThaiYeuCau oldStatus, YeuCau.TrangThaiYeuCau newStatus) {
        for (YeuCauObserver observer : observers) {
            try {
                observer.onYeuCauStatusChanged(yeuCau, oldStatus, newStatus);
            } catch (Exception e) {
                System.err.println("Lỗi khi thông báo observer: " + e.getMessage());
            }
        }
    }
    
    /**
     * Thông báo yêu cầu được duyệt
     */
    public void notifyYeuCauApproved(YeuCau yeuCau) {
        for (YeuCauObserver observer : observers) {
            try {
                observer.onYeuCauApproved(yeuCau);
            } catch (Exception e) {
                System.err.println("Lỗi khi thông báo observer: " + e.getMessage());
            }
        }
    }
    
    /**
     * Thông báo yêu cầu bị từ chối
     */
    public void notifyYeuCauRejected(YeuCau yeuCau) {
        for (YeuCauObserver observer : observers) {
            try {
                observer.onYeuCauRejected(yeuCau);
            } catch (Exception e) {
                System.err.println("Lỗi khi thông báo observer: " + e.getMessage());
            }
        }
    }
    
    /**
     * Thông báo yêu cầu được cấp phát
     */
    public void notifyYeuCauAllocated(YeuCau yeuCau) {
        for (YeuCauObserver observer : observers) {
            try {
                observer.onYeuCauAllocated(yeuCau);
            } catch (Exception e) {
                System.err.println("Lỗi khi thông báo observer: " + e.getMessage());
            }
        }
    }
    
    /**
     * Thông báo yêu cầu bị hủy
     */
    public void notifyYeuCauCancelled(YeuCau yeuCau) {
        for (YeuCauObserver observer : observers) {
            try {
                observer.onYeuCauCancelled(yeuCau);
            } catch (Exception e) {
                System.err.println("Lỗi khi thông báo observer: " + e.getMessage());
            }
        }
    }
    
    /**
     * Lấy số lượng observers
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