package com.mycompany.device.model;

/**
 * Model đại diện cho phòng ban trong hệ thống
 * @author Kim Ngan - Model Layer
 */
public class PhongBan {
    private String maPhongBan;
    private String tenPhongBan;
    private String moTa;
    
    // Constructors
    public PhongBan() {}
    
    public PhongBan(String maPhongBan, String tenPhongBan) {
        this.maPhongBan = maPhongBan;
        this.tenPhongBan = tenPhongBan;
    }
    
    public PhongBan(String maPhongBan, String tenPhongBan, String moTa) {
        this.maPhongBan = maPhongBan;
        this.tenPhongBan = tenPhongBan;
        this.moTa = moTa;
    }
    
    // Getters and Setters
    public String getMaPhongBan() { 
        return maPhongBan; 
    }
    
    public void setMaPhongBan(String maPhongBan) { 
        this.maPhongBan = maPhongBan; 
    }
    
    public String getTenPhongBan() { 
        return tenPhongBan; 
    }
    
    public void setTenPhongBan(String tenPhongBan) { 
        this.tenPhongBan = tenPhongBan; 
    }
    
    public String getMoTa() { 
        return moTa; 
    }
    
    public void setMoTa(String moTa) { 
        this.moTa = moTa; 
    }
    
    @Override
    public String toString() {
        return String.format("PhongBan{maPhongBan='%s', tenPhongBan='%s', moTa='%s'}", 
                           maPhongBan, tenPhongBan, moTa);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PhongBan phongBan = (PhongBan) obj;
        return maPhongBan != null ? maPhongBan.equals(phongBan.maPhongBan) : phongBan.maPhongBan == null;
    }
    
    @Override
    public int hashCode() {
        return maPhongBan != null ? maPhongBan.hashCode() : 0;
    }
}
