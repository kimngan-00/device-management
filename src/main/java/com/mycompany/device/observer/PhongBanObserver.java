package com.mycompany.device.observer;

import com.mycompany.device.model.PhongBan;

/**
 * Observer interface cho PhongBan
 */
public interface PhongBanObserver {
    
    /**
     * @param phongBan Phòng ban vừa được thêm
     */
    void onPhongBanAdded(PhongBan phongBan);
    
    /**
     * @param maPhongBan Mã phòng ban bị xóa
     */
    void onPhongBanDeleted(String maPhongBan);
    
    /**
     * @param phongBan Phòng ban sau khi cập nhật
     * @param oldPhongBan Phòng ban trước khi cập nhật
     */
    void onPhongBanUpdated(PhongBan phongBan, PhongBan oldPhongBan);
}
