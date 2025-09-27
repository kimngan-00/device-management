package com.mycompany.device.service;

import com.mycompany.device.model.ThietBi;
import java.util.List;

/**
 * Giao diện định nghĩa các nghiệp vụ (Business Logic) cho đối tượng ThietBi.
 * @author Kim Ngan - Service Interface
 */
public interface ThietBiService {
    // Thao tác CRUD
    List<ThietBi> findAll();
    ThietBi findById(Long id);
    boolean save(ThietBi thietBi);
    boolean update(ThietBi thietBi);
    boolean delete(Long id);
    
    // Logic nghiệp vụ bổ sung
    List<ThietBi> search(String keyword, String searchType);
}