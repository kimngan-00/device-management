package com.mycompany.device.service;

import com.mycompany.device.model.NhanVien;
import java.util.List;
import java.util.Optional;

/**
 * Service interface cho NhanVien
 * @author Kim Ngan - Service Layer
 */
public interface NhanVienService {
    
    /**
     * Tạo nhân viên mới
     * @param maNhanVien Mã nhân viên
     * @param tenNhanVien Tên nhân viên
     * @param email Email
     * @param password Mật khẩu
     * @param soDienThoai Số điện thoại (có thể null)
     * @param role Vai trò
     * @param maPhongBan Mã phòng ban
     * @return true nếu tạo thành công, false nếu thất bại
     */
    boolean taoNhanVien(String maNhanVien, String tenNhanVien, String email, String password, 
                       String soDienThoai, NhanVien.NhanVienRole role, String maPhongBan);
    
    /**
     * Tạo nhân viên mới từ object
     * @param nhanVien Nhân viên cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    boolean taoNhanVien(NhanVien nhanVien);
    
    /**
     * Cập nhật thông tin nhân viên
     * @param maNhanVien Mã nhân viên cần cập nhật
     * @param tenNhanVien Tên nhân viên mới
     * @param email Email mới
     * @param password Mật khẩu mới
     * @param soDienThoai Số điện thoại mới
     * @param role Vai trò mới
     * @param maPhongBan Mã phòng ban mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean capNhatNhanVien(String maNhanVien, String tenNhanVien, String email, String password, 
                           String soDienThoai, NhanVien.NhanVienRole role, String maPhongBan);
    
    /**
     * Cập nhật thông tin nhân viên từ object
     * @param nhanVien Nhân viên cần cập nhật
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean capNhatNhanVien(NhanVien nhanVien);
    
    /**
     * Xem danh sách tất cả nhân viên
     * @return List chứa tất cả nhân viên
     */
    List<NhanVien> xemDanhSachNhanVien();
    
    /**
     * Tìm nhân viên theo mã
     * @param maNhanVien Mã nhân viên
     * @return Optional chứa nhân viên nếu tìm thấy
     */
    Optional<NhanVien> timNhanVienTheoMa(String maNhanVien);
    
    /**
     * Tìm nhân viên theo email
     * @param email Email nhân viên
     * @return Optional chứa nhân viên nếu tìm thấy
     */
    Optional<NhanVien> timNhanVienTheoEmail(String email);
    
    /**
     * Tìm kiếm nhân viên theo tên
     * @param tenNhanVien Tên nhân viên cần tìm
     * @return List chứa các nhân viên tìm được
     */
    List<NhanVien> timKiemNhanVienTheoTen(String tenNhanVien);
    
    /**
     * Tìm nhân viên theo phòng ban
     * @param maPhongBan Mã phòng ban
     * @return List chứa các nhân viên thuộc phòng ban
     */
    List<NhanVien> timNhanVienTheoPhongBan(String maPhongBan);
    
    /**
     * Tìm nhân viên theo vai trò
     * @param role Vai trò nhân viên
     * @return List chứa các nhân viên có vai trò
     */
    List<NhanVien> timNhanVienTheoVaiTro(NhanVien.NhanVienRole role);
    
    /**
     * Xóa nhân viên
     * @param maNhanVien Mã nhân viên cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean xoaNhanVien(String maNhanVien);
    
    /**
     * Kiểm tra nhân viên có tồn tại không
     * @param maNhanVien Mã nhân viên cần kiểm tra
     * @return true nếu tồn tại, false nếu không
     */
    boolean kiemTraNhanVienTonTai(String maNhanVien);
    
    /**
     * Kiểm tra email có tồn tại không
     * @param email Email cần kiểm tra
     * @return true nếu tồn tại, false nếu không
     */
    boolean kiemTraEmailTonTai(String email);
    
    /**
     * Đếm tổng số nhân viên
     * @return Số lượng nhân viên
     */
    int demSoLuongNhanVien();
    
    /**
     * Đếm số nhân viên theo phòng ban
     * @param maPhongBan Mã phòng ban
     * @return Số lượng nhân viên trong phòng ban
     */
    int demSoLuongNhanVienTheoPhongBan(String maPhongBan);
    
    /**
     * Validate thông tin nhân viên
     * @param nhanVien Nhân viên cần validate
     * @return true nếu hợp lệ, false nếu không
     */
    boolean validateNhanVien(NhanVien nhanVien);
    
    /**
     * Đăng nhập nhân viên
     * @param email Email
     * @param password Mật khẩu
     * @return Optional chứa nhân viên nếu đăng nhập thành công
     */
    Optional<NhanVien> dangNhap(String email, String password);
}
