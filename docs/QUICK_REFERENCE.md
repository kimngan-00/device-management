# Quick Reference - Tạo Screen Mới

## 🚀 Quy trình nhanh (9 bước)

1. **Model** → Tạo `NewEntity.java` trong `model/`
2. **DAO** → Tạo interface + MySQL implementation trong `dao/`
3. **Service** → Tạo interface + implementation trong `service/`
4. **Panel** → Tạo `NewEntityPanel.java` trong `ui/swing/panel/`
5. **Controller** → Tạo `NewEntityController.java` trong `controller/`
6. **Router** → Thêm constant vào `ScreenRouter.java`
7. **Menu** → Thêm menu item vào `MainFrame.java`
8. **Register** → Đăng ký panel với ScreenRouter
9. **Database** → Tạo table SQL

## 📁 File Structure Template

```
src/main/java/com/mycompany/device/
├── model/NewEntity.java
├── dao/
│   ├── NewEntityDAO.java
│   └── impl/NewEntityDAOMySQLImpl.java
├── service/
│   ├── NewEntityService.java
│   └── impl/NewEntityServiceImpl.java
├── controller/NewEntityController.java
└── ui/swing/panel/NewEntityPanel.java
```

## ⚡ Code Snippets

### ScreenRouter constant
```java
public static final String NEW_ENTITY = "NEW_ENTITY";
```

### MainFrame menu
```java
JMenuItem newEntityMenuItem = new JMenuItem("Quản lý NewEntity");
newEntityMenuItem.addActionListener(e -> {
    screenRouter.navigateToScreen(ScreenRouter.NEW_ENTITY);
});
```

### Panel registration
```java
NewEntityPanel newEntityPanel = new NewEntityPanel();
NewEntityController newEntityController = new NewEntityController(newEntityPanel);
screenRouter.registerScreen(ScreenRouter.NEW_ENTITY, newEntityPanel);
```

## 🗄️ Database Template
```sql
CREATE TABLE IF NOT EXISTS new_entity (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## 🎯 Button Styling (Copy-paste ready)
```java
private void styleButton(JButton button) {
    button.setBackground(Color.WHITE);
    button.setForeground(Color.BLACK);
    button.setFocusPainted(false);
    button.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.BLACK, 2),
        BorderFactory.createEmptyBorder(8, 16, 8, 16)
    ));
    button.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
    
    button.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseEntered(java.awt.event.MouseEvent e) {
            button.setBackground(new Color(240, 240, 240));
        }
        
        @Override
        public void mouseExited(java.awt.event.MouseEvent e) {
            button.setBackground(Color.WHITE);
        }
    });
}
```

## ✅ Test Commands
```bash
# Compile
mvn clean compile

# Run
mvn exec:java

# Test checklist
1. Login → Navigate to new menu
2. CRUD operations (Add/Update/Delete/Search)
3. Table selection → Form population
4. Clear form functionality
```

---
📖 **Xem chi tiết**: [CREATE_NEW_SCREEN_GUIDE.md](CREATE_NEW_SCREEN_GUIDE.md)
