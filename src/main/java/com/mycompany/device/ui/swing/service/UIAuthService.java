package com.mycompany.device.ui.swing.service;

/**
 * Interface dành cho UI để giao tiếp với Backend
 * Backend team sẽ implement interface này
 * 
 * @author UI Team - Interface for Backend Integration
 */
public interface UIAuthService {
    
    /**
     * Xác thực đăng nhập
     * @param email Email người dùng
     * @param password Mật khẩu
     * @return AuthResult chứa thông tin đăng nhập
     */
    AuthResult login(String email, String password);
    
    /**
     * Đăng xuất
     * @param userId ID người dùng
     */
    void logout(String userId);
    
    /**
     * Kiểm tra session có còn hợp lệ không
     * @param sessionId Session ID
     * @return true nếu session hợp lệ
     */
    boolean isSessionValid(String sessionId);
    
    /**
     * Kết quả xác thực đăng nhập
     */
    public static class AuthResult {
        private final boolean success;
        private final String message;
        private final UserInfo userInfo;
        private final String sessionId;
        
        public AuthResult(boolean success, String message, UserInfo userInfo, String sessionId) {
            this.success = success;
            this.message = message;
            this.userInfo = userInfo;
            this.sessionId = sessionId;
        }
        
        // Getters
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public UserInfo getUserInfo() { return userInfo; }
        public String getSessionId() { return sessionId; }
    }
    
    /**
     * Thông tin người dùng cơ bản cho UI
     */
    public static class UserInfo {
        private final String id;
        private final String name;
        private final String email;
        private final String role;
        private final String department;
        
        public UserInfo(String id, String name, String email, String role, String department) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.role = role;
            this.department = department;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
        public String getDepartment() { return department; }
        
        @Override
        public String toString() {
            return "UserInfo{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    ", role='" + role + '\'' +
                    ", department='" + department + '\'' +
                    '}';
        }
    }
}
