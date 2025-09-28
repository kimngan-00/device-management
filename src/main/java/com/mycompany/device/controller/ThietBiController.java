package com.mycompany.device.controller;

import com.mycompany.device.model.ThietBi;
import com.mycompany.device.service.ThietBiService;
import com.mycompany.device.service.impl.ThietBiServiceImpl;
import java.util.List;

/**
 * Lớp điều khiển (Controller) trung gian giữa Panel và Service Layer.
 */
public class ThietBiController {

    private final ThietBiService thietBiService;
    
    public ThietBiController() {
        // Sử dụng Service đã được sửa lỗi
        this.thietBiService = new ThietBiServiceImpl();
    }
    
    public List<ThietBi> getAllThietBi() {
        return thietBiService.findAll();
    }
    
    public boolean createThietBi(ThietBi thietBi) {
        return thietBiService.save(thietBi);
    }
    
    public boolean updateThietBi(ThietBi thietBi) {
        return thietBiService.update(thietBi);
    }
    
    public boolean deleteThietBi(Long id) {
        return thietBiService.delete(id);
    }
    
    public List<ThietBi> searchThietBi(String keyword, String searchType) {
        // Gọi Service (Service sẽ gọi DAO đã được sửa lỗi SQL)
        return thietBiService.search(keyword, searchType);
    }
}   