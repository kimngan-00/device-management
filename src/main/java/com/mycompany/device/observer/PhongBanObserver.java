package com.mycompany.device.observer;

import com.mycompany.device.model.PhongBan;

/**
 * Observer interface cho PhongBan
 * Theo dõi và phản ứng với các thay đổi của PhongBan objects
 * @author Kim Ngan - Observer Pattern
 */
public interface PhongBanObserver {
    
    /**
     * Được gọi khi có phòng ban mới được thêm
     * @param phongBan Phòng ban vừa được thêm
     */
    void onPhongBanAdded(PhongBan phongBan);
    
    /**
     * Được gọi khi phòng ban bị xóa
     * @param maPhongBan Mã phòng ban bị xóa
     */
    void onPhongBanDeleted(String maPhongBan);
    
    /**
     * Được gọi khi thông tin phòng ban được cập nhật
     * @param phongBan Phòng ban sau khi cập nhật
     * @param oldPhongBan Phòng ban trước khi cập nhật
     */
    void onPhongBanUpdated(PhongBan phongBan, PhongBan oldPhongBan);
}
