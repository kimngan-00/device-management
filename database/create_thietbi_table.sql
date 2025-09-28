nn-- Script tạo bảng ThietBi
-- Chạy script này trong MySQL để tạo bảng ThietBi

USE quanlythietbi; -- Thay đổi tên database theo database của bạn

CREATE TABLE IF NOT EXISTS ThietBi (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    soSerial VARCHAR(100) UNIQUE NOT NULL,
    loaiId BIGINT NOT NULL,
    trangThai ENUM('TON_KHO', 'DANG_CAP_PHAT', 'DANG_BAO_TRI', 'HU_HONG', 'NGUNG_SU_DUNG') 
        NOT NULL DEFAULT 'TON_KHO',
    ngayMua DATE,
    giaMua DECIMAL(15,2),
    ghiChu TEXT 
        CHARACTER SET utf8mb4 
        COLLATE utf8mb4_0900_ai_ci,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (loaiId) REFERENCES LoaiThietBi(id) ON DELETE CASCADE
) 
CHARACTER SET = utf8mb4 
COLLATE = utf8mb4_0900_ai_ci;

-- Thêm dữ liệu mẫu
INSERT INTO ThietBi (soSerial, loaiId, trangThai, ngayMua, giaMua, ghiChu) VALUES
('TB001-LAPTOP-001', 1, 'TON_KHO', '2024-01-15', 15000000.00, 'Laptop Dell Inspiron 15'),
('TB002-LAPTOP-002', 1, 'DANG_CAP_PHAT', '2024-01-20', 18000000.00, 'Laptop HP Pavilion'),
('TB003-DESKTOP-001', 2, 'TON_KHO', '2024-02-01', 12000000.00, 'Desktop Dell OptiPlex'),
('TB004-MONITOR-001', 3, 'TON_KHO', '2024-02-05', 5000000.00, 'Monitor Samsung 24 inch'),
('TB005-MONITOR-002', 3, 'DANG_BAO_TRI', '2024-02-10', 4500000.00, 'Monitor LG 22 inch'),
('TB006-KEYBOARD-001', 4, 'TON_KHO', '2024-02-15', 500000.00, 'Keyboard Logitech'),
('TB007-MOUSE-001', 5, 'TON_KHO', '2024-02-20', 200000.00, 'Mouse Logitech'),
('TB008-PRINTER-001', 6, 'TON_KHO', '2024-03-01', 3000000.00, 'Printer Canon'),
('TB009-SCANNER-001', 7, 'TON_KHO', '2024-03-05', 2500000.00, 'Scanner Epson'),
('TB010-PROJECTOR-001', 8, 'TON_KHO', '2024-03-10', 15000000.00, 'Projector Epson');

-- Kiểm tra dữ liệu
SELECT 'Dữ liệu ThietBi:' as info;
SELECT t.id, t.soSerial, l.tenLoai, t.trangThai, t.ngayMua, t.giaMua 
FROM ThietBi t 
JOIN LoaiThietBi l ON t.loaiId = l.id;
SELECT COUNT(*) as 'Tổng số thiết bị' FROM ThietBi; 