package com.mycompany.device.service.impl;

import com.mycompany.device.dao.ThietBiDAO;
import com.mycompany.device.dao.impl.ThietBiDAOMySQLImpl;
import com.mycompany.device.model.ThietBi;
import com.mycompany.device.service.ThietBiService;
import java.util.List;

/**
 * Triển khai nghiệp vụ cho ThietBi.
 */
public class ThietBiServiceImpl implements ThietBiService {
    
    private final ThietBiDAO thietBiDAO;

    public ThietBiServiceImpl() {
        this.thietBiDAO = new ThietBiDAOMySQLImpl();
    }

    @Override
    public List<ThietBi> findAll() {
        return thietBiDAO.findAll();
    }

    @Override
    public ThietBi findById(Long id) {
        return thietBiDAO.findById(id);
    }

    @Override
    public boolean save(ThietBi thietBi) {
        if (thietBiDAO.findBySoSerial(thietBi.getSoSerial()) != null) {
            throw new IllegalArgumentException("Lỗi: Số Serial đã tồn tại trong hệ thống.");
        }
        return thietBiDAO.save(thietBi);
    }

    @Override
    public boolean update(ThietBi thietBi) {
        ThietBi existingBySerial = thietBiDAO.findBySoSerial(thietBi.getSoSerial());
        if (existingBySerial != null && !existingBySerial.getId().equals(thietBi.getId())) {
             throw new IllegalArgumentException("Lỗi: Số Serial đã tồn tại ở thiết bị khác.");
        }
        return thietBiDAO.update(thietBi);
    }

    @Override
    public boolean delete(Long id) {
        return thietBiDAO.delete(id);
    }

    /**
     * Chuyển toàn bộ logic tìm kiếm xuống DAO để tận dụng truy vấn SQL đã sửa lỗi ID.
     */
    @Override
    public List<ThietBi> search(String keyword, String searchType) {
        // Nếu keyword rỗng, trả về tất cả
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll();
        }
        
        return thietBiDAO.search(keyword, searchType);
    }
}