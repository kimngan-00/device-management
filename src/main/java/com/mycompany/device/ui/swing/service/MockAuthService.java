package com.mycompany.device.ui.swing.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Mock implementation cho UIAuthService
 * Chứa dữ liệu mẫu để test UI
 * 
 * Khi Backend sẵn sàng, chỉ cần thay MockAuthService bằng RealAuthService
 * 
 * @author UI Team - Mock Data for UI Testing
 */
public class MockAuthService implements UIAuthService {
    
    // Mock database - dữ liệu mẫu
    private static final Map<String, MockUser> MOCK_USERS = new HashMap<>();
    
    static {
        // Tạo tài khoản mẫu
        MOCK_USERS.put("admin@company.com", new MockUser(
            "NV001", "Nguyễn Văn Admin", "admin@company.com", "admin123", "ADMIN", "Phòng Kỹ thuật"
        ));
        
        MOCK_USERS.put("manager@company.com", new MockUser(
            "NV002", "Trần Thị Manager", "manager@company.com", "manager123", "MANAGER", "Phòng Nhân sự"
        ));
        
        MOCK_USERS.put("staff@company.com", new MockUser(
            "NV003", "Lê Văn Staff", "staff@company.com", "staff123", "STAFF", "Phòng Tài chính"
        ));
        
        // Thêm tài khoản từ database thật (nếu có)
        MOCK_USERS.put("nva@company.com", new MockUser(
            "NV001", "Nguyễn Văn A", "nva@company.com", "123456", "ADMIN", "Phòng Kỹ thuật"
        ));
    }
    
    @Override
    public AuthResult login(String email, String password) {
        // Simulate network delay
        try {
            Thread.sleep(500); // 0.5 giây
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Validate input
        if (email == null || email.trim().isEmpty()) {
            return new AuthResult(false, "Email không được để trống", null, null);
        }
        
        if (password == null || password.trim().isEmpty()) {
            return new AuthResult(false, "Mật khẩu không được để trống", null, null);
        }
        
        // Check credentials
        MockUser user = MOCK_USERS.get(email.toLowerCase().trim());
        if (user == null) {
            return new AuthResult(false, "Email không tồn tại trong hệ thống", null, null);
        }
        
        if (!user.password.equals(password)) {
            return new AuthResult(false, "Mật khẩu không đúng", null, null);
        }
        
        // Success - create session and user info
        String sessionId = UUID.randomUUID().toString();
        UserInfo userInfo = new UserInfo(
            user.id,
            user.name,
            user.email,
            user.role,
            user.department
        );
        
        return new AuthResult(true, "Đăng nhập thành công", userInfo, sessionId);
    }
    
    @Override
    public void logout(String userId) {
        // Mock logout - chỉ log
        System.out.println("User " + userId + " đã đăng xuất");
    }
    
    @Override
    public boolean isSessionValid(String sessionId) {
        // Mock session validation - always true for testing
        return sessionId != null && !sessionId.trim().isEmpty();
    }
    
    /**
     * Lấy danh sách tài khoản mẫu để hiển thị cho người test
     */
    public static String getMockAccountsInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== TÀI KHOẢN MẪU ===\n");
        sb.append("1. Admin:\n");
        sb.append("   Email: admin@company.com\n");
        sb.append("   Password: admin123\n\n");
        
        sb.append("2. Manager:\n");
        sb.append("   Email: manager@company.com\n");
        sb.append("   Password: manager123\n\n");
        
        sb.append("3. Staff:\n");
        sb.append("   Email: staff@company.com\n");
        sb.append("   Password: staff123\n\n");
        
        sb.append("4. Tài khoản DB:\n");
        sb.append("   Email: nva@company.com\n");
        sb.append("   Password: 123456\n");
        
        return sb.toString();
    }
    
    /**
     * Inner class để lưu thông tin user mock
     */
    private static class MockUser {
        final String id;
        final String name;
        final String email;
        final String password;
        final String role;
        final String department;
        
        MockUser(String id, String name, String email, String password, String role, String department) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.password = password;
            this.role = role;
            this.department = department;
        }
    }
}
