package com.mycompany.device.dao.impl;

import com.mycompany.device.dao.YeuCauDAO;
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
 * MySQL implementation của YeuCauDAO
 * @author Kim Ngan - DAO Implementation Layer
 */
public class YeuCauDAOMySQLImpl implements YeuCauDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(YeuCauDAOMySQLImpl.class);
    
    @Override
    public boolean createYeuCau(YeuCau yeuCau) {
        String sql = "INSERT INTO yeu_cau (thiet_bi_id, nhan_vien_id, trang_thai, ly_do, ngay_tao, ngay_cap_nhat) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setLong(1, yeuCau.getThietBiId());
            stmt.setLong(2, yeuCau.getNhanVienId());
            stmt.setString(3, yeuCau.getTrangThai().name());
            stmt.setString(4, yeuCau.getLyDo());
            stmt.setTimestamp(5, Timestamp.valueOf(yeuCau.getNgayTao()));
            stmt.setTimestamp(6, Timestamp.valueOf(yeuCau.getNgayCapNhat()));
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        yeuCau.setId(generatedKeys.getLong(1));
                    }
                }
                logger.info("Tạo yêu cầu thành công: ID={}", yeuCau.getId());
                return true;
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi khi tạo yêu cầu", e);
        }
        
        return false;
    }
    
    @Override
    public boolean updateYeuCau(YeuCau yeuCau) {
        String sql = "UPDATE yeu_cau SET thiet_bi_id=?, nhan_vien_id=?, trang_thai=?, ly_do=?, ngay_cap_nhat=? WHERE id=?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, yeuCau.getThietBiId());
            stmt.setLong(2, yeuCau.getNhanVienId());
            stmt.setString(3, yeuCau.getTrangThai().name());
            stmt.setString(4, yeuCau.getLyDo());
            stmt.setTimestamp(5, Timestamp.valueOf(yeuCau.getNgayCapNhat()));
            stmt.setLong(6, yeuCau.getId());
            
            int rowsAffected = stmt.executeUpdate();
            logger.info("Cập nhật yêu cầu: ID={}, Rows affected={}", yeuCau.getId(), rowsAffected);
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            logger.error("Lỗi khi cập nhật yêu cầu: ID={}", yeuCau.getId(), e);
        }
        
        return false;
    }
    
    @Override
    public boolean deleteYeuCau(Long yeuCauId) {
        String sql = "DELETE FROM yeu_cau WHERE id=?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, yeuCauId);
            int rowsAffected = stmt.executeUpdate();
            logger.info("Xóa yêu cầu: ID={}, Rows affected={}", yeuCauId, rowsAffected);
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            logger.error("Lỗi khi xóa yêu cầu: ID={}", yeuCauId, e);
        }
        
        return false;
    }
    
    @Override
    public Optional<YeuCau> findYeuCauById(Long yeuCauId) {
        String sql = "SELECT * FROM yeu_cau WHERE id=?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, yeuCauId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToYeuCau(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi khi tìm yêu cầu: ID={}", yeuCauId, e);
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<YeuCau> getAllYeuCau() {
        String sql = "SELECT * FROM yeu_cau ORDER BY ngay_tao DESC";
        List<YeuCau> yeuCauList = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                yeuCauList.add(mapResultSetToYeuCau(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy danh sách yêu cầu", e);
        }
        
        return yeuCauList;
    }
    
    @Override
    public List<YeuCau> findYeuCauByThietBi(Long thietBiId) {
        String sql = "SELECT * FROM yeu_cau WHERE thiet_bi_id=? ORDER BY ngay_tao DESC";
        return findYeuCauByCondition(sql, thietBiId);
    }
    
    @Override
    public List<YeuCau> findYeuCauByNhanVien(Long nhanVienId) {
        String sql = "SELECT * FROM yeu_cau WHERE nhan_vien_id=? ORDER BY ngay_tao DESC";
        return findYeuCauByCondition(sql, nhanVienId);
    }
    
    @Override
    public List<YeuCau> findYeuCauByTrangThai(YeuCau.TrangThaiYeuCau trangThai) {
        String sql = "SELECT * FROM yeu_cau WHERE trang_thai=? ORDER BY ngay_tao DESC";
        return findYeuCauByCondition(sql, trangThai.name());
    }
    
    @Override
    public List<YeuCau> searchYeuCauByLyDo(String lyDo) {
        String sql = "SELECT * FROM yeu_cau WHERE ly_do LIKE ? ORDER BY ngay_tao DESC";
        return findYeuCauByCondition(sql, "%" + lyDo + "%");
    }
    
    @Override
    public boolean existsYeuCau(Long yeuCauId) {
        String sql = "SELECT COUNT(*) FROM yeu_cau WHERE id=?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, yeuCauId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi khi kiểm tra yêu cầu tồn tại: ID={}", yeuCauId, e);
        }
        
        return false;
    }
    
    @Override
    public int countYeuCau() {
        String sql = "SELECT COUNT(*) FROM yeu_cau";
        return executeCountQuery(sql);
    }
    
    @Override
    public int countYeuCauByTrangThai(YeuCau.TrangThaiYeuCau trangThai) {
        String sql = "SELECT COUNT(*) FROM yeu_cau WHERE trang_thai=?";
        return executeCountQuery(sql, trangThai.name());
    }
    
    @Override
    public int countYeuCauByNhanVien(Long nhanVienId) {
        String sql = "SELECT COUNT(*) FROM yeu_cau WHERE nhan_vien_id=?";
        return executeCountQuery(sql, nhanVienId);
    }
    
    private List<YeuCau> findYeuCauByCondition(String sql, Object... params) {
        List<YeuCau> yeuCauList = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    yeuCauList.add(mapResultSetToYeuCau(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi khi tìm yêu cầu với điều kiện: {}", sql, e);
        }
        
        return yeuCauList;
    }
    
    private int executeCountQuery(String sql, Object... params) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi khi đếm yêu cầu: {}", sql, e);
        }
        
        return 0;
    }
    
    private YeuCau mapResultSetToYeuCau(ResultSet rs) throws SQLException {
        YeuCau yeuCau = new YeuCau();
        yeuCau.setId(rs.getLong("id"));
        yeuCau.setThietBiId(rs.getLong("thiet_bi_id"));
        yeuCau.setNhanVienId(rs.getLong("nhan_vien_id"));
        yeuCau.setTrangThai(YeuCau.TrangThaiYeuCau.valueOf(rs.getString("trang_thai")));
        yeuCau.setLyDo(rs.getString("ly_do"));
        yeuCau.setNgayTao(rs.getTimestamp("ngay_tao").toLocalDateTime());
        yeuCau.setNgayCapNhat(rs.getTimestamp("ngay_cap_nhat").toLocalDateTime());
        return yeuCau;
    }
} 