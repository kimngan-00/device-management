package com.mycompany.device.service;

import com.mycompany.device.model.PhongBan;
import java.util.List;
import java.util.Optional;

/**
 * Service interface cho PhongBan
 * @author Kim Ngan - Service Layer
 */
public interface PhongBanService {
    
    /**
     * Tạo phòng ban mới
     * @param maPhongBan Mã phòng ban
     * @param tenPhongBan Tên phòng ban
     * @param moTa Mô tả (có thể null)
     * @return true nếu tạo thành công, false nếu thất bại
     */
    boolean taoPhongBan(String maPhongBan, String tenPhongBan, String moTa);
    
    /**
     * Tạo phòng ban mới từ object
     * @param phongBan Phòng ban cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    boolean taoPhongBan(PhongBan phongBan);
    
    /**
     * Cập nhật thông tin phòng ban
     * @param maPhongBan Mã phòng ban cần cập nhật
     * @param tenPhongBan Tên phòng ban mới
     * @param moTa Mô tả mới (có thể null)
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean capNhatPhongBan(String maPhongBan, String tenPhongBan, String moTa);
    
    /**
     * Cập nhật thông tin phòng ban từ object
     * @param phongBan Phòng ban cần cập nhật
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean capNhatPhongBan(PhongBan phongBan);
    
    /**
     * Xem danh sách tất cả phòng ban
     * @return List chứa tất cả phòng ban
     */
    List<PhongBan> xemDanhSachPhongBan();
    
    /**
     * Tìm phòng ban theo mã
     * @param maPhongBan Mã phòng ban
     * @return Optional chứa phòng ban nếu tìm thấy
     */
    Optional<PhongBan> timPhongBanTheoMa(String maPhongBan);
    
    /**
     * Tìm kiếm phòng ban theo tên
     * @param tenPhongBan Tên phòng ban cần tìm
     * @return List chứa các phòng ban tìm được
     */
    List<PhongBan> timKiemPhongBanTheoTen(String tenPhongBan);
    
    /**
     * Xóa phòng ban
     * @param maPhongBan Mã phòng ban cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean xoaPhongBan(String maPhongBan);
    
    /**
     * Kiểm tra phòng ban có tồn tại không
     * @param maPhongBan Mã phòng ban cần kiểm tra
     * @return true nếu tồn tại, false nếu không
     */
    boolean kiemTraPhongBanTonTai(String maPhongBan);
    
    /**
     * Đếm tổng số phòng ban
     * @return Số lượng phòng ban
     */
    int demSoLuongPhongBan();
    
    /**
     * Validate thông tin phòng ban
     * @param phongBan Phòng ban cần validate
     * @return true nếu hợp lệ, false nếu không
     */
    boolean validatePhongBan(PhongBan phongBan);
}
