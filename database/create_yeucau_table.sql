-- Script tạo bảng YeuCau
-- Chạy script này trong MySQL để tạo bảng YeuCau

USE quanlythietbi; -- Thay đổi tên database theo database của bạn

CREATE TABLE IF NOT EXISTS YeuCau (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    thietBiId BIGINT NOT NULL,
    nhanVienId VARCHAR(20) NOT NULL,
    trangThai ENUM('CHO_DUYET', 'DA_DUYET', 'TU_CHOI', 'DA_CAP_PHAT', 'DA_HUY') 
        NOT NULL DEFAULT 'CHO_DUYET',
    lyDo TEXT 
        CHARACTER SET utf8mb4 
        COLLATE utf8mb4_0900_ai_ci,
    ngayTao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ngayCapNhat TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (thietBiId) REFERENCES ThietBi(id) ON DELETE CASCADE,
    FOREIGN KEY (nhanVienId) REFERENCES NhanVien(maNhanVien) ON DELETE CASCADE
) 
CHARACTER SET = utf8mb4 
COLLATE = utf8mb4_0900_ai_ci;

-- Thêm dữ liệu mẫu
INSERT INTO YeuCau (thietBiId, nhanVienId, trangThai, lyDo) VALUES
(1, 'NV002', 'CHO_DUYET', 'Cần laptop để làm việc từ xa'),
(2, 'NV003', 'DA_DUYET', 'Cần laptop cho dự án mới'),
(3, 'NV004', 'CHO_DUYET', 'Cần desktop cho phòng làm việc'),
(4, 'NV002', 'DA_CAP_PHAT', 'Cần monitor để làm việc hiệu quả hơn'),
(5, 'NV003', 'TU_CHOI', 'Monitor không phù hợp với yêu cầu'),
(6, 'NV004', 'CHO_DUYET', 'Cần keyboard mới thay thế keyboard cũ'),
(7, 'NV002', 'DA_DUYET', 'Cần mouse mới'),
(8, 'NV003', 'CHO_DUYET', 'Cần printer để in tài liệu'),
(9, 'NV004', 'DA_HUY', 'Không cần scanner nữa'),
(10, 'NV002', 'CHO_DUYET', 'Cần projector cho presentation');

-- Kiểm tra dữ liệu
SELECT 'Dữ liệu YeuCau:' as info;
SELECT y.id, t.soSerial, n.tenNhanVien, y.trangThai, y.lyDo, y.ngayTao
FROM YeuCau y 
JOIN ThietBi t ON y.thietBiId = t.id
JOIN NhanVien n ON y.nhanVienId = n.maNhanVien;
SELECT COUNT(*) as 'Tổng số yêu cầu' FROM YeuCau; 