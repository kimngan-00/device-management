# Hệ Thống Quản Lý Thiết Bị

## Mô tả dự án
Hệ thống quản lý thiết bị được phát triển bởi nhóm 3 thành viên, sử dụng Java với kiến trúc 3 lớp (3-tier architecture) và MySQL database.

## Cấu trúc dự án

### Kiến trúc hệ thống
```
src/main/java/com/mycompany/device/
├── DeviceManagementApp.java          # Class chính của ứng dụng
├── model/                            # Layer 1: Model Layer
│   ├── Device.java                   # Model thiết bị
│   └── User.java                     # Model người dùng
├── dao/                              # Layer 2: Data Access Layer
│   ├── DeviceDAO.java                # Interface DAO cho thiết bị
│   ├── UserDAO.java                  # Interface DAO cho người dùng
│   └── impl/
│       ├── DeviceDAOImpl.java        # Implementation in-memory
│       ├── DeviceDAOMySQLImpl.java   # Implementation MySQL
│       └── UserDAOMySQLImpl.java     # Implementation MySQL cho User
├── service/                          # Layer 3: Business Logic Layer
│   ├── DeviceService.java            # Interface Service
│   ├── DeviceStatistics.java         # Class thống kê
│   └── impl/
│       └── DeviceServiceImpl.java    # Implementation Service
├── ui/                               # Presentation Layer
│   └── DeviceManagementUI.java       # Giao diện người dùng
└── util/
    └── DatabaseConnection.java       # Quản lý kết nối database
```

### Phân công nhóm
- **Team Member 1**: Model Layer - Định nghĩa các entity và model
- **Team Member 2**: Data Access Layer - Xử lý truy cập dữ liệu MySQL
- **Team Member 3**: Business Logic & Presentation Layer - Logic nghiệp vụ và giao diện

## Tính năng chính

### 1. Quản lý thiết bị
- Thêm thiết bị mới
- Cập nhật thông tin thiết bị
- Xóa thiết bị
- Xem danh sách thiết bị

### 2. Tìm kiếm thiết bị
- Tìm theo tên
- Tìm theo loại
- Tìm theo trạng thái
- Tìm theo vị trí

### 3. Giao/Thu hồi thiết bị
- Giao thiết bị cho người dùng
- Thu hồi thiết bị
- Thay đổi trạng thái thiết bị

### 4. Thống kê
- Thống kê tổng quan
- Thống kê theo trạng thái
- Thống kê theo loại thiết bị

## Cài đặt và chạy

### Yêu cầu hệ thống
- Java 24 hoặc cao hơn
- Maven 3.6+
- MySQL 8.0 hoặc cao hơn

### Cài đặt MySQL
Xem hướng dẫn chi tiết tại: [docs/MYSQL_SETUP.md](docs/MYSQL_SETUP.md)

### Cấu hình Database

1. **Chỉnh sửa file `config.env`:**
```bash
# Database Configuration
DB_URL=jdbc:mysql://localhost:3306/device_management?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
DB_USERNAME=root
DB_PASSWORD=your_password_here
DB_DRIVER=com.mysql.cj.jdbc.Driver
```

2. **Hoặc sử dụng cấu hình mặc định** (không cần mật khẩu):
```bash
DB_USERNAME=root
DB_PASSWORD=
```

**Lưu ý:** Hệ thống sẽ tự động tạo database `device_management` nếu chưa tồn tại.

### Cài đặt ứng dụng
```bash
# Clone repository
git clone <repository-url>
cd device-management-system

# Compile project
mvn clean compile

# Chạy ứng dụng
mvn exec:java
```

### Đăng nhập
- **Admin**: username = "admin"
- **Manager**: username = "manager"
- **User**: bất kỳ username khác

## Cấu trúc dữ liệu

### Device (Thiết bị)
- deviceId: ID thiết bị
- deviceName: Tên thiết bị
- deviceType: Loại thiết bị
- manufacturer: Nhà sản xuất
- model: Model
- serialNumber: Số serial
- location: Vị trí
- status: Trạng thái (Có sẵn, Đang sử dụng, Bảo trì, Đã nghỉ hưu, Mất mát)
- assignedTo: Người được giao
- description: Mô tả

### User (Người dùng)
- userId: ID người dùng
- username: Tên đăng nhập
- fullName: Họ tên đầy đủ
- email: Email
- phone: Số điện thoại
- department: Phòng ban
- role: Vai trò (Admin, Manager, User)

## Database Schema

### Bảng devices
```sql
CREATE TABLE devices (
    device_id VARCHAR(20) PRIMARY KEY,
    device_name VARCHAR(100) NOT NULL,
    device_type VARCHAR(50) NOT NULL,
    manufacturer VARCHAR(100),
    model VARCHAR(100),
    serial_number VARCHAR(100),
    location VARCHAR(100),
    status ENUM('AVAILABLE', 'IN_USE', 'MAINTENANCE', 'RETIRED', 'LOST') DEFAULT 'AVAILABLE',
    purchase_date TIMESTAMP NULL,
    last_maintenance_date TIMESTAMP NULL,
    assigned_to VARCHAR(20),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (assigned_to) REFERENCES users(user_id) ON DELETE SET NULL
);
```

### Bảng users
```sql
CREATE TABLE users (
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
```

### Bảng device_history
```sql
CREATE TABLE device_history (
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
```

## Công nghệ sử dụng
- **Java 24**: Ngôn ngữ lập trình chính
- **Maven**: Quản lý dependencies và build
- **MySQL 8.0**: Database chính
- **MySQL Connector**: Kết nối database
- **Jackson**: Xử lý JSON
- **SLF4J & Logback**: Logging
- **JUnit**: Unit testing

## Cấu hình Database

### File application.properties
```properties
# Cấu hình database MySQL
database.url=jdbc:mysql://localhost:3306/device_management?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
database.username=root
database.password=your_password
database.driver=com.mysql.cj.jdbc.Driver
```

### Backup và Restore
```bash
# Backup
mysqldump -u root -p device_management > backup.sql

# Restore
mysql -u root -p device_management < backup.sql
```

## Mở rộng tương lai
- Giao diện web với Spring Boot
- RESTful API
- Báo cáo và biểu đồ
- Quản lý bảo trì
- Lịch sử sử dụng thiết bị
- Thông báo email/SMS
- Mobile app
- Cloud deployment

## Đóng góp
Mỗi thành viên trong nhóm chịu trách nhiệm cho một layer cụ thể:
1. **Model Layer**: Định nghĩa cấu trúc dữ liệu
2. **Data Access Layer**: Truy cập và lưu trữ dữ liệu MySQL
3. **Business Logic Layer**: Xử lý logic nghiệp vụ
4. **Presentation Layer**: Giao diện người dùng

## Troubleshooting

### Lỗi kết nối database
1. Kiểm tra MySQL service có đang chạy không
2. Kiểm tra username/password trong application.properties
3. Kiểm tra database `device_management` đã được tạo chưa

### Lỗi timezone
Thêm `serverTimezone=UTC` vào connection URL

### Lỗi SSL
Thêm `useSSL=false` vào connection URL

## License
MIT License 