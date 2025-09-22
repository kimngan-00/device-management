package com.mycompany.device.model;

import java.time.LocalDateTime;

/**
 * Model đại diện cho yêu cầu trong hệ thống
 * @author Kim Ngan - Model Layer
 */
public class YeuCau {
    private Long id;
    private Long thietBiId;
    private Long nhanVienId;
    private TrangThaiYeuCau trangThai;
    private String lyDo;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
    
    public enum TrangThaiYeuCau {
        CHO_DUYET("Chờ duyệt"),
        DA_DUYET("Đã duyệt"),
        TU_CHOI("Từ chối"),
        DA_CAP_PHAT("Đã cấp phát"),
        DA_HUY("Đã hủy");
        
        private final String displayName;
        
        TrangThaiYeuCau(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    // Constructors
    public YeuCau() {
        this.trangThai = TrangThaiYeuCau.CHO_DUYET;
        this.ngayTao = LocalDateTime.now();
        this.ngayCapNhat = LocalDateTime.now();
    }
    
    public YeuCau(Long thietBiId, Long nhanVienId) {
        this();
        this.thietBiId = thietBiId;
        this.nhanVienId = nhanVienId;
    }
    
    public YeuCau(Long thietBiId, Long nhanVienId, String lyDo) {
        this(thietBiId, nhanVienId);
        this.lyDo = lyDo;
    }
    
    public YeuCau(Long thietBiId, Long nhanVienId, TrangThaiYeuCau trangThai, String lyDo) {
        this(thietBiId, nhanVienId, lyDo);
        this.trangThai = trangThai;
    }
    
    public YeuCau(Long id, Long thietBiId, Long nhanVienId, TrangThaiYeuCau trangThai, 
                   String lyDo, LocalDateTime ngayTao, LocalDateTime ngayCapNhat) {
        this(thietBiId, nhanVienId, trangThai, lyDo);
        this.id = id;
        this.ngayTao = ngayTao;
        this.ngayCapNhat = ngayCapNhat;
    }
    
    // Getters and Setters
    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }
    
    public Long getThietBiId() { 
        return thietBiId; 
    }
    
    public void setThietBiId(Long thietBiId) { 
        this.thietBiId = thietBiId; 
    }
    
    public Long getNhanVienId() { 
        return nhanVienId; 
    }
    
    public void setNhanVienId(Long nhanVienId) { 
        this.nhanVienId = nhanVienId; 
    }
    
    public TrangThaiYeuCau getTrangThai() { 
        return trangThai; 
    }
    
    public void setTrangThai(TrangThaiYeuCau trangThai) { 
        this.trangThai = trangThai; 
    }
    
    public String getLyDo() { 
        return lyDo; 
    }
    
    public void setLyDo(String lyDo) { 
        this.lyDo = lyDo; 
    }
    
    public LocalDateTime getNgayTao() { 
        return ngayTao; 
    }
    
    public void setNgayTao(LocalDateTime ngayTao) { 
        this.ngayTao = ngayTao; 
    }
    
    public LocalDateTime getNgayCapNhat() { 
        return ngayCapNhat; 
    }
    
    public void setNgayCapNhat(LocalDateTime ngayCapNhat) { 
        this.ngayCapNhat = ngayCapNhat; 
    }
    
    // Business methods
    public boolean isChoDuyet() {
        return TrangThaiYeuCau.CHO_DUYET.equals(this.trangThai);
    }
    
    public boolean isDaDuyet() {
        return TrangThaiYeuCau.DA_DUYET.equals(this.trangThai);
    }
    
    public boolean isTuChoi() {
        return TrangThaiYeuCau.TU_CHOI.equals(this.trangThai);
    }
    
    public boolean isDaCapPhat() {
        return TrangThaiYeuCau.DA_CAP_PHAT.equals(this.trangThai);
    }
    
    public boolean isDaHuy() {
        return TrangThaiYeuCau.DA_HUY.equals(this.trangThai);
    }
    
    public boolean hasLyDo() {
        return lyDo != null && !lyDo.trim().isEmpty();
    }
    
    public boolean isCompleted() {
        return isDaDuyet() || isTuChoi() || isDaCapPhat() || isDaHuy();
    }
    
    public boolean isPending() {
        return isChoDuyet();
    }
    
    public void updateTrangThai(TrangThaiYeuCau newTrangThai) {
        this.trangThai = newTrangThai;
        this.ngayCapNhat = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return String.format("YeuCau{id=%d, thietBiId=%d, nhanVienId=%d, trangThai='%s', lyDo='%s', ngayTao='%s', ngayCapNhat='%s'}", 
                           id, thietBiId, nhanVienId, trangThai.getDisplayName(), lyDo, ngayTao, ngayCapNhat);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        YeuCau yeuCau = (YeuCau) obj;
        return id != null ? id.equals(yeuCau.id) : yeuCau.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
