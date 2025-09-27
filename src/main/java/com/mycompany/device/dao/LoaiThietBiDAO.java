package com.mycompany.device.dao;



import com.mycompany.device.model.LoaiThietBi;
import java.util.List;

/**
 * Giao diện định nghĩa các phương thức truy cập dữ liệu (CRUD) cho đối tượng LoaiThietBi.
 * @author Kim Ngan - LoaiThietBi DAO Interface
 */
public interface LoaiThietBiDAO {
    // Thao tác CRUD cơ bản
    List<LoaiThietBi> findAll();
    LoaiThietBi findById(Long id);
    boolean save(LoaiThietBi loaiThietBi);
    boolean update(LoaiThietBi loaiThietBi);
    boolean delete(Long id);
    
    // Tìm kiếm theo Mã Loại (dùng cho logic nghiệp vụ: kiểm tra trùng lặp)
    LoaiThietBi findByMaLoai(String maLoai);
}