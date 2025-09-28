package com.mycompany.device.model;

import com.mycompany.device.service.NhanVienService;
import com.mycompany.device.util.PasswordUtil;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Model đại diện cho nhân viên trong hệ thống
 * @author Kim Ngan - Model Layer
 */
public class NhanVien {
    private String maNhanVien;
    private String tenNhanVien;
    private String email;
    private String password;
    private String soDienThoai;
    private NhanVienRole role;
    private String maPhongBan;
    private LocalDateTime ngayTao;
    
    public enum NhanVienRole {
        ADMIN("Quản trị viên"),
        STAFF("Nhân viên");
        
        private final String displayName;
        
        NhanVienRole(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    // Constructors
    public NhanVien() {
        this.role = NhanVienRole.STAFF;
        this.ngayTao = LocalDateTime.now();
    }
    
    public NhanVien(String maNhanVien, String tenNhanVien, String email, String password) {
        this();
        this.maNhanVien = maNhanVien;
        this.tenNhanVien = tenNhanVien;
        this.email = email;
        this.password = password;
    }
    
    public NhanVien(String maNhanVien, String tenNhanVien, String email, String password, 
                   String soDienThoai, NhanVienRole role, String maPhongBan) {
        this(maNhanVien, tenNhanVien, email, password);
        this.soDienThoai = soDienThoai;
        this.role = role;
        this.maPhongBan = maPhongBan;
    }
    
    public NhanVien(String maNhanVien, String tenNhanVien, String email, String password, 
                   String soDienThoai, NhanVienRole role, String maPhongBan, LocalDateTime ngayTao) {
        this(maNhanVien, tenNhanVien, email, password, soDienThoai, role, maPhongBan);
        this.ngayTao = ngayTao;
    }
    
    // Getters and Setters
    public String getId() {
        return this.maNhanVien;
    }
    
    public String getMaNhanVien() { 
        return maNhanVien; 
    }
    
    public void setMaNhanVien(String maNhanVien) { 
        this.maNhanVien = maNhanVien; 
    }
    
    public String getTenNhanVien() { 
        return tenNhanVien; 
    }
    
    public void setTenNhanVien(String tenNhanVien) { 
        this.tenNhanVien = tenNhanVien; 
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }
    
    public String getPassword() { 
        return password; 
    }
    
    public void setPassword(String password) { 
        this.password = password; 
    }
    
    public String getSoDienThoai() { 
        return soDienThoai; 
    }
    
    public void setSoDienThoai(String soDienThoai) { 
        this.soDienThoai = soDienThoai; 
    }
    
    public NhanVienRole getRole() { 
        return role; 
    }
    
    public void setRole(NhanVienRole role) { 
        this.role = role; 
    }
    
    public String getMaPhongBan() { 
        return maPhongBan; 
    }
    
    public void setMaPhongBan(String maPhongBan) { 
        this.maPhongBan = maPhongBan; 
    }
    
    public LocalDateTime getNgayTao() { 
        return ngayTao; 
    }
    
    public void setNgayTao(LocalDateTime ngayTao) { 
        this.ngayTao = ngayTao; 
    }
    
    // Business methods
    public boolean isAdmin() {
        return NhanVienRole.ADMIN.equals(this.role);
    }
    
    public boolean isStaff() {
        return NhanVienRole.STAFF.equals(this.role);
    }
    
    @Override
    public String toString() {
        return String.format("NhanVien{maNhanVien='%s', tenNhanVien='%s', email='%s', role='%s', maPhongBan='%s', ngayTao='%s'}", 
                           maNhanVien, tenNhanVien, email, role.getDisplayName(), maPhongBan, ngayTao);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NhanVien nhanVien = (NhanVien) obj;
        return maNhanVien != null ? maNhanVien.equals(nhanVien.maNhanVien) : nhanVien.maNhanVien == null;
    }
    
    @Override
    public int hashCode() {
        return maNhanVien != null ? maNhanVien.hashCode() : 0;
    }

    /**
     * Static method để thực hiện login
     * @param email Email đăng nhập
     * @param password Mật khẩu
     * @param nhanVienService Service để xử lý
     * @return Optional chứa NhanVien nếu đăng nhập thành công
     */
    public static Optional<NhanVien> login(String email, String password, NhanVienService nhanVienService) {
        if (nhanVienService == null) {
            throw new IllegalArgumentException("NhanVienService không được null");
        }
        return nhanVienService.dangNhap(email, password);
    }

    /**
     * Method kiểm tra mật khẩu cho instance hiện tại
     * @param password Mật khẩu cần kiểm tra
     * @return true nếu mật khẩu đúng
     */
    public boolean checkPassword(String password) {
        if (password == null || this.password == null) {
            return false;
        }
        return PasswordUtil.verifyPasswordSimple(password, this.password);
    }

    /**
     * Method authenticate - kết hợp email và password check
     * @param email Email để authenticate
     * @param password Password để authenticate
     * @return true nếu email và password khớp với instance này
     */
    public boolean authenticate(String email, String password) {
        if (email == null || !email.trim().equalsIgnoreCase(this.email)) {
            return false;
        }
        return checkPassword(password);
    }
}
