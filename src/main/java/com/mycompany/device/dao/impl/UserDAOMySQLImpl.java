package com.mycompany.device.dao.impl;

import com.mycompany.device.dao.UserDAO;
import com.mycompany.device.model.User;
import com.mycompany.device.util.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation của UserDAO sử dụng MySQL database
 * @author Team Member 2 - Data Access Layer
 */
public class UserDAOMySQLImpl implements UserDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(UserDAOMySQLImpl.class);
    
    @Override
    public boolean addUser(User user) {
        String sql = """
            INSERT INTO users (user_id, username, full_name, email, phone, department, role)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getFullName());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getPhone());
            pstmt.setString(6, user.getDepartment());
            pstmt.setString(7, user.getRole().name());
            
            int result = pstmt.executeUpdate();
            logger.info("User added successfully: {}", user.getUserId());
            return result > 0;
            
        } catch (SQLException e) {
            logger.error("Error adding user: {}", user.getUserId(), e);
            return false;
        }
    }
    
    @Override
    public boolean updateUser(User user) {
        String sql = """
            UPDATE users SET username = ?, full_name = ?, email = ?, phone = ?, 
                           department = ?, role = ?, is_active = ?
            WHERE user_id = ?
        """;
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getFullName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPhone());
            pstmt.setString(5, user.getDepartment());
            pstmt.setString(6, user.getRole().name());
            pstmt.setBoolean(7, user.isActive());
            pstmt.setString(8, user.getUserId());
            
            int result = pstmt.executeUpdate();
            logger.info("User updated successfully: {}", user.getUserId());
            return result > 0;
            
        } catch (SQLException e) {
            logger.error("Error updating user: {}", user.getUserId(), e);
            return false;
        }
    }
    
    @Override
    public boolean deleteUser(String userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            int result = pstmt.executeUpdate();
            logger.info("User deleted successfully: {}", userId);
            return result > 0;
            
        } catch (SQLException e) {
            logger.error("Error deleting user: {}", userId, e);
            return false;
        }
    }
    
    @Override
    public Optional<User> findById(String userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                User user = mapResultSetToUser(rs);
                return Optional.of(user);
            }
            
        } catch (SQLException e) {
            logger.error("Error finding user by ID: {}", userId, e);
        }
        
        return Optional.empty();
    }
    
    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                User user = mapResultSetToUser(rs);
                return Optional.of(user);
            }
            
        } catch (SQLException e) {
            logger.error("Error finding user by username: {}", username, e);
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users ORDER BY user_id";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                User user = mapResultSetToUser(rs);
                users.add(user);
            }
            
            logger.debug("Retrieved {} users from database", users.size());
            
        } catch (SQLException e) {
            logger.error("Error retrieving all users", e);
        }
        
        return users;
    }
    
    @Override
    public List<User> findByName(String fullName) {
        String sql = "SELECT * FROM users WHERE full_name LIKE ? ORDER BY full_name";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + fullName + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                User user = mapResultSetToUser(rs);
                users.add(user);
            }
            
        } catch (SQLException e) {
            logger.error("Error finding users by name: {}", fullName, e);
        }
        
        return users;
    }
    
    @Override
    public List<User> findByDepartment(String department) {
        String sql = "SELECT * FROM users WHERE department = ? ORDER BY full_name";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, department);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                User user = mapResultSetToUser(rs);
                users.add(user);
            }
            
        } catch (SQLException e) {
            logger.error("Error finding users by department: {}", department, e);
        }
        
        return users;
    }
    
    @Override
    public List<User> findByRole(User.UserRole role) {
        String sql = "SELECT * FROM users WHERE role = ? ORDER BY full_name";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, role.name());
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                User user = mapResultSetToUser(rs);
                users.add(user);
            }
            
        } catch (SQLException e) {
            logger.error("Error finding users by role: {}", role, e);
        }
        
        return users;
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                User user = mapResultSetToUser(rs);
                return Optional.of(user);
            }
            
        } catch (SQLException e) {
            logger.error("Error finding user by email: {}", email, e);
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<User> getActiveUsers() {
        String sql = "SELECT * FROM users WHERE is_active = TRUE ORDER BY full_name";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                User user = mapResultSetToUser(rs);
                users.add(user);
            }
            
        } catch (SQLException e) {
            logger.error("Error retrieving active users", e);
        }
        
        return users;
    }
    
    @Override
    public int getTotalUserCount() {
        String sql = "SELECT COUNT(*) FROM users";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            logger.error("Error getting total user count", e);
        }
        
        return 0;
    }
    
    @Override
    public int getUserCountByRole(User.UserRole role) {
        String sql = "SELECT COUNT(*) FROM users WHERE role = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, role.name());
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            logger.error("Error getting user count by role: {}", role, e);
        }
        
        return 0;
    }
    
    /**
     * Map ResultSet thành User object
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        
        user.setUserId(rs.getString("user_id"));
        user.setUsername(rs.getString("username"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setDepartment(rs.getString("department"));
        user.setRole(User.UserRole.valueOf(rs.getString("role")));
        user.setActive(rs.getBoolean("is_active"));
        
        // Xử lý timestamp
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return user;
    }
} 