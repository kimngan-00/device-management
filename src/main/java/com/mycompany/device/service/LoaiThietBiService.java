package com.mycompany.device.service;

import com.mycompany.device.model.LoaiThietBi;
import java.util.List;

/**
 * Giao diện định nghĩa các nghiệp vụ (Business Logic) cho đối tượng LoaiThietBi.
 * @author Kim Ngan - LoaiThietBi Service Interface
 */
public interface LoaiThietBiService {
    List<LoaiThietBi> findAll();
    LoaiThietBi findById(Long id);
    
    // Thao tác CRUD có logic nghiệp vụ
    boolean save(LoaiThietBi loaiThietBi);
    boolean update(LoaiThietBi loaiThietBi);
    boolean delete(Long id);
}