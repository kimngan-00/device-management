# Hướng dẫn cài đặt và cấu hình MySQL cho hệ thống quản lý thiết bị

## Yêu cầu hệ thống
- MySQL Server 8.0 hoặc cao hơn
- Java 24+
- Maven 3.6+

## Bước 1: Cài đặt MySQL

### Trên macOS (sử dụng Homebrew)
```bash
# Cài đặt MySQL
brew install mysql

# Khởi động MySQL service
brew services start mysql

# Thiết lập mật khẩu root
mysql_secure_installation
```

### Trên Ubuntu/Debian
```bash
# Cập nhật package list
sudo apt update

# Cài đặt MySQL
sudo apt install mysql-server

# Khởi động MySQL service
sudo systemctl start mysql
sudo systemctl enable mysql

# Thiết lập bảo mật
sudo mysql_secure_installation
```

### Trên Windows
1. Tải MySQL Installer từ trang chủ MySQL
2. Chạy installer và làm theo hướng dẫn
3. Thiết lập mật khẩu root trong quá trình cài đặt

## Bước 2: Tạo database và user

### Đăng nhập vào MySQL
```bash
mysql -u root -p
```

### Tạo database và user
```sql
-- Tạo database
CREATE DATABASE device_management
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Tạo user cho ứng dụng (tùy chọn)
CREATE USER 'device_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON device_management.* TO 'device_user'@'localhost';
FLUSH PRIVILEGES;

-- Thoát MySQL
EXIT;
```

## Bước 3: Chạy script khởi tạo database

### Cách 1: Sử dụng command line
```bash
mysql -u root -p device_management < database/init.sql
```

### Cách 2: Sử dụng MySQL Workbench
1. Mở MySQL Workbench
2. Kết nối đến MySQL server
3. Chọn database `device_management`
4. Mở file `database/init.sql`
5. Chạy script

## Bước 4: Cấu hình ứng dụng

### Cập nhật file `src/main/resources/application.properties`
```properties
# Cấu hình database MySQL
database.url=jdbc:mysql://localhost:3306/device_management?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
database.username=root
database.password=your_root_password
database.driver=com.mysql.cj.jdbc.Driver
```

**Lưu ý:** Thay `your_root_password` bằng mật khẩu MySQL root của bạn.

## Bước 5: Kiểm tra kết nối

### Chạy ứng dụng
```bash
# Compile project
mvn clean compile

# Chạy ứng dụng
mvn exec:java
```

### Kiểm tra database
```sql
-- Đăng nhập MySQL
mysql -u root -p device_management

-- Kiểm tra các bảng
SHOW TABLES;

-- Kiểm tra dữ liệu users
SELECT * FROM users;

-- Kiểm tra dữ liệu devices
SELECT * FROM devices;

-- Kiểm tra thống kê
SELECT 
    COUNT(*) as total_devices,
    status,
    COUNT(*) as count_by_status
FROM devices 
GROUP BY status;
```

## Bước 6: Xử lý lỗi thường gặp

### Lỗi kết nối database
```
Error: Communications link failure
```
**Giải pháp:**
1. Kiểm tra MySQL service có đang chạy không
2. Kiểm tra port 3306 có bị block không
3. Kiểm tra username/password trong application.properties

### Lỗi timezone
```
Error: The server time zone value is unrecognized
```
**Giải pháp:**
Thêm `serverTimezone=UTC` vào connection URL

### Lỗi SSL
```
Error: SSL connection is required
```
**Giải pháp:**
Thêm `useSSL=false` vào connection URL

### Lỗi authentication
```
Error: Access denied for user
```
**Giải pháp:**
1. Kiểm tra username/password
2. Kiểm tra quyền của user
3. Thử đăng nhập trực tiếp bằng command line

## Bước 7: Backup và Restore

### Backup database
```bash
mysqldump -u root -p device_management > backup_$(date +%Y%m%d_%H%M%S).sql
```

### Restore database
```bash
mysql -u root -p device_management < backup_file.sql
```

## Bước 8: Tối ưu hiệu suất

### Cấu hình MySQL (my.cnf)
```ini
[mysqld]
# Buffer pool size (50-70% RAM)
innodb_buffer_pool_size = 1G

# Log file size
innodb_log_file_size = 256M

# Connection timeout
wait_timeout = 28800
interactive_timeout = 28800

# Query cache
query_cache_type = 1
query_cache_size = 64M
```

## Monitoring và Maintenance

### Kiểm tra trạng thái MySQL
```bash
# Kiểm tra service status
sudo systemctl status mysql

# Kiểm tra process
ps aux | grep mysql

# Kiểm tra port
netstat -tlnp | grep 3306
```

### Log files
- Error log: `/var/log/mysql/error.log`
- General log: `/var/log/mysql/general.log`
- Slow query log: `/var/log/mysql/slow.log`

## Troubleshooting

### MySQL không khởi động
```bash
# Kiểm tra error log
sudo tail -f /var/log/mysql/error.log

# Kiểm tra quyền thư mục
sudo chown -R mysql:mysql /var/lib/mysql
sudo chmod -R 755 /var/lib/mysql
```

### Performance issues
```sql
-- Kiểm tra slow queries
SHOW VARIABLES LIKE 'slow_query_log';
SHOW VARIABLES LIKE 'long_query_time';

-- Kiểm tra process list
SHOW PROCESSLIST;

-- Kiểm tra table status
SHOW TABLE STATUS;
```

## Liên hệ hỗ trợ
Nếu gặp vấn đề, vui lòng:
1. Kiểm tra log files
2. Chụp màn hình lỗi
3. Liên hệ team phát triển 