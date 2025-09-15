package com.mycompany.device.dao;

import com.mycompany.device.model.PhongBan;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface cho PhongBan
 * @author Kim Ngan - DAO Layer
 */
public interface PhongBanDAO {
    
    /**
     * Tạo phòng ban mới
     * @param phongBan Phòng ban cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    boolean createPhongBan(PhongBan phongBan);
    
    /**
     * Cập nhật thông tin phòng ban
     * @param phongBan Phòng ban cần cập nhật
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updatePhongBan(PhongBan phongBan);
    
    /**
     * Xóa phòng ban theo mã
     * @param maPhongBan Mã phòng ban cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deletePhongBan(String maPhongBan);
    
    /**
     * Tìm phòng ban theo mã
     * @param maPhongBan Mã phòng ban
     * @return Optional chứa phòng ban nếu tìm thấy
     */
    Optional<PhongBan> findPhongBanByMa(String maPhongBan);
    
    /**
     * Lấy danh sách tất cả phòng ban
     * @return List chứa tất cả phòng ban
     */
    List<PhongBan> getAllPhongBan();
    
    /**
     * Tìm phòng ban theo tên (tìm kiếm gần đúng)
     * @param tenPhongBan Tên phòng ban cần tìm
     * @return List chứa các phòng ban tìm được
     */
    List<PhongBan> searchPhongBanByTen(String tenPhongBan);
    
    /**
     * Kiểm tra phòng ban có tồn tại không
     * @param maPhongBan Mã phòng ban cần kiểm tra
     * @return true nếu tồn tại, false nếu không
     */
    boolean existsPhongBan(String maPhongBan);
    
    /**
     * Đếm tổng số phòng ban
     * @return Số lượng phòng ban
     */
    int countPhongBan();
}
