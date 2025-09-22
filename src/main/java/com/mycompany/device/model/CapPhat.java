package com.mycompany.device.model;

import java.time.LocalDateTime;

/**
 * Model đại diện cho cấp phát thiết bị trong hệ thống
 * @author Kim Ngan - Model Layer
 */
public class CapPhat {
    private Long id;
    private Long yeuCauId;
    private LocalDateTime ngayCap;
    private LocalDateTime ngayTra;
    private TinhTrangTra tinhTrangTra;
    private String ghiChu;
    
    public enum TinhTrangTra {
        TOT("Tốt"),
        TRAY_XUOC("Trầy xước"),
        HU_HONG("Hư hỏng"),
        MAT("Mất");
        
        private final String displayName;
        
        TinhTrangTra(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    // Constructors
    public CapPhat() {
        this.ngayCap = LocalDateTime.now();
    }
    
    public CapPhat(Long yeuCauId) {
        this();
        this.yeuCauId = yeuCauId;
    }
    
    public CapPhat(Long yeuCauId, LocalDateTime ngayCap) {
        this.yeuCauId = yeuCauId;
        this.ngayCap = ngayCap;
    }
    
    public CapPhat(Long yeuCauId, LocalDateTime ngayCap, LocalDateTime ngayTra, 
                   TinhTrangTra tinhTrangTra, String ghiChu) {
        this(yeuCauId, ngayCap);
        this.ngayTra = ngayTra;
        this.tinhTrangTra = tinhTrangTra;
        this.ghiChu = ghiChu;
    }
    
    public CapPhat(Long id, Long yeuCauId, LocalDateTime ngayCap, LocalDateTime ngayTra, 
                   TinhTrangTra tinhTrangTra, String ghiChu) {
        this(yeuCauId, ngayCap, ngayTra, tinhTrangTra, ghiChu);
        this.id = id;
    }
    
    // Getters and Setters
    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }
    
    public Long getYeuCauId() { 
        return yeuCauId; 
    }
    
    public void setYeuCauId(Long yeuCauId) { 
        this.yeuCauId = yeuCauId; 
    }
    
    public LocalDateTime getNgayCap() { 
        return ngayCap; 
    }
    
    public void setNgayCap(LocalDateTime ngayCap) { 
        this.ngayCap = ngayCap; 
    }
    
    public LocalDateTime getNgayTra() { 
        return ngayTra; 
    }
    
    public void setNgayTra(LocalDateTime ngayTra) { 
        this.ngayTra = ngayTra; 
    }
    
    public TinhTrangTra getTinhTrangTra() { 
        return tinhTrangTra; 
    }
    
    public void setTinhTrangTra(TinhTrangTra tinhTrangTra) { 
        this.tinhTrangTra = tinhTrangTra; 
    }
    
    public String getGhiChu() { 
        return ghiChu; 
    }
    
    public void setGhiChu(String ghiChu) { 
        this.ghiChu = ghiChu; 
    }
    
    // Business methods
    public boolean isTot() {
        return TinhTrangTra.TOT.equals(this.tinhTrangTra);
    }
    
    public boolean isTrayXuoc() {
        return TinhTrangTra.TRAY_XUOC.equals(this.tinhTrangTra);
    }
    
    public boolean isHuHong() {
        return TinhTrangTra.HU_HONG.equals(this.tinhTrangTra);
    }
    
    public boolean isMat() {
        return TinhTrangTra.MAT.equals(this.tinhTrangTra);
    }
    
    public boolean hasNgayTra() {
        return ngayTra != null;
    }
    
    public boolean hasGhiChu() {
        return ghiChu != null && !ghiChu.trim().isEmpty();
    }
    
    public boolean isDaTra() {
        return hasNgayTra();
    }
    
    public boolean isChuaTra() {
        return !hasNgayTra();
    }
    
    public void traThietBi(TinhTrangTra tinhTrangTra, String ghiChu) {
        this.ngayTra = LocalDateTime.now();
        this.tinhTrangTra = tinhTrangTra;
        this.ghiChu = ghiChu;
    }
    
    @Override
    public String toString() {
        return String.format("CapPhat{id=%d, yeuCauId=%d, ngayCap='%s', ngayTra='%s', tinhTrangTra='%s', ghiChu='%s'}", 
                           id, yeuCauId, ngayCap, ngayTra, 
                           tinhTrangTra != null ? tinhTrangTra.getDisplayName() : "null", ghiChu);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CapPhat capPhat = (CapPhat) obj;
        return id != null ? id.equals(capPhat.id) : capPhat.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
