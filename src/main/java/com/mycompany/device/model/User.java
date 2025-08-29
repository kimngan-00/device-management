package com.mycompany.device.model;

import java.time.LocalDateTime;

/**
 * Model đại diện cho người dùng trong hệ thống
 * @author Team Member 1 - Model Layer
 */
public class User {
    private String userId;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String department;
    private UserRole role;
    private LocalDateTime createdAt;
    private boolean isActive;
    
    public enum UserRole {
        ADMIN("Quản trị viên"),
        MANAGER("Quản lý"),
        USER("Người dùng");
        
        private final String displayName;
        
        UserRole(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    // Constructors
    public User() {}
    
    public User(String userId, String username, String fullName, UserRole role) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    @Override
    public String toString() {
        return String.format("User{id='%s', username='%s', name='%s', role='%s'}", 
                           userId, username, fullName, role.getDisplayName());
    }
} 