package com.mycompany.device.dao.impl;

import com.mycompany.device.dao.CapPhatDAO;
import com.mycompany.device.model.CapPhat;
import com.mycompany.device.model.YeuCau;
import com.mycompany.device.util.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * MySQL implementation của CapPhatDAO
 * @author Kim Ngan - DAO Implementation Layer
 */
public class CapPhatDAOMySQLImpl implements CapPhatDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(CapPhatDAOMySQLImpl.class);
    
    @Override
    public boolean createCapPhat(CapPhat capPhat) {
        String sql = "INSERT INTO CapPhat (yeuCauId, ngayCap, ngayTra, tinhTrangTra, ghiChu) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setLong(1, capPhat.getYeuCauId());
            stmt.setTimestamp(2, Timestamp.valueOf(capPhat.getNgayCap()));
            stmt.setTimestamp(3, capPhat.getNgayTra() != null ? Timestamp.valueOf(capPhat.getNgayTra()) : null);
            stmt.setString(4, capPhat.getTinhTrangTra() != null ? capPhat.getTinhTrangTra().name() : null);
            stmt.setString(5, capPhat.getGhiChu());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        capPhat.setId(generatedKeys.getLong(1));
                    }
                }
                logger.info("Tạo cấp phát thành công: ID={}, YeuCau ID={}", capPhat.getId(), capPhat.getYeuCauId());
                return true;
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi khi tạo cấp phát", e);
        }
        
        return false;
    }
    
    @Override
    public boolean updateCapPhat(CapPhat capPhat) {
        String sql = "UPDATE CapPhat SET yeuCauId=?, ngayCap=?, ngayTra=?, tinhTrangTra=?, ghiChu=? WHERE id=?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, capPhat.getYeuCauId());
            stmt.setTimestamp(2, Timestamp.valueOf(capPhat.getNgayCap()));
            stmt.setTimestamp(3, capPhat.getNgayTra() != null ? Timestamp.valueOf(capPhat.getNgayTra()) : null);
            stmt.setString(4, capPhat.getTinhTrangTra() != null ? capPhat.getTinhTrangTra().name() : null);
            stmt.setString(5, capPhat.getGhiChu());
            stmt.setLong(6, capPhat.getId());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                logger.info("Cập nhật cấp phát thành công: ID={}", capPhat.getId());
                return true;
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi khi cập nhật cấp phát: ID={}", capPhat.getId(), e);
        }
        
        return false;
    }
    
    @Override
    public boolean deleteCapPhat(Long capPhatId) {
        String sql = "DELETE FROM CapPhat WHERE id=?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, capPhatId);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                logger.info("Xóa cấp phát thành công: ID={}", capPhatId);
                return true;
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi khi xóa cấp phát: ID={}", capPhatId, e);
        }
        
        return false;
    }
    
    @Override
    public Optional<CapPhat> findCapPhatById(Long capPhatId) {
        String sql = "SELECT * FROM CapPhat WHERE id=?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, capPhatId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCapPhat(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi khi tìm cấp phát: ID={}", capPhatId, e);
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<CapPhat> getAllCapPhat() {
        String sql = "SELECT * FROM CapPhat ORDER BY ngayCap DESC";
        
        List<CapPhat> result = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                result.add(mapResultSetToCapPhat(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy danh sách cấp phát", e);
        }
        
        return result;
    }
    
    @Override
    public List<CapPhat> findCapPhatByYeuCau(Long yeuCauId) {
        String sql = "SELECT * FROM CapPhat WHERE yeuCauId=? ORDER BY ngayCap DESC";
        
        List<CapPhat> result = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, yeuCauId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(mapResultSetToCapPhat(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi khi tìm cấp phát theo yêu cầu: ID={}", yeuCauId, e);
        }
        
        return result;
    }
    
    @Override
    public List<CapPhat> findActiveCapPhat() {
        String sql = "SELECT * FROM CapPhat WHERE ngayTra IS NULL ORDER BY ngayCap DESC";
        
        List<CapPhat> result = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                result.add(mapResultSetToCapPhat(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy danh sách cấp phát đang hoạt động", e);
        }
        
        return result;
    }
    
    @Override
    public List<CapPhat> findReturnedCapPhat() {
        String sql = "SELECT * FROM CapPhat WHERE ngayTra IS NOT NULL ORDER BY ngayTra DESC";
        
        List<CapPhat> result = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                result.add(mapResultSetToCapPhat(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy danh sách cấp phát đã trả", e);
        }
        
        return result;
    }
    
    @Override
    public boolean isThietBiBeingAllocated(Long thietBiId) {
        String sql = "SELECT COUNT(*) FROM CapPhat cp " +
                    "JOIN YeuCau yc ON cp.yeuCauId = yc.id " +
                    "WHERE yc.thietBiId = ? AND cp.ngayTra IS NULL";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, thietBiId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi khi kiểm tra thiết bị đang được cấp phát: ID={}", thietBiId, e);
        }
        
        return false;
    }
    
    @Override
    public Optional<CapPhat> findCurrentCapPhatByThietBi(Long thietBiId) {
        String sql = "SELECT cp.* FROM CapPhat cp " +
                    "JOIN YeuCau yc ON cp.yeuCauId = yc.id " +
                    "WHERE yc.thietBiId = ? AND cp.ngayTra IS NULL " +
                    "ORDER BY cp.ngayCap DESC LIMIT 1";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, thietBiId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCapPhat(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi khi tìm cấp phát hiện tại của thiết bị: ID={}", thietBiId, e);
        }
        
        return Optional.empty();
    }
    
    @Override
    public int countCapPhat() {
        String sql = "SELECT COUNT(*) FROM CapPhat";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi khi đếm số cấp phát", e);
        }
        
        return 0;
    }
    
    @Override
    public int countActiveCapPhat() {
        String sql = "SELECT COUNT(*) FROM CapPhat WHERE ngayTra IS NULL";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi khi đếm số cấp phát đang hoạt động", e);
        }
        
        return 0;
    }
    
    @Override
    public int countReturnedCapPhat() {
        String sql = "SELECT COUNT(*) FROM CapPhat WHERE ngayTra IS NOT NULL";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi khi đếm số cấp phát đã trả", e);
        }
        
        return 0;
    }
    
    @Override
    public boolean updateReturnInfo(Long capPhatId, CapPhat.TinhTrangTra tinhTrangTra, String ghiChu) {
        String sql = "UPDATE CapPhat SET ngayTra=?, tinhTrangTra=?, ghiChu=? WHERE id=?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(2, tinhTrangTra.name());
            stmt.setString(3, ghiChu);
            stmt.setLong(4, capPhatId);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                logger.info("Cập nhật thông tin trả thiết bị thành công: ID={}", capPhatId);
                return true;
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi khi cập nhật thông tin trả thiết bị: ID={}", capPhatId, e);
        }
        
        return false;
    }
    
    /**
     * Map ResultSet thành CapPhat object
     */
    private CapPhat mapResultSetToCapPhat(ResultSet rs) throws SQLException {
        CapPhat capPhat = new CapPhat();
        capPhat.setId(rs.getLong("id"));
        capPhat.setYeuCauId(rs.getLong("yeuCauId"));
        
        Timestamp ngayCap = rs.getTimestamp("ngayCap");
        if (ngayCap != null) {
            capPhat.setNgayCap(ngayCap.toLocalDateTime());
        }
        
        Timestamp ngayTra = rs.getTimestamp("ngayTra");
        if (ngayTra != null) {
            capPhat.setNgayTra(ngayTra.toLocalDateTime());
        }
        
        String tinhTrangTraStr = rs.getString("tinhTrangTra");
        if (tinhTrangTraStr != null) {
            capPhat.setTinhTrangTra(CapPhat.TinhTrangTra.valueOf(tinhTrangTraStr));
        }
        
        capPhat.setGhiChu(rs.getString("ghiChu"));
        
        return capPhat;
    }
}
