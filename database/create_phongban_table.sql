-- Script tạo bảng PhongBan
-- Chạy script này trong MySQL để tạo bảng PhongBan

USE quanlythietbi; -- Thay đổi tên database theo database của bạn

CREATE TABLE IF NOT EXISTS PhongBan (
    maPhongBan VARCHAR(20) PRIMARY KEY,
    tenPhongBan VARCHAR(255) 
        CHARACTER SET utf8mb4 
        COLLATE utf8mb4_0900_ai_ci NOT NULL,
    moTa TEXT 
        CHARACTER SET utf8mb4 
        COLLATE utf8mb4_0900_ai_ci
) 
CHARACTER SET = utf8mb4 
COLLATE = utf8mb4_0900_ai_ci;

-- Thêm dữ liệu mẫu
INSERT INTO PhongBan (maPhongBan, tenPhongBan, moTa) VALUES
('PB001', 'Phòng Kỹ thuật', 'Quản lý kỹ thuật và công nghệ thông tin'),
('PB002', 'Phòng Nhân sự', 'Quản lý nhân sự và tuyển dụng'),
('PB003', 'Phòng Tài chính', 'Quản lý tài chính và kế toán'),
('PB004', 'Phòng Marketing', 'Quản lý marketing và quảng cáo'),
('PB005', 'Phòng Kinh doanh', 'Quản lý kinh doanh và bán hàng');

-- Kiểm tra dữ liệu
SELECT * FROM PhongBan;
SELECT COUNT(*) as 'Tổng số phòng ban' FROM PhongBan;
