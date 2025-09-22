package com.mycompany.device.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Model đại diện cho thiết bị trong hệ thống
 * @author Kim Ngan - Model Layer
 */
public class ThietBi {
    private Long id;
    private String soSerial;
    private Long loaiId;
    private TrangThaiThietBi trangThai;
    private LocalDate ngayMua;
    private BigDecimal giaMua;
    private String ghiChu;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public enum TrangThaiThietBi {
        TON_KHO("Tồn kho"),
        DANG_CAP_PHAT("Đang cấp phát"),
        DANG_BAO_TRI("Đang bảo trì"),
        HU_HONG("Hư hỏng"),
        NGUNG_SU_DUNG("Ngừng sử dụng");
        
        private final String displayName;
        
        TrangThaiThietBi(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    // Constructors
    public ThietBi() {
        this.trangThai = TrangThaiThietBi.TON_KHO;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public ThietBi(Long loaiId) {
        this();
        this.loaiId = loaiId;
    }
    
    public ThietBi(String soSerial, Long loaiId, TrangThaiThietBi trangThai) {
        this(loaiId);
        this.soSerial = soSerial;
        this.trangThai = trangThai;
    }
    
    public ThietBi(String soSerial, Long loaiId, TrangThaiThietBi trangThai,
                   LocalDate ngayMua, BigDecimal giaMua, String ghiChu) {
        this(soSerial, loaiId, trangThai);
        this.ngayMua = ngayMua;
        this.giaMua = giaMua;
        this.ghiChu = ghiChu;
    }
    
    public ThietBi(Long id, String soSerial, Long loaiId, TrangThaiThietBi trangThai,
                   LocalDate ngayMua, BigDecimal giaMua, String ghiChu, 
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this(soSerial, loaiId, trangThai, ngayMua, giaMua, ghiChu);
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }
    
    public String getSoSerial() { 
        return soSerial; 
    }
    
    public void setSoSerial(String soSerial) { 
        this.soSerial = soSerial; 
    }
    
    public Long getLoaiId() { 
        return loaiId; 
    }
    
    public void setLoaiId(Long loaiId) { 
        this.loaiId = loaiId; 
    }
    
    public TrangThaiThietBi getTrangThai() { 
        return trangThai; 
    }
    
    public void setTrangThai(TrangThaiThietBi trangThai) { 
        this.trangThai = trangThai; 
    }
    
    public LocalDate getNgayMua() { 
        return ngayMua; 
    }
    
    public void setNgayMua(LocalDate ngayMua) { 
        this.ngayMua = ngayMua; 
    }
    
    public BigDecimal getGiaMua() { 
        return giaMua; 
    }
    
    public void setGiaMua(BigDecimal giaMua) { 
        this.giaMua = giaMua; 
    }
    
    public String getGhiChu() { 
        return ghiChu; 
    }
    
    public void setGhiChu(String ghiChu) { 
        this.ghiChu = ghiChu; 
    }
    
    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }
    
    public LocalDateTime getUpdatedAt() { 
        return updatedAt; 
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) { 
        this.updatedAt = updatedAt; 
    }
    
    // Business methods
    public boolean isTonKho() {
        return TrangThaiThietBi.TON_KHO.equals(this.trangThai);
    }
    
    public boolean isDangCapPhat() {
        return TrangThaiThietBi.DANG_CAP_PHAT.equals(this.trangThai);
    }
    
    public boolean isDangBaoTri() {
        return TrangThaiThietBi.DANG_BAO_TRI.equals(this.trangThai);
    }
    
    public boolean isHuHong() {
        return TrangThaiThietBi.HU_HONG.equals(this.trangThai);
    }
    
    public boolean isNgungSuDung() {
        return TrangThaiThietBi.NGUNG_SU_DUNG.equals(this.trangThai);
    }
    
    public boolean hasGiaMua() {
        return giaMua != null && giaMua.compareTo(BigDecimal.ZERO) > 0;
    }
    
    public boolean hasNgayMua() {
        return ngayMua != null;
    }
    
    public boolean hasGhiChu() {
        return ghiChu != null && !ghiChu.trim().isEmpty();
    }
    
    @Override
    public String toString() {
        return String.format("ThietBi{id=%d, soSerial='%s', loaiId=%d, trangThai='%s', ngayMua='%s', giaMua=%s, ghiChu='%s', createdAt='%s', updatedAt='%s'}", 
                           id, soSerial, loaiId, trangThai.getDisplayName(), ngayMua, giaMua, ghiChu, createdAt, updatedAt);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ThietBi thietBi = (ThietBi) obj;
        return id != null ? id.equals(thietBi.id) : thietBi.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
