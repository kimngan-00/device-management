package com.mycompany.device.observer;

/**
 * Interface cho Subject trong Observer Pattern
 * @author Team Device Management
 */
public interface DeviceSubject {
    
    /**
     * Đăng ký observer
     */
    void registerObserver(DeviceObserver observer);
    
    /**
     * Hủy đăng ký observer
     */
    void removeObserver(DeviceObserver observer);
    
    /**
     * Thông báo cho tất cả observers
     */
    void notifyObservers();
} 