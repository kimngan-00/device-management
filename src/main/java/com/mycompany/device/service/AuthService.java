package com.mycompany.device.service;

import com.mycompany.device.controller.AuthController.AuthResult;
import com.mycompany.device.model.NhanVien;

/**
 * Service interface cho Authentication
 */
public interface AuthService {
    
    /**
     * Đăng nhập
     */
    AuthResult login(String email, String password);
    
    /**
     * Đăng xuất
     */
    void logout();
    
    /**
     * Kiểm tra đã đăng nhập
     */
    boolean isLoggedIn();
    
    /**
     * Lấy user hiện tại
     */
    NhanVien getCurrentUser();
    
    /**
     * Kiểm tra session
     */
    boolean isSessionValid(String sessionId);
} 