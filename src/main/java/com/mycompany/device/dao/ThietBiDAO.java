package com.mycompany.device.dao;

import com.mycompany.device.model.ThietBi;
import java.util.List;

/**
 * Giao diện định nghĩa các phương thức truy cập dữ liệu cho đối tượng ThietBi.
 * @author Kim Ngan - DAO Interface
 */
public interface ThietBiDAO {
    // Lấy tất cả thiết bị
    List<ThietBi> findAll();

    // Tìm thiết bị theo ID
    ThietBi findById(Long id);

    // Lưu một thiết bị mới (INSERT)
    boolean save(ThietBi thietBi);

    // Cập nhật một thiết bị hiện có (UPDATE)
    boolean update(ThietBi thietBi);

    // Xóa một thiết bị theo ID
    boolean delete(Long id);
    
    // Tìm kiếm thiết bị theo số serial (để kiểm tra trùng lặp)
    ThietBi findBySoSerial(String soSerial);
    
    /**
     * Thực hiện tìm kiếm linh hoạt trong DB bằng SQL.
     * @param keyword Từ khóa tìm kiếm
     * @param searchType Loại trường tìm kiếm ("Số Serial", "Loại ID", "Trạng thái", "Tất cả")
     * @return Danh sách thiết bị khớp
     */
    List<ThietBi> search(String keyword, String searchType);
}