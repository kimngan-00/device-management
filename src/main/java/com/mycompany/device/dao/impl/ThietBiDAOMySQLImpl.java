package com.mycompany.device.dao.impl;

import com.mycompany.device.dao.ThietBiDAO;
import com.mycompany.device.model.ThietBi;
import com.mycompany.device.model.ThietBi.TrangThaiThietBi;
import com.mycompany.device.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

public class ThietBiDAOMySQLImpl implements ThietBiDAO {

    // (LƯU Ý: Bạn cần đảm bảo đã tạo class DatabaseConnection và các phương thức DB cơ bản)

    private static final String SELECT_ALL_SQL = 
        "SELECT id, soSerial, loaiId, trangThai, ngayMua, giaMua, ghiChu, createdAt, updatedAt FROM ThietBi";
    private static final String SELECT_BY_ID_SQL = SELECT_ALL_SQL + " WHERE id = ?";
    private static final String SELECT_BY_SERIAL_SQL = SELECT_ALL_SQL + " WHERE soSerial = ?";
    private static final String INSERT_SQL = 
        "INSERT INTO ThietBi (soSerial, loaiId, trangThai, ngayMua, giaMua, ghiChu) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = 
        "UPDATE ThietBi SET soSerial = ?, loaiId = ?, trangThai = ?, ngayMua = ?, giaMua = ?, ghiChu = ?, updatedAt = CURRENT_TIMESTAMP WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM ThietBi WHERE id = ?";

    private ThietBi mapResultSetToThietBi(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String soSerial = rs.getString("soSerial");
        Long loaiId = rs.getLong("loaiId");
        
        TrangThaiThietBi trangThai;
        try {
            trangThai = TrangThaiThietBi.valueOf(rs.getString("trangThai"));
        } catch (IllegalArgumentException e) {
            trangThai = TrangThaiThietBi.TON_KHO; 
        }
        
        Date ngayMuaSql = rs.getDate("ngayMua");
        BigDecimal giaMua = rs.getBigDecimal("giaMua");
        String ghiChu = rs.getString("ghiChu");
        Timestamp createdAtSql = rs.getTimestamp("createdAt");
        Timestamp updatedAtSql = rs.getTimestamp("updatedAt");
        
        LocalDate ngayMua = ngayMuaSql != null ? ngayMuaSql.toLocalDate() : null;
        LocalDateTime createdAt = createdAtSql != null ? createdAtSql.toLocalDateTime() : null;
        LocalDateTime updatedAt = updatedAtSql != null ? updatedAtSql.toLocalDateTime() : null;

        return new ThietBi(
            id, soSerial, loaiId, trangThai, ngayMua, 
            giaMua, ghiChu, createdAt, updatedAt
        );
    }
    
    private List<ThietBi> executeQuery(String sql, Object... params) throws SQLException {
        List<ThietBi> list = new ArrayList<>();
        // Giả định DatabaseConnection.getInstance().getConnection() tồn tại và hoạt động
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToThietBi(rs));
                }
            }
        }
        return list;
    }
    
    // =======================================================
    //          CÁC PHƯƠNG THỨC CRUD CƠ BẢN (KHÔNG ĐỔI)
    // =======================================================
    @Override
    public List<ThietBi> findAll() {
        try { return executeQuery(SELECT_ALL_SQL); } catch (SQLException e) { e.printStackTrace(); return new ArrayList<>(); }
    }
    @Override
    public ThietBi findById(Long id) {
        try { List<ThietBi> result = executeQuery(SELECT_BY_ID_SQL, id); return result.isEmpty() ? null : result.get(0); } catch (SQLException e) { e.printStackTrace(); return null; }
    }
    @Override
    public ThietBi findBySoSerial(String soSerial) {
        try { List<ThietBi> result = executeQuery(SELECT_BY_SERIAL_SQL, soSerial); return result.isEmpty() ? null : result.get(0); } catch (SQLException e) { e.printStackTrace(); return null; }
    }
    // (LƯU Ý: Bạn cần đảm bảo các method save, update, delete có logic thực thi đúng)
    @Override
    public boolean save(ThietBi thietBi) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL)) {
            pstmt.setString(1, thietBi.getSoSerial());
            pstmt.setLong(2, thietBi.getLoaiId());
            pstmt.setString(3, thietBi.getTrangThai().name());
            pstmt.setDate(4, thietBi.getNgayMua() != null ? Date.valueOf(thietBi.getNgayMua()) : null);
            pstmt.setBigDecimal(5, thietBi.getGiaMua());
            pstmt.setString(6, thietBi.getGhiChu());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    @Override
    public boolean update(ThietBi thietBi) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {
            pstmt.setString(1, thietBi.getSoSerial());
            pstmt.setLong(2, thietBi.getLoaiId());
            pstmt.setString(3, thietBi.getTrangThai().name());
            pstmt.setDate(4, thietBi.getNgayMua() != null ? Date.valueOf(thietBi.getNgayMua()) : null);
            pstmt.setBigDecimal(5, thietBi.getGiaMua());
            pstmt.setString(6, thietBi.getGhiChu());
            pstmt.setLong(7, thietBi.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    @Override
    public boolean delete(Long id) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)) {
            pstmt.setLong(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // =======================================================
    //          LOGIC TÌM KIẾM MỚI (SQL fix)
    // =======================================================
    @Override
    public List<ThietBi> search(String keyword, String searchType) {
        // Bảo vệ keyword: trim() và kiểm tra rỗng
        String trimmedKeyword = keyword.trim();
        if (trimmedKeyword.isEmpty() || "Tất cả".equals(searchType)) {
             // Nếu là Tất cả (và có keyword), logic sẽ chạy xuống dưới
             if (trimmedKeyword.isEmpty()) return findAll(); 
        }
        
        StringBuilder sql = new StringBuilder(SELECT_ALL_SQL).append(" WHERE 1=1 ");
        List<Object> params = new ArrayList<>();
        
        // Từ khóa dùng cho LIKE
        String likeKeyword = "%" + trimmedKeyword + "%";
        
        switch (searchType) {
            case "Số Serial":
                sql.append(" AND soSerial LIKE ?");
                params.add(likeKeyword);
                break;
            case "Loại ID":
                 try {
                    // Dùng Long.valueOf() để chuyển đổi SỐ. 
                    Long loaiId = Long.valueOf(trimmedKeyword);
                    // TÌM KIẾM CHÍNH XÁC ID DƯỚI DẠNG SỐ
                    sql.append(" AND loaiId = ?");
                    params.add(loaiId);
                } catch (NumberFormatException e) {
                    // Nếu nhập chữ cho Loai ID, trả về list rỗng
                    return new ArrayList<>();
                }
                break;
            case "Trạng thái":
                sql.append(" AND trangThai LIKE ?");
                // Tên ENUM được lưu bằng chữ HOA
                params.add(likeKeyword.toUpperCase()); 
                break;
            case "Ghi chú":
                sql.append(" AND ghiChu LIKE ?");
                params.add(likeKeyword);
                break;
            case "Tất cả":
            default:
                // Tìm kiếm tổng hợp theo nhiều cột
                sql.append(" AND (soSerial LIKE ? OR ghiChu LIKE ? OR trangThai LIKE ?)");
                params.add(likeKeyword);
                params.add(likeKeyword);
                params.add(likeKeyword.toUpperCase()); 
                break;
        }
        
        try {
            return executeQuery(sql.toString(), params.toArray());
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}