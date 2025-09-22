package com.mycompany.device.model;

/**
 * Model đại diện cho loại thiết bị trong hệ thống
 * @author Kim Ngan - Model Layer
 */
public class LoaiThietBi {
    private Long id;
    private String maLoai;
    private String tenLoai;
    private String moTa;
    
    // Constructors
    public LoaiThietBi() {
    }
    
    public LoaiThietBi(String maLoai, String tenLoai) {
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
    }
    
    public LoaiThietBi(String maLoai, String tenLoai, String moTa) {
        this(maLoai, tenLoai);
        this.moTa = moTa;
    }
    
    public LoaiThietBi(Long id, String maLoai, String tenLoai, String moTa) {
        this(maLoai, tenLoai, moTa);
        this.id = id;
    }
    
    // Getters and Setters
    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }
    
    public String getMaLoai() { 
        return maLoai; 
    }
    
    public void setMaLoai(String maLoai) { 
        this.maLoai = maLoai; 
    }
    
    public String getTenLoai() { 
        return tenLoai; 
    }
    
    public void setTenLoai(String tenLoai) { 
        this.tenLoai = tenLoai; 
    }
    
    public String getMoTa() { 
        return moTa; 
    }
    
    public void setMoTa(String moTa) { 
        this.moTa = moTa; 
    }
    
    // Business methods
    public boolean hasMoTa() {
        return moTa != null && !moTa.trim().isEmpty();
    }
    
    @Override
    public String toString() {
        return String.format("LoaiThietBi{id=%d, maLoai='%s', tenLoai='%s', moTa='%s'}", 
                           id, maLoai, tenLoai, moTa);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LoaiThietBi loaiThietBi = (LoaiThietBi) obj;
        return id != null ? id.equals(loaiThietBi.id) : loaiThietBi.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
