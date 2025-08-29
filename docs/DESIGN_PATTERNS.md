# Design Patterns trong Hệ thống Quản lý Thiết bị

## Tổng quan

Dự án này đã áp dụng các Design Patterns phổ biến để tạo ra một kiến trúc code sạch, dễ bảo trì và mở rộng.

## 1. Singleton Pattern

### Mục đích
Đảm bảo chỉ có một instance của DatabaseConnection trong toàn bộ ứng dụng.

### Implementation
```java
public class DatabaseConnection {
    private static DatabaseConnection instance;
    
    private DatabaseConnection() {
        loadDatabaseConfig();
    }
    
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
}
```

### Lợi ích
- ✅ Tiết kiệm tài nguyên database connection
- ✅ Đảm bảo tính nhất quán của cấu hình
- ✅ Thread-safe với synchronized method

## 2. Factory Pattern

### Mục đích
Tạo các DAO objects dựa trên loại database được cấu hình.

### Implementation
```java
public class DAOFactory {
    public static DeviceDAO createDeviceDAO() {
        return createDeviceDAO(getDefaultDatabaseType());
    }
    
    public static DeviceDAO createDeviceDAO(DatabaseType databaseType) {
        switch (databaseType) {
            case MYSQL:
                return new DeviceDAOMySQLImpl();
            case IN_MEMORY:
                return new DeviceDAOImpl();
            // ...
        }
    }
}
```

### Lợi ích
- ✅ Dễ dàng chuyển đổi database (MySQL, SQL Server, PostgreSQL)
- ✅ Encapsulation logic tạo object
- ✅ Mở rộng dễ dàng cho database mới

## 3. Builder Pattern

### Mục đích
Tạo Device objects một cách linh hoạt và dễ đọc.

### Implementation
```java
Device laptop = DeviceBuilder.createLaptop("DEV001", "Laptop Dell")
    .description("Laptop cao cấp")
    .price(25000000)
    .manufacturer("Dell")
    .serialNumber("DL123456")
    .build();
```

### Lợi ích
- ✅ Code dễ đọc và maintain
- ✅ Linh hoạt trong việc set properties
- ✅ Immutable objects
- ✅ Validation trong quá trình build

## 4. Observer Pattern

### Mục đích
Theo dõi và phản ứng với các thay đổi của Device objects.

### Implementation
```java
public interface DeviceObserver {
    void onDeviceAdded(Device device);
    void onDeviceDeleted(String deviceId);
    void onDeviceStatusChanged(Device device, String oldStatus, String newStatus);
}

public class DeviceLogger implements DeviceObserver {
    @Override
    public void onDeviceAdded(Device device) {
        logger.info("Device added: {} - {}", device.getDeviceId(), device.getDeviceName());
    }
}
```

### Lợi ích
- ✅ Loose coupling giữa Device và logging
- ✅ Dễ dàng thêm observers mới (email notification, audit trail)
- ✅ Real-time monitoring

## 5. Strategy Pattern (Được chuẩn bị)

### Mục đích
Cho phép thay đổi algorithm xử lý database mà không ảnh hưởng đến client code.

### Implementation
```java
public interface DatabaseStrategy {
    Connection getConnection();
    void executeQuery(String sql);
}

public class MySQLStrategy implements DatabaseStrategy {
    // MySQL specific implementation
}

public class SQLServerStrategy implements DatabaseStrategy {
    // SQL Server specific implementation
}
```

## Cấu trúc thư mục

```
src/main/java/com/mycompany/device/
├── dao/
│   ├── factory/
│   │   └── DAOFactory.java          # ← Factory Pattern
│   └── impl/
│       ├── DeviceDAOMySQLImpl.java
│       └── UserDAOMySQLImpl.java
├── model/
│   ├── Device.java
│   └── DeviceBuilder.java           # ← Builder Pattern
├── observer/
│   ├── DeviceObserver.java          # ← Observer Pattern
│   ├── DeviceSubject.java
│   └── DeviceLogger.java
├── service/
│   └── impl/
│       └── DeviceServiceImpl.java   # ← Sử dụng tất cả patterns
└── util/
    └── DatabaseConnection.java      # ← Singleton Pattern
```

## Lợi ích tổng thể

### 1. Maintainability
- Code được tổ chức rõ ràng
- Dễ dàng thêm tính năng mới
- Dễ dàng sửa lỗi

### 2. Extensibility
- Dễ dàng thêm database mới
- Dễ dàng thêm observers mới
- Dễ dàng thêm device types mới

### 3. Testability
- Có thể mock các dependencies
- Unit tests dễ viết
- Integration tests rõ ràng

### 4. Performance
- Singleton giảm overhead
- Factory caching objects
- Observer async processing

## Ví dụ sử dụng

### Tạo Device với Builder
```java
Device device = DeviceBuilder.createLaptop("DEV001", "MacBook Pro")
    .description("Laptop cho developer")
    .price(35000000)
    .manufacturer("Apple")
    .serialNumber("MB123456")
    .build();
```

### Sử dụng Factory
```java
DeviceDAO deviceDAO = DAOFactory.createDeviceDAO(DAOFactory.DatabaseType.MYSQL);
```

### Thêm Observer
```java
deviceService.addObserver(new EmailNotifier());
deviceService.addObserver(new AuditLogger());
```

## Kết luận

Việc áp dụng các Design Patterns đã tạo ra một kiến trúc code:
- **Sạch và dễ hiểu**
- **Dễ bảo trì và mở rộng**
- **Có thể test tốt**
- **Performance cao**

Các patterns này có thể được áp dụng cho các dự án Java khác để tạo ra code chất lượng cao. 