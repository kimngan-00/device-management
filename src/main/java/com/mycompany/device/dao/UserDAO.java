package com.mycompany.device.dao;

import com.mycompany.device.model.User;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface cho User
 * @author Team Member 2 - Data Access Layer
 */
public interface UserDAO {
    
    /**
     * Thêm người dùng mới
     */
    boolean addUser(User user);
    
    /**
     * Cập nhật thông tin người dùng
     */
    boolean updateUser(User user);
    
    /**
     * Xóa người dùng theo ID
     */
    boolean deleteUser(String userId);
    
    /**
     * Tìm người dùng theo ID
     */
    Optional<User> findById(String userId);
    
    /**
     * Tìm người dùng theo username
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Lấy tất cả người dùng
     */
    List<User> getAllUsers();
    
    /**
     * Tìm người dùng theo tên
     */
    List<User> findByName(String fullName);
    
    /**
     * Tìm người dùng theo phòng ban
     */
    List<User> findByDepartment(String department);
    
    /**
     * Tìm người dùng theo vai trò
     */
    List<User> findByRole(User.UserRole role);
    
    /**
     * Tìm người dùng theo email
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Lấy danh sách người dùng đang hoạt động
     */
    List<User> getActiveUsers();
    
    /**
     * Đếm tổng số người dùng
     */
    int getTotalUserCount();
    
    /**
     * Đếm người dùng theo vai trò
     */
    int getUserCountByRole(User.UserRole role);
} 