package com.mycompany.device.dao.impl;

import com.mycompany.device.dao.NhanVienDAO;
import com.mycompany.device.model.NhanVien;
import com.mycompany.device.util.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation của NhanVienDAO sử dụng MySQL database
 * @author Kim Ngan - DAO MySQL Implementation Layer
 */
public class NhanVienDAOMySQLImpl implements NhanVienDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(NhanVienDAOMySQLImpl.class);
    
    private final DatabaseConnection dbConnection;
    
    // SQL queries
    private static final String INSERT_NHANVIEN = 
        "INSERT INTO NhanVien (maNhanVien, tenNhanVien, email, password, soDienThoai, role, maPhongBan, ngayTao) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String UPDATE_NHANVIEN = 
        "UPDATE NhanVien SET tenNhanVien = ?, email = ?, password = ?, soDienThoai = ?, role = ?, maPhongBan = ? WHERE maNhanVien = ?";
    
    private static final String DELETE_NHANVIEN = 
        "DELETE FROM NhanVien WHERE maNhanVien = ?";
    
    private static final String SELECT_BY_MA = 
        "SELECT maNhanVien, tenNhanVien, email, password, soDienThoai, role, maPhongBan, ngayTao FROM NhanVien WHERE maNhanVien = ?";
    
    private static final String SELECT_BY_EMAIL = 
        "SELECT maNhanVien, tenNhanVien, email, password, soDienThoai, role, maPhongBan, ngayTao FROM NhanVien WHERE email = ?";
    
    private static final String SELECT_ALL = 
        "SELECT maNhanVien, tenNhanVien, email, password, soDienThoai, role, maPhongBan, ngayTao FROM NhanVien ORDER BY maNhanVien";
    
    private static final String SEARCH_BY_TEN = 
        "SELECT maNhanVien, tenNhanVien, email, password, soDienThoai, role, maPhongBan, ngayTao FROM NhanVien WHERE tenNhanVien LIKE ? ORDER BY maNhanVien";
    
    private static final String SELECT_BY_PHONGBAN = 
        "SELECT maNhanVien, tenNhanVien, email, password, soDienThoai, role, maPhongBan, ngayTao FROM NhanVien WHERE maPhongBan = ? ORDER BY maNhanVien";
    
    private static final String SELECT_BY_ROLE = 
        "SELECT maNhanVien, tenNhanVien, email, password, soDienThoai, role, maPhongBan, ngayTao FROM NhanVien WHERE role = ? ORDER BY maNhanVien";
    
    private static final String EXISTS_NHANVIEN = 
        "SELECT COUNT(*) FROM NhanVien WHERE maNhanVien = ?";
    
    private static final String EXISTS_EMAIL = 
        "SELECT COUNT(*) FROM NhanVien WHERE email = ?";
    
    private static final String COUNT_NHANVIEN = 
        "SELECT COUNT(*) FROM NhanVien";
    
    private static final String COUNT_BY_PHONGBAN = 
        "SELECT COUNT(*) FROM NhanVien WHERE maPhongBan = ?";
    
    public NhanVienDAOMySQLImpl() {
        this.dbConnection = DatabaseConnection.getInstance();
        logger.info("Khởi tạo NhanVienDAOMySQLImpl với MySQL database");
    }
    
    @Override
    public boolean createNhanVien(NhanVien nhanVien) {
        if (nhanVien == null || nhanVien.getMaNhanVien() == null || nhanVien.getMaNhanVien().trim().isEmpty()) {
            logger.warn("Không thể tạo nhân viên: thông tin không hợp lệ");
            return false;
        }
        
        String maNhanVien = nhanVien.getMaNhanVien().trim();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_NHANVIEN)) {
            
            stmt.setString(1, maNhanVien);
            stmt.setString(2, nhanVien.getTenNhanVien());
            stmt.setString(3, nhanVien.getEmail());
            stmt.setString(4, nhanVien.getPassword());
            stmt.setString(5, nhanVien.getSoDienThoai());
            stmt.setString(6, nhanVien.getRole().name());
            stmt.setString(7, nhanVien.getMaPhongBan());
            stmt.setTimestamp(8, Timestamp.valueOf(nhanVien.getNgayTao()));
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                logger.info("Tạo nhân viên thành công: {}", maNhanVien);
                return true;
            } else {
                logger.warn("Không có dòng nào được tạo cho nhân viên: {}", maNhanVien);
                return false;
            }
            
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // Duplicate entry
                logger.warn("Nhân viên với mã '{}' hoặc email '{}' đã tồn tại", maNhanVien, nhanVien.getEmail());
            } else {
                logger.error("Lỗi SQL khi tạo nhân viên: {}", e.getMessage());
            }
            return false;
        } catch (Exception e) {
            logger.error("Lỗi khi tạo nhân viên: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean updateNhanVien(NhanVien nhanVien) {
        if (nhanVien == null || nhanVien.getMaNhanVien() == null || nhanVien.getMaNhanVien().trim().isEmpty()) {
            logger.warn("Không thể cập nhật nhân viên: thông tin không hợp lệ");
            return false;
        }
        
        String maNhanVien = nhanVien.getMaNhanVien().trim();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_NHANVIEN)) {
            
            stmt.setString(1, nhanVien.getTenNhanVien());
            stmt.setString(2, nhanVien.getEmail());
            stmt.setString(3, nhanVien.getPassword());
            stmt.setString(4, nhanVien.getSoDienThoai());
            stmt.setString(5, nhanVien.getRole().name());
            stmt.setString(6, nhanVien.getMaPhongBan());
            stmt.setString(7, maNhanVien);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                logger.info("Cập nhật nhân viên thành công: {}", maNhanVien);
                return true;
            } else {
                logger.warn("Không tìm thấy nhân viên để cập nhật: {}", maNhanVien);
                return false;
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi SQL khi cập nhật nhân viên: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Lỗi khi cập nhật nhân viên: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean deleteNhanVien(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.trim().isEmpty()) {
            logger.warn("Không thể xóa nhân viên: mã nhân viên không hợp lệ");
            return false;
        }
        
        String ma = maNhanVien.trim();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_NHANVIEN)) {
            
            stmt.setString(1, ma);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                logger.info("Xóa nhân viên thành công: {}", ma);
                return true;
            } else {
                logger.warn("Không tìm thấy nhân viên để xóa: {}", ma);
                return false;
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi SQL khi xóa nhân viên: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Lỗi khi xóa nhân viên: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public Optional<NhanVien> findNhanVienByMa(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.trim().isEmpty()) {
            logger.warn("Không thể tìm nhân viên: mã nhân viên không hợp lệ");
            return Optional.empty();
        }
        
        String ma = maNhanVien.trim();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_MA)) {
            
            stmt.setString(1, ma);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    NhanVien nhanVien = mapResultSetToNhanVien(rs);
                    logger.debug("Tìm thấy nhân viên: {}", ma);
                    return Optional.of(nhanVien);
                } else {
                    logger.debug("Không tìm thấy nhân viên: {}", ma);
                    return Optional.empty();
                }
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi SQL khi tìm nhân viên: {}", e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm nhân viên: {}", e.getMessage());
            return Optional.empty();
        }
    }
    
    @Override
    public Optional<NhanVien> findNhanVienByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            logger.warn("Không thể tìm nhân viên: email không hợp lệ");
            return Optional.empty();
        }
        
        String emailTrim = email.trim();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_EMAIL)) {
            
            stmt.setString(1, emailTrim);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    NhanVien nhanVien = mapResultSetToNhanVien(rs);
                    logger.debug("Tìm thấy nhân viên theo email: {}", emailTrim);
                    return Optional.of(nhanVien);
                } else {
                    logger.debug("Không tìm thấy nhân viên theo email: {}", emailTrim);
                    return Optional.empty();
                }
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi SQL khi tìm nhân viên theo email: {}", e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm nhân viên theo email: {}", e.getMessage());
            return Optional.empty();
        }
    }
    
    @Override
    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> result = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                NhanVien nhanVien = mapResultSetToNhanVien(rs);
                result.add(nhanVien);
            }
            
            logger.debug("Lấy danh sách tất cả nhân viên: {} nhân viên", result.size());
            
        } catch (SQLException e) {
            logger.error("Lỗi SQL khi lấy danh sách nhân viên: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Lỗi khi lấy danh sách nhân viên: {}", e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public List<NhanVien> searchNhanVienByTen(String tenNhanVien) {
        List<NhanVien> result = new ArrayList<>();
        
        if (tenNhanVien == null || tenNhanVien.trim().isEmpty()) {
            logger.warn("Không thể tìm kiếm nhân viên: tên nhân viên không hợp lệ");
            return result;
        }
        
        String ten = "%" + tenNhanVien.trim() + "%";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SEARCH_BY_TEN)) {
            
            stmt.setString(1, ten);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    NhanVien nhanVien = mapResultSetToNhanVien(rs);
                    result.add(nhanVien);
                }
            }
            
            logger.debug("Tìm kiếm nhân viên theo tên '{}': {} kết quả", tenNhanVien, result.size());
            
        } catch (SQLException e) {
            logger.error("Lỗi SQL khi tìm kiếm nhân viên: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Lỗi khi tìm kiếm nhân viên: {}", e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public List<NhanVien> findNhanVienByPhongBan(String maPhongBan) {
        List<NhanVien> result = new ArrayList<>();
        
        if (maPhongBan == null || maPhongBan.trim().isEmpty()) {
            logger.warn("Không thể tìm nhân viên: mã phòng ban không hợp lệ");
            return result;
        }
        
        String ma = maPhongBan.trim();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_PHONGBAN)) {
            
            stmt.setString(1, ma);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    NhanVien nhanVien = mapResultSetToNhanVien(rs);
                    result.add(nhanVien);
                }
            }
            
            logger.debug("Tìm nhân viên theo phòng ban '{}': {} kết quả", maPhongBan, result.size());
            
        } catch (SQLException e) {
            logger.error("Lỗi SQL khi tìm nhân viên theo phòng ban: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Lỗi khi tìm nhân viên theo phòng ban: {}", e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public List<NhanVien> findNhanVienByRole(NhanVien.NhanVienRole role) {
        List<NhanVien> result = new ArrayList<>();
        
        if (role == null) {
            logger.warn("Không thể tìm nhân viên: vai trò không hợp lệ");
            return result;
        }
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ROLE)) {
            
            stmt.setString(1, role.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    NhanVien nhanVien = mapResultSetToNhanVien(rs);
                    result.add(nhanVien);
                }
            }
            
            logger.debug("Tìm nhân viên theo vai trò '{}': {} kết quả", role, result.size());
            
        } catch (SQLException e) {
            logger.error("Lỗi SQL khi tìm nhân viên theo vai trò: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Lỗi khi tìm nhân viên theo vai trò: {}", e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public boolean existsNhanVien(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.trim().isEmpty()) {
            return false;
        }
        
        String ma = maNhanVien.trim();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(EXISTS_NHANVIEN)) {
            
            stmt.setString(1, ma);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    boolean exists = count > 0;
                    logger.debug("Kiểm tra tồn tại nhân viên '{}': {}", ma, exists);
                    return exists;
                }
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi SQL khi kiểm tra tồn tại nhân viên: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Lỗi khi kiểm tra tồn tại nhân viên: {}", e.getMessage());
        }
        
        return false;
    }
    
    @Override
    public boolean existsEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        String emailTrim = email.trim();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(EXISTS_EMAIL)) {
            
            stmt.setString(1, emailTrim);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    boolean exists = count > 0;
                    logger.debug("Kiểm tra tồn tại email '{}': {}", emailTrim, exists);
                    return exists;
                }
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi SQL khi kiểm tra tồn tại email: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Lỗi khi kiểm tra tồn tại email: {}", e.getMessage());
        }
        
        return false;
    }
    
    @Override
    public int countNhanVien() {
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(COUNT_NHANVIEN);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                int count = rs.getInt(1);
                logger.debug("Đếm số lượng nhân viên: {}", count);
                return count;
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi SQL khi đếm nhân viên: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Lỗi khi đếm nhân viên: {}", e.getMessage());
        }
        
        return 0;
    }
    
    @Override
    public int countNhanVienByPhongBan(String maPhongBan) {
        if (maPhongBan == null || maPhongBan.trim().isEmpty()) {
            return 0;
        }
        
        String ma = maPhongBan.trim();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(COUNT_BY_PHONGBAN)) {
            
            stmt.setString(1, ma);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    logger.debug("Đếm số lượng nhân viên theo phòng ban '{}': {}", maPhongBan, count);
                    return count;
                }
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi SQL khi đếm nhân viên theo phòng ban: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Lỗi khi đếm nhân viên theo phòng ban: {}", e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Map ResultSet thành NhanVien object
     */
    private NhanVien mapResultSetToNhanVien(ResultSet rs) throws SQLException {
        NhanVien nhanVien = new NhanVien();
        nhanVien.setMaNhanVien(rs.getString("maNhanVien"));
        nhanVien.setTenNhanVien(rs.getString("tenNhanVien"));
        nhanVien.setEmail(rs.getString("email"));
        nhanVien.setPassword(rs.getString("password"));
        nhanVien.setSoDienThoai(rs.getString("soDienThoai"));
        nhanVien.setRole(NhanVien.NhanVienRole.valueOf(rs.getString("role")));
        nhanVien.setMaPhongBan(rs.getString("maPhongBan"));
        
        Timestamp ngayTao = rs.getTimestamp("ngayTao");
        if (ngayTao != null) {
            nhanVien.setNgayTao(ngayTao.toLocalDateTime());
        }
        
        return nhanVien;
    }
}
