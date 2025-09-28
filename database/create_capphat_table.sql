-- Tạo bảng CapPhat để lưu trữ lịch sử cấp phát thiết bị
CREATE TABLE IF NOT EXISTS CapPhat (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    yeuCauId BIGINT NOT NULL,
    ngayCap DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ngayTra DATETIME NULL,
    tinhTrangTra ENUM('TOT', 'TRAY_XUOC', 'HU_HONG', 'MAT') NULL,
    ghiChu TEXT,
    
    -- Foreign key constraint
    FOREIGN KEY (yeuCauId) REFERENCES YeuCau(id) ON DELETE CASCADE,
    
    -- Indexes để tối ưu hóa truy vấn
    INDEX idx_capphat_yeucau (yeuCauId),
    INDEX idx_capphat_ngaycap (ngayCap),
    INDEX idx_capphat_ngaytra (ngayTra),
    INDEX idx_capphat_active (ngayTra) -- Để tìm nhanh cấp phát đang hoạt động
);

-- Comment cho bảng
ALTER TABLE CapPhat COMMENT = 'Bảng lưu trữ lịch sử cấp phát thiết bị cho nhân viên';
