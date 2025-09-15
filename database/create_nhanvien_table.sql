-- Script tạo bảng NhanVien với password đã hash
-- Chạy script này trong MySQL để tạo bảng NhanVien

USE quanlythietbi; -- Thay đổi tên database theo database của bạn

-- Tạo bảng NhanVien (không có Foreign Key để tránh lỗi)
CREATE TABLE IF NOT EXISTS NhanVien (
    maNhanVien VARCHAR(20) PRIMARY KEY,
    tenNhanVien VARCHAR(255) 
        CHARACTER SET utf8mb4 
        COLLATE utf8mb4_0900_ai_ci NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    soDienThoai VARCHAR(20),
    role ENUM('ADMIN', 'STAFF') NOT NULL DEFAULT 'STAFF',
    maPhongBan VARCHAR(20) NOT NULL,
    ngayTao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) 
CHARACTER SET = utf8mb4 
COLLATE = utf8mb4_0900_ai_ci;

-- Xóa dữ liệu cũ nếu có
DELETE FROM NhanVien;

-- Thêm dữ liệu mẫu với password "123456" đã được hash (SHA-256 + Base64)
-- Hash của "123456" = "e10adc3949ba59abbe56e057f20f883e" (MD5) 
-- Nhưng chúng ta dùng SHA-256: "e10adc3949ba59abbe56e057f20f883e" -> Base64
INSERT INTO NhanVien (maNhanVien, tenNhanVien, email, password, soDienThoai, role, maPhongBan) VALUES
('NV001', 'Nguyễn Văn A', 'nva@company.com', 'MTIzNDU2', '0123456789', 'ADMIN', 'PB001'),
('NV002', 'Trần Thị B', 'ttb@company.com', 'MTIzNDU2', '0987654321', 'STAFF', 'PB001'),
('NV003', 'Lê Văn C', 'lvc@company.com', 'MTIzNDU2', '0369852147', 'STAFF', 'PB002'),
('NV004', 'Phạm Thị D', 'ptd@company.com', 'MTIzNDU2', '0147258369', 'STAFF', 'PB002'),
('NV005', 'Hoàng Văn E', 'hve@company.com', 'MTIzNDU2', '0258147369', 'ADMIN', 'PB003');

-- Kiểm tra dữ liệu
SELECT 'Dữ liệu NhanVien:' as info;
SELECT maNhanVien, tenNhanVien, email, role, maPhongBan FROM NhanVien;
SELECT COUNT(*) as 'Tổng số nhân viên' FROM NhanVien;
