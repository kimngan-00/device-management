package com.mycompany.device.dao;

import com.mycompany.device.model.NhanVien;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface cho NhanVien
 * @author Kim Ngan - DAO Layer
 */
public interface NhanVienDAO {
    
    /**
     * Tạo nhân viên mới
     * @param nhanVien Nhân viên cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    boolean createNhanVien(NhanVien nhanVien);
    
    /**
     * Cập nhật thông tin nhân viên
     * @param nhanVien Nhân viên cần cập nhật
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateNhanVien(NhanVien nhanVien);
    
    /**
     * Xóa nhân viên theo mã
     * @param maNhanVien Mã nhân viên cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteNhanVien(String maNhanVien);
    
    /**
     * Tìm nhân viên theo mã
     * @param maNhanVien Mã nhân viên
     * @return Optional chứa nhân viên nếu tìm thấy
     */
    Optional<NhanVien> findNhanVienByMa(String maNhanVien);
    
    /**
     * Tìm nhân viên theo email
     * @param email Email nhân viên
     * @return Optional chứa nhân viên nếu tìm thấy
     */
    Optional<NhanVien> findNhanVienByEmail(String email);
    
    /**
     * Lấy danh sách tất cả nhân viên
     * @return List chứa tất cả nhân viên
     */
    List<NhanVien> getAllNhanVien();
    
    /**
     * Tìm nhân viên theo tên (tìm kiếm gần đúng)
     * @param tenNhanVien Tên nhân viên cần tìm
     * @return List chứa các nhân viên tìm được
     */
    List<NhanVien> searchNhanVienByTen(String tenNhanVien);
    
    /**
     * Tìm nhân viên theo phòng ban
     * @param maPhongBan Mã phòng ban
     * @return List chứa các nhân viên thuộc phòng ban
     */
    List<NhanVien> findNhanVienByPhongBan(String maPhongBan);
    
    /**
     * Tìm nhân viên theo vai trò
     * @param role Vai trò nhân viên
     * @return List chứa các nhân viên có vai trò
     */
    List<NhanVien> findNhanVienByRole(NhanVien.NhanVienRole role);
    
    /**
     * Kiểm tra nhân viên có tồn tại không
     * @param maNhanVien Mã nhân viên cần kiểm tra
     * @return true nếu tồn tại, false nếu không
     */
    boolean existsNhanVien(String maNhanVien);
    
    /**
     * Kiểm tra email có tồn tại không
     * @param email Email cần kiểm tra
     * @return true nếu tồn tại, false nếu không
     */
    boolean existsEmail(String email);
    
    /**
     * Đếm tổng số nhân viên
     * @return Số lượng nhân viên
     */
    int countNhanVien();
    
    /**
     * Đếm số nhân viên theo phòng ban
     * @param maPhongBan Mã phòng ban
     * @return Số lượng nhân viên trong phòng ban
     */
    int countNhanVienByPhongBan(String maPhongBan);
}
