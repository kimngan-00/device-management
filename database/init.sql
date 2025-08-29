-- Script khởi tạo database cho hệ thống quản lý thiết bị
-- Tác giả: Team Device Management

-- Tạo database
CREATE DATABASE IF NOT EXISTS device_management
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE device_management;

-- Tạo bảng users
CREATE TABLE IF NOT EXISTS users (
    user_id VARCHAR(20) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    department VARCHAR(100),
    role ENUM('ADMIN', 'MANAGER', 'USER') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

-- Tạo bảng devices
CREATE TABLE IF NOT EXISTS devices (
    device_id VARCHAR(20) PRIMARY KEY,
    device_name VARCHAR(100) NOT NULL,
    device_type VARCHAR(50) NOT NULL,
    status ENUM('AVAILABLE', 'IN_USE', 'MAINTENANCE') DEFAULT 'AVAILABLE',
    description TEXT,
    price FLOAT DEFAULT 0.0,
    manufacturer VARCHAR(100),
    serial_number VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tạo bảng device_history để theo dõi lịch sử
CREATE TABLE IF NOT EXISTS device_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    device_id VARCHAR(20) NOT NULL,
    action VARCHAR(50) NOT NULL,
    old_value TEXT,
    new_value TEXT,
    user_id VARCHAR(20),
    action_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (device_id) REFERENCES devices(device_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
);

-- Tạo indexes để tối ưu hiệu suất
CREATE INDEX idx_devices_status ON devices(status);
CREATE INDEX idx_devices_type ON devices(device_type);
CREATE INDEX idx_devices_manufacturer ON devices(manufacturer);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_department ON users(department);
CREATE INDEX idx_device_history_device_id ON device_history(device_id);
CREATE INDEX idx_device_history_action_date ON device_history(action_date);

-- Thêm dữ liệu mẫu cho users
INSERT INTO users (user_id, username, full_name, email, phone, department, role) VALUES
('ADM001', 'admin', 'Quản trị viên hệ thống', 'admin@company.com', '0123456789', 'IT', 'ADMIN'),
('MGR001', 'manager', 'Quản lý thiết bị', 'manager@company.com', '0987654321', 'Quản lý', 'MANAGER'),
('USR001', 'user1', 'Nguyễn Văn A', 'user1@company.com', '0111222333', 'Kế toán', 'USER'),
('USR002', 'user2', 'Trần Thị B', 'user2@company.com', '0444555666', 'Nhân sự', 'USER'),
('USR003', 'user3', 'Lê Văn C', 'user3@company.com', '0777888999', 'Marketing', 'USER');

-- Thêm dữ liệu mẫu cho devices
INSERT INTO devices (device_id, device_name, device_type, status, description, price, manufacturer, serial_number) VALUES
('DEV000001', 'Laptop Dell XPS 13', 'Laptop', 'AVAILABLE', 'Laptop cao cấp cho nhân viên IT', 25000000, 'Dell', 'DL123456789'),
('DEV000002', 'Máy in HP LaserJet', 'Máy in', 'AVAILABLE', 'Máy in laser đen trắng', 3500000, 'HP', 'HP987654321'),
('DEV000003', 'Màn hình Samsung 24"', 'Màn hình', 'AVAILABLE', 'Màn hình LED 24 inch', 4500000, 'Samsung', 'SM456789123'),
('DEV000004', 'Điện thoại iPhone 14', 'Điện thoại', 'AVAILABLE', 'Điện thoại thông minh', 18000000, 'Apple', 'AP789123456'),
('DEV000005', 'iPad Pro 12.9"', 'Máy tính bảng', 'AVAILABLE', 'Máy tính bảng cao cấp', 22000000, 'Apple', 'AP321654987'),
('DEV000006', 'Máy tính để bàn HP', 'Máy tính để bàn', 'AVAILABLE', 'Máy tính để bàn cho kỹ sư', 12000000, 'HP', 'HP111222333'),
('DEV000007', 'Máy chiếu Epson', 'Máy chiếu', 'AVAILABLE', 'Máy chiếu cho thuyết trình', 8000000, 'Epson', 'EP444555666'),
('DEV000008', 'Máy scan Canon', 'Máy scan', 'AVAILABLE', 'Máy quét tài liệu', 2500000, 'Canon', 'CA777888999');

-- Hiển thị thông tin database
SELECT 'Database device_management đã được tạo thành công!' as message;
SELECT COUNT(*) as total_users FROM users;
SELECT COUNT(*) as total_devices FROM devices;
