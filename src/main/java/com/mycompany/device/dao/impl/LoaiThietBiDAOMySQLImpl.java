package com.mycompany.device.dao.impl;

import com.mycompany.device.dao.LoaiThietBiDAO;
import com.mycompany.device.util.DatabaseConnection;
import com.mycompany.device.model.LoaiThietBi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Triển khai LoaiThietBiDAO sử dụng MySQL.
 * @author Kim Ngan - LoaiThietBi DAO Implementation
 */
public class LoaiThietBiDAOMySQLImpl implements LoaiThietBiDAO {

    private static final String SELECT_ALL_SQL = 
        "SELECT id, maLoai, tenLoai, moTa FROM LoaiThietBi";
    private static final String SELECT_BY_ID_SQL = SELECT_ALL_SQL + " WHERE id = ?";
    private static final String SELECT_BY_MA_LOAI_SQL = SELECT_ALL_SQL + " WHERE maLoai = ?";
    private static final String INSERT_SQL = 
        "INSERT INTO LoaiThietBi (maLoai, tenLoai, moTa) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = 
        "UPDATE LoaiThietBi SET maLoai = ?, tenLoai = ?, moTa = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM LoaiThietBi WHERE id = ?";

    private LoaiThietBi mapResultSetToLoaiThietBi(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String maLoai = rs.getString("maLoai");
        String tenLoai = rs.getString("tenLoai");
        String moTa = rs.getString("moTa");
        
        return new LoaiThietBi(id, maLoai, tenLoai, moTa);
    }

    @Override
    public List<LoaiThietBi> findAll() {
        List<LoaiThietBi> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) {
                list.add(mapResultSetToLoaiThietBi(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Ném RuntimeException để Service layer xử lý
            throw new RuntimeException("Lỗi DB khi tìm tất cả Loại Thiết Bị.", e);
        }
        return list;
    }

    @Override
    public LoaiThietBi findById(Long id) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLoaiThietBi(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public LoaiThietBi findByMaLoai(String maLoai) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_MA_LOAI_SQL)) {
            
            pstmt.setString(1, maLoai);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLoaiThietBi(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean save(LoaiThietBi loaiThietBi) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, loaiThietBi.getMaLoai());
            pstmt.setString(2, loaiThietBi.getTenLoai());
            pstmt.setString(3, loaiThietBi.getMoTa());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        loaiThietBi.setId(generatedKeys.getLong(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(LoaiThietBi loaiThietBi) {
        if (loaiThietBi.getId() == null) return false;
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {
            
            pstmt.setString(1, loaiThietBi.getMaLoai());
            pstmt.setString(2, loaiThietBi.getTenLoai());
            pstmt.setString(3, loaiThietBi.getMoTa());
            pstmt.setLong(4, loaiThietBi.getId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)) {
            
            pstmt.setLong(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}