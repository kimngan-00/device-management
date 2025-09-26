# Role-Based Access Control System

## Tổng quan

Hệ thống quản lý truy cập dựa trên vai trò (Role-Based Access Control - RBAC) đã được triển khai để kiểm soát quyền truy cập menu và chức năng dựa trên vai trò của người dùng.

## Phân quyền người dùng

### ADMIN (Quản trị viên)
**Quyền truy cập:**
- ✅ Dashboard (Tổng quan)
- ✅ Quản lý Phòng ban
- ✅ Quản lý Nhân viên  
- ✅ Quản lý Thiết bị
- ✅ Quản lý Yêu cầu (Admin) - Phê duyệt yêu cầu
- ✅ Hồ sơ cá nhân
- ✅ Báo cáo
- ✅ Cài đặt
- ❌ Yêu cầu sử dụng thiết bị (Không hiển thị)

### STAFF (Nhân viên)
**Quyền truy cập:**
- ✅ Dashboard (Tổng quan)
- ✅ Yêu cầu sử dụng thiết bị - Tạo yêu cầu mới
- ✅ Hồ sơ cá nhân
- ❌ Quản lý Phòng ban (Không hiển thị)
- ❌ Quản lý Nhân viên (Không hiển thị)
- ❌ Quản lý Thiết bị (Không hiển thị)  
- ❌ Quản lý Yêu cầu (Admin) (Không hiển thị)
- ❌ Báo cáo (Không hiển thị)
- ❌ Cài đặt (Không hiển thị)

## Triển khai kỹ thuật

### 1. MainFrame.java

#### Phương thức `addMenuItemsBasedOnRole()`
```java
private void addMenuItemsBasedOnRole() {
    NhanVien currentUser = authController.getCurrentUser();
    
    if (currentUser == null) {
        return;
    }
    
    NhanVien.NhanVienRole role = currentUser.getVaiTro();
    
    // Menu hiển thị cho tất cả vai trò
    addMenuItem("Dashboard", "📊", ScreenRouter.DASHBOARD);
    addMenuItem("Hồ sơ cá nhân", "👤", ScreenRouter.HO_SO_CA_NHAN);
    
    // Menu dành riêng cho từng vai trò
    if (role == NhanVien.NhanVienRole.ADMIN) {
        // Admin có quyền truy cập tất cả menu trừ "Yêu cầu sử dụng thiết bị"
        addMenuItem("Quản lý Phòng ban", "🏢", ScreenRouter.PHONG_BAN);
        addMenuItem("Quản lý Nhân viên", "👥", ScreenRouter.NHAN_VIEN);
        addMenuItem("Quản lý Thiết bị", "💻", ScreenRouter.THIET_BI);
        addMenuItem("Quản lý Yêu cầu (Admin)", "📋", ScreenRouter.ADMIN_YEU_CAU);
        addMenuItem("Báo cáo", "📈", ScreenRouter.BAO_CAO);
        addMenuItem("Cài đặt", "⚙️", ScreenRouter.CAI_DAT);
    } else if (role == NhanVien.NhanVienRole.STAFF) {
        // Staff chỉ có quyền truy cập "Yêu cầu sử dụng thiết bị"
        addMenuItem("Yêu cầu sử dụng thiết bị", "📱", ScreenRouter.YEU_CAU);
    }
}
```

#### Phương thức `refreshSidebar()`
```java
public void refreshSidebar() {
    // Xóa tất cả menu items hiện tại
    sidebarPanel.removeAll();
    
    // Tạo lại menu dựa trên vai trò
    addMenuItemsBasedOnRole();
    
    // Cập nhật UI
    sidebarPanel.revalidate();
    sidebarPanel.repaint();
}
```

### 2. LoginFrame.java

#### Cập nhật trong `openMainFrame()`
```java
MainFrame mainFrame = new MainFrame(authController);
// Cập nhật sidebar dựa trên quyền của user
mainFrame.refreshSidebar();
mainFrame.setVisible(true);
```

### 3. ScreenRouter.java

Đã thêm hằng số cho màn hình Admin:
```java
public static final String ADMIN_YEU_CAU = "ADMIN_YEU_CAU";
```

## Chức năng AdminYeuCauPanel

### Tính năng chính:
1. **Hiển thị danh sách yêu cầu:** Table với các cột thông tin chi tiết
2. **Action buttons:** Approve/Reject cho từng yêu cầu
3. **Filtering:** Lọc theo trạng thái (Tất cả, Chờ duyệt, Đã phê duyệt, Bị từ chối)
4. **Search:** Tìm kiếm theo mã yêu cầu, tên nhân viên, tên thiết bị
5. **Statistics:** Hiển thị thống kê tổng quan
6. **Refresh:** Cập nhật danh sách realtime

### Action buttons implementation:
```java
// Custom renderer cho action buttons
private class ActionButtonRenderer extends JPanel implements TableCellRenderer
// Custom editor cho xử lý sự kiện click
private class ActionButtonEditor extends DefaultCellEditor
```

## Luồng hoạt động

1. **Đăng nhập:** User nhập thông tin đăng nhập
2. **Xác thực:** AuthController kiểm tra thông tin và lưu user session
3. **Tạo MainFrame:** LoginFrame tạo MainFrame với AuthController
4. **Refresh Sidebar:** Gọi `refreshSidebar()` để tạo menu theo vai trò
5. **Hiển thị giao diện:** MainFrame hiển thị với menu phù hợp với vai trò

## Bảo mật

### Kiểm soát truy cập:
- Menu items được tạo động dựa trên vai trò
- Không có menu item nào của role khác được hiển thị
- AuthController quản lý session và thông tin user

### Ngăn chặn truy cập trái phép:
- Chỉ có ADMIN mới có thể truy cập AdminYeuCauPanel
- STAFF không thể thấy hoặc truy cập các chức năng quản trị
- Mỗi action đều được kiểm tra quyền thông qua AuthController

## Testing

### Test cases cần kiểm tra:

1. **Đăng nhập với ADMIN:**
   - Verify: Hiển thị đầy đủ menu trừ "Yêu cầu sử dụng thiết bị"
   - Verify: Có thể truy cập AdminYeuCauPanel
   - Verify: Có thể approve/reject yêu cầu

2. **Đăng nhập với STAFF:**
   - Verify: Chỉ hiển thị Dashboard, Hồ sơ cá nhân, Yêu cầu sử dụng thiết bị
   - Verify: Không thể truy cập các chức năng quản trị
   - Verify: Có thể tạo yêu cầu mới

3. **Switch user:**
   - Verify: Menu được cập nhật đúng khi đăng nhập user khác vai trò
   - Verify: Session được quản lý chính xác

## Mở rộng

Hệ thống có thể được mở rộng để:
- Thêm vai trò mới (VD: MANAGER, SUPERVISOR)
- Kiểm soát quyền ở cấp độ chức năng chi tiết hơn
- Audit log cho các hành động của user
- Permission-based access thay vì role-based
