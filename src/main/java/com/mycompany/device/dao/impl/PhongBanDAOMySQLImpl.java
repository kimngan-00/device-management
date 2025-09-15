package com.mycompany.device.dao.impl;

import com.mycompany.device.dao.PhongBanDAO;
import com.mycompany.device.model.PhongBan;
import com.mycompany.device.util.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation của PhongBanDAO sử dụng MySQL database
 * @author Kim Ngan - DAO MySQL Implementation Layer
 */
public class PhongBanDAOMySQLImpl implements PhongBanDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(PhongBanDAOMySQLImpl.class);
    
    private final DatabaseConnection dbConnection;
    
    // SQL queries
    private static final String INSERT_PHONGBAN = 
        "INSERT INTO PhongBan (maPhongBan, tenPhongBan, moTa) VALUES (?, ?, ?)";
    
    private static final String UPDATE_PHONGBAN = 
        "UPDATE PhongBan SET tenPhongBan = ?, moTa = ? WHERE maPhongBan = ?";
    
    private static final String DELETE_PHONGBAN = 
        "DELETE FROM PhongBan WHERE maPhongBan = ?";
    
    private static final String SELECT_BY_MA = 
        "SELECT maPhongBan, tenPhongBan, moTa FROM PhongBan WHERE maPhongBan = ?";
    
    private static final String SELECT_ALL = 
        "SELECT maPhongBan, tenPhongBan, moTa FROM PhongBan ORDER BY maPhongBan";
    
    private static final String SEARCH_BY_TEN = 
        "SELECT maPhongBan, tenPhongBan, moTa FROM PhongBan WHERE tenPhongBan LIKE ? ORDER BY maPhongBan";
    
    private static final String EXISTS_PHONGBAN = 
        "SELECT COUNT(*) FROM PhongBan WHERE maPhongBan = ?";
    
    private static final String COUNT_PHONGBAN = 
        "SELECT COUNT(*) FROM PhongBan";
    
    public PhongBanDAOMySQLImpl() {
        this.dbConnection = DatabaseConnection.getInstance();
        logger.info("Khởi tạo PhongBanDAOMySQLImpl với MySQL database");
    }
    
    @Override
    public boolean createPhongBan(PhongBan phongBan) {
        if (phongBan == null || phongBan.getMaPhongBan() == null || phongBan.getMaPhongBan().trim().isEmpty()) {
            logger.warn("Không thể tạo phòng ban: thông tin không hợp lệ");
            return false;
        }
        
        String maPhongBan = phongBan.getMaPhongBan().trim();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_PHONGBAN)) {
            
            stmt.setString(1, maPhongBan);
            stmt.setString(2, phongBan.getTenPhongBan());
            stmt.setString(3, phongBan.getMoTa());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                logger.info("Tạo phòng ban thành công: {}", maPhongBan);
                return true;
            } else {
                logger.warn("Không có dòng nào được tạo cho phòng ban: {}", maPhongBan);
                return false;
            }
            
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // Duplicate entry
                logger.warn("Phòng ban với mã '{}' đã tồn tại", maPhongBan);
            } else {
                logger.error("Lỗi SQL khi tạo phòng ban: {}", e.getMessage());
            }
            return false;
        } catch (Exception e) {
            logger.error("Lỗi khi tạo phòng ban: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean updatePhongBan(PhongBan phongBan) {
        if (phongBan == null || phongBan.getMaPhongBan() == null || phongBan.getMaPhongBan().trim().isEmpty()) {
            logger.warn("Không thể cập nhật phòng ban: thông tin không hợp lệ");
            return false;
        }
        
        String maPhongBan = phongBan.getMaPhongBan().trim();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_PHONGBAN)) {
            
            stmt.setString(1, phongBan.getTenPhongBan());
            stmt.setString(2, phongBan.getMoTa());
            stmt.setString(3, maPhongBan);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                logger.info("Cập nhật phòng ban thành công: {}", maPhongBan);
                return true;
            } else {
                logger.warn("Không tìm thấy phòng ban để cập nhật: {}", maPhongBan);
                return false;
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi SQL khi cập nhật phòng ban: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Lỗi khi cập nhật phòng ban: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean deletePhongBan(String maPhongBan) {
        if (maPhongBan == null || maPhongBan.trim().isEmpty()) {
            logger.warn("Không thể xóa phòng ban: mã phòng ban không hợp lệ");
            return false;
        }
        
        String ma = maPhongBan.trim();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_PHONGBAN)) {
            
            stmt.setString(1, ma);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                logger.info("Xóa phòng ban thành công: {}", ma);
                return true;
            } else {
                logger.warn("Không tìm thấy phòng ban để xóa: {}", ma);
                return false;
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi SQL khi xóa phòng ban: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Lỗi khi xóa phòng ban: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public Optional<PhongBan> findPhongBanByMa(String maPhongBan) {
        if (maPhongBan == null || maPhongBan.trim().isEmpty()) {
            logger.warn("Không thể tìm phòng ban: mã phòng ban không hợp lệ");
            return Optional.empty();
        }
        
        String ma = maPhongBan.trim();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_MA)) {
            
            stmt.setString(1, ma);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    PhongBan phongBan = mapResultSetToPhongBan(rs);
                    logger.debug("Tìm thấy phòng ban: {}", ma);
                    return Optional.of(phongBan);
                } else {
                    logger.debug("Không tìm thấy phòng ban: {}", ma);
                    return Optional.empty();
                }
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi SQL khi tìm phòng ban: {}", e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm phòng ban: {}", e.getMessage());
            return Optional.empty();
        }
    }
    
    @Override
    public List<PhongBan> getAllPhongBan() {
        List<PhongBan> result = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                PhongBan phongBan = mapResultSetToPhongBan(rs);
                result.add(phongBan);
            }
            
            logger.debug("Lấy danh sách tất cả phòng ban: {} phòng ban", result.size());
            
        } catch (SQLException e) {
            logger.error("Lỗi SQL khi lấy danh sách phòng ban: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Lỗi khi lấy danh sách phòng ban: {}", e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public List<PhongBan> searchPhongBanByTen(String tenPhongBan) {
        List<PhongBan> result = new ArrayList<>();
        
        if (tenPhongBan == null || tenPhongBan.trim().isEmpty()) {
            logger.warn("Không thể tìm kiếm phòng ban: tên phòng ban không hợp lệ");
            return result;
        }
        
        String ten = "%" + tenPhongBan.trim() + "%";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SEARCH_BY_TEN)) {
            
            stmt.setString(1, ten);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PhongBan phongBan = mapResultSetToPhongBan(rs);
                    result.add(phongBan);
                }
            }
            
            logger.debug("Tìm kiếm phòng ban theo tên '{}': {} kết quả", tenPhongBan, result.size());
            
        } catch (SQLException e) {
            logger.error("Lỗi SQL khi tìm kiếm phòng ban: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Lỗi khi tìm kiếm phòng ban: {}", e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public boolean existsPhongBan(String maPhongBan) {
        if (maPhongBan == null || maPhongBan.trim().isEmpty()) {
            return false;
        }
        
        String ma = maPhongBan.trim();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(EXISTS_PHONGBAN)) {
            
            stmt.setString(1, ma);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    boolean exists = count > 0;
                    logger.debug("Kiểm tra tồn tại phòng ban '{}': {}", ma, exists);
                    return exists;
                }
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi SQL khi kiểm tra tồn tại phòng ban: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Lỗi khi kiểm tra tồn tại phòng ban: {}", e.getMessage());
        }
        
        return false;
    }
    
    @Override
    public int countPhongBan() {
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(COUNT_PHONGBAN);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                int count = rs.getInt(1);
                logger.debug("Đếm số lượng phòng ban: {}", count);
                return count;
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi SQL khi đếm phòng ban: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Lỗi khi đếm phòng ban: {}", e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Map ResultSet thành PhongBan object
     */
    private PhongBan mapResultSetToPhongBan(ResultSet rs) throws SQLException {
        PhongBan phongBan = new PhongBan();
        phongBan.setMaPhongBan(rs.getString("maPhongBan"));
        phongBan.setTenPhongBan(rs.getString("tenPhongBan"));
        phongBan.setMoTa(rs.getString("moTa"));
        return phongBan;
    }
}
