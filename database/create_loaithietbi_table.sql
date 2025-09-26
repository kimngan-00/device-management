-- Script tạo bảng LoaiThietBi
-- Chạy script này trong MySQL để tạo bảng LoaiThietBi

USE quanlythietbi; -- Thay đổi tên database theo database của bạn

CREATE TABLE IF NOT EXISTS LoaiThietBi (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    maLoai VARCHAR(20) UNIQUE NOT NULL,
    tenLoai VARCHAR(255) 
        CHARACTER SET utf8mb4 
        COLLATE utf8mb4_0900_ai_ci NOT NULL,
    moTa TEXT 
        CHARACTER SET utf8mb4 
        COLLATE utf8mb4_0900_ai_ci,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) 
CHARACTER SET = utf8mb4 
COLLATE = utf8mb4_0900_ai_ci;

-- Thêm dữ liệu mẫu
INSERT INTO LoaiThietBi (maLoai, tenLoai, moTa) VALUES
('LTB001', 'Laptop', 'Máy tính xách tay'),
('LTB002', 'Desktop', 'Máy tính để bàn'),
('LTB003', 'Monitor', 'Màn hình máy tính'),
('LTB004', 'Keyboard', 'Bàn phím'),
('LTB005', 'Mouse', 'Chuột máy tính'),
('LTB006', 'Printer', 'Máy in'),
('LTB007', 'Scanner', 'Máy quét'),
('LTB008', 'Projector', 'Máy chiếu'),
('LTB009', 'Router', 'Bộ định tuyến'),
('LTB010', 'Switch', 'Bộ chuyển mạch');

-- Kiểm tra dữ liệu
SELECT 'Dữ liệu LoaiThietBi:' as info;
SELECT * FROM LoaiThietBi;
SELECT COUNT(*) as 'Tổng số loại thiết bị' FROM LoaiThietBi; 