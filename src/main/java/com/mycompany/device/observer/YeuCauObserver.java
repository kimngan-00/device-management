package com.mycompany.device.observer;

import com.mycompany.device.model.YeuCau;

/**
 * Observer interface cho YeuCau
 * @author Kim Ngan - Observer Pattern
 */
public interface YeuCauObserver {
    
    /**
     * Được gọi khi có yêu cầu mới được tạo
     */
    void onYeuCauAdded(YeuCau yeuCau);
    
    /**
     * Được gọi khi yêu cầu bị xóa
     */
    void onYeuCauDeleted(Long yeuCauId);
    
    /**
     * Được gọi khi yêu cầu được cập nhật
     */
    void onYeuCauUpdated(YeuCau yeuCau, YeuCau oldYeuCau);
    
    /**
     * Được gọi khi trạng thái yêu cầu thay đổi
     */
    void onYeuCauStatusChanged(YeuCau yeuCau, YeuCau.TrangThaiYeuCau oldStatus, YeuCau.TrangThaiYeuCau newStatus);
    
    /**
     * Được gọi khi yêu cầu được duyệt
     */
    void onYeuCauApproved(YeuCau yeuCau);
    
    /**
     * Được gọi khi yêu cầu bị từ chối
     */
    void onYeuCauRejected(YeuCau yeuCau);
    
    /**
     * Được gọi khi yêu cầu được cấp phát
     */
    void onYeuCauAllocated(YeuCau yeuCau);
    
    /**
     * Được gọi khi yêu cầu bị hủy
     */
    void onYeuCauCancelled(YeuCau yeuCau);
} 