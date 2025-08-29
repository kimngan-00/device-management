# Hệ Thống Quản Lý Thiết Bị

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

### Lỗi timezone
Thêm `serverTimezone=UTC` vào connection URL

### Lỗi SSL
Thêm `useSSL=false` vào connection URL

## License
MIT License 