package com.mycompany.device.controller;

import com.mycompany.device.model.NhanVien;
import com.mycompany.device.service.NhanVienService;
import com.mycompany.device.service.impl.NhanVienServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

/**
 * Controller cho Authentication trong MVC pattern
 * Xử lý logic đăng nhập, đăng xuất và quản lý session
 */
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    private final NhanVienService nhanVienService;
    private NhanVien currentUser;
    private String currentSessionId;
    
    /**
     * Constructor - khởi tạo Controller với NhanVienService
     */
    public AuthController() {
        this.nhanVienService = new NhanVienServiceImpl();
        logger.info("AuthController đã được khởi tạo thành công");
    }
    
    /**
     * Constructor với dependency injection (cho testing)
     */
    public AuthController(NhanVienService nhanVienService) {
        this.nhanVienService = nhanVienService;
        logger.info("AuthController đã được khởi tạo với injected service");
    }
    
    /**
     * Đăng nhập theo email và password
     * @param email Email người dùng
     * @param password Mật khẩu
     * @return AuthResult chứa thông tin đăng nhập
     */
    public AuthResult login(String email, String password) {
        logger.info("Bắt đầu xử lý đăng nhập cho email: {}", email);
        
        try {
            // Validate input
            if (!validateLoginInput(email, password)) {
                return new AuthResult(false, "Thông tin đăng nhập không hợp lệ", null, null);
            }
            
            // Gọi service để đăng nhập - sử dụng method có sẵn
            Optional<NhanVien> nhanVienOpt = nhanVienService.dangNhap(email.trim(), password);
            
            if (nhanVienOpt.isPresent()) {
                NhanVien nhanVien = nhanVienOpt.get();
                
                // Tạo session
                String sessionId = generateSessionId(nhanVien);
                
                // Lưu thông tin current user
                this.currentUser = nhanVien;
                this.currentSessionId = sessionId;
                
                logger.info("Đăng nhập thành công: {} - {}", email, nhanVien.getTenNhanVien());
                
                return new AuthResult(true, "Đăng nhập thành công!", nhanVien, sessionId);
                
            } else {
                logger.warn("Đăng nhập thất bại: {}", email);
                return new AuthResult(false, "Email hoặc mật khẩu không đúng!", null, null);
            }
            
        } catch (Exception e) {
            logger.error("Lỗi khi xử lý đăng nhập", e);
            return new AuthResult(false, "Lỗi hệ thống: " + e.getMessage(), null, null);
        }
    }
    
    /**
     * Đăng xuất người dùng hiện tại
     */
    public void logout() {
        if (currentUser != null) {
            logger.info("Nhân viên đăng xuất: {} - {}", currentUser.getEmail(), currentUser.getTenNhanVien());
            
            // Nếu service có method dangXuat
            if (nhanVienService instanceof NhanVienServiceImpl) {
                ((NhanVienServiceImpl) nhanVienService).dangXuat(currentUser);
            }
            
            // Clear session
            this.currentUser = null;
            this.currentSessionId = null;
        }
    }
    
    /**
     * Kiểm tra user đã đăng nhập chưa
     */
    public boolean isLoggedIn() {
        return currentUser != null && currentSessionId != null;
    }
    
    /**
     * Lấy thông tin user hiện tại
     */
    public NhanVien getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Lấy session ID hiện tại
     */
    public String getCurrentSessionId() {
        return currentSessionId;
    }
    
    /**
     * Kiểm tra quyền admin
     */
    public boolean isCurrentUserAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }
    
    /**
     * Kiểm tra quyền staff
     */
    public boolean isCurrentUserStaff() {
        return currentUser != null && currentUser.isStaff();
    }
    
    /**
     * Kiểm tra session có hợp lệ không
     */
    public boolean isSessionValid(String sessionId) {
        return currentSessionId != null && currentSessionId.equals(sessionId);
    }
    
    /**
     * Refresh session (gia hạn thời gian)
     */
    public String refreshSession() {
        if (currentUser != null) {
            this.currentSessionId = generateSessionId(currentUser);
            logger.debug("Session được refresh cho user: {}", currentUser.getEmail());
            return currentSessionId;
        }
        return null;
    }
    
    /**
     * Validate input đăng nhập
     */
    private boolean validateLoginInput(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            logger.warn("Email không được để trống");
            return false;
        }
        
        if (password == null || password.trim().isEmpty()) {
            logger.warn("Mật khẩu không được để trống");
            return false;
        }
        
        if (!email.trim().contains("@")) {
            logger.warn("Email không hợp lệ: {}", email);
            return false;
        }
        
        if (email.trim().length() > 255) {
            logger.warn("Email quá dài: {}", email);
            return false;
        }
        
        return true;
    }
    
    /**
     * Tạo session ID unique
     */
    private String generateSessionId(NhanVien nhanVien) {
        return "session_" + nhanVien.getMaNhanVien() + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }
    
    /**
     * Class kết quả đăng nhập
     */
    public static class AuthResult {
        private final boolean success;
        private final String message;
        private final NhanVien nhanVien;
        private final String sessionId;
        
        public AuthResult(boolean success, String message, NhanVien nhanVien, String sessionId) {
            this.success = success;
            this.message = message;
            this.nhanVien = nhanVien;
            this.sessionId = sessionId;
        }
        
        // Getters
        public boolean isSuccess() { 
            return success; 
        }
        
        public String getMessage() { 
            return message; 
        }
        
        public NhanVien getNhanVien() { 
            return nhanVien; 
        }
        
        public String getSessionId() { 
            return sessionId; 
        }
        
        @Override
        public String toString() {
            return String.format("AuthResult{success=%s, message='%s', nhanVien=%s, sessionId='%s'}", 
                               success, message, nhanVien != null ? nhanVien.getTenNhanVien() : null, sessionId);
        }
    }
} 