package com.mycompany.device.service.impl;

import com.mycompany.device.dao.LoaiThietBiDAO;
import com.mycompany.device.dao.impl.LoaiThietBiDAOMySQLImpl;
import com.mycompany.device.model.LoaiThietBi;
import com.mycompany.device.service.LoaiThietBiService;
import java.util.List;

/**
 * Triển khai nghiệp vụ cho LoaiThietBi.
 * @author Kim Ngan - LoaiThietBi Service Implementation
 */
public class LoaiThietBiServiceImpl implements LoaiThietBiService {
    
    private final LoaiThietBiDAO loaiThietBiDAO;

    public LoaiThietBiServiceImpl() {
        // Khởi tạo DAO
        this.loaiThietBiDAO = new LoaiThietBiDAOMySQLImpl();
    }

    @Override
    public List<LoaiThietBi> findAll() {
        return loaiThietBiDAO.findAll();
    }

    @Override
    public LoaiThietBi findById(Long id) {
        return loaiThietBiDAO.findById(id);
    }

    @Override
    public boolean save(LoaiThietBi loaiThietBi) {
        // Logic nghiệp vụ: Mã Loại không được trùng
        if (loaiThietBiDAO.findByMaLoai(loaiThietBi.getMaLoai()) != null) {
            throw new IllegalArgumentException("Lỗi: Mã Loại Thiết Bị '" + loaiThietBi.getMaLoai() + "' đã tồn tại.");
        }
        return loaiThietBiDAO.save(loaiThietBi);
    }

    @Override
    public boolean update(LoaiThietBi loaiThietBi) {
        // Logic nghiệp vụ: Kiểm tra trùng lặp Mã Loại, loại trừ chính bản thân nó
        LoaiThietBi existingByMaLoai = loaiThietBiDAO.findByMaLoai(loaiThietBi.getMaLoai());
        if (existingByMaLoai != null && !existingByMaLoai.getId().equals(loaiThietBi.getId())) {
             throw new IllegalArgumentException("Lỗi: Mã Loại Thiết Bị '" + loaiThietBi.getMaLoai() + "' đã tồn tại ở loại khác.");
        }
        return loaiThietBiDAO.update(loaiThietBi);
    }

    @Override
    public boolean delete(Long id) {
        // Cần thêm logic kiểm tra xem có thiết bị nào đang sử dụng loại này không
        // Nếu có, phải ném exception hoặc xóa thiết bị phụ thuộc (CASCADE DELETE trong DB)
        // Hiện tại, chỉ gọi DAO để xóa
        return loaiThietBiDAO.delete(id);
    }
}