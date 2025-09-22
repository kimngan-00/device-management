# Hướng dẫn tạo Screen mới trong Device Management System

## 📋 Tổng quan

Tài liệu này hướng dẫn chi tiết cách tạo một screen/panel mới trong hệ thống quản lý thiết bị, bao gồm:
- Tạo Panel (View)
- Tạo Controller 
- Đăng ký vào Menu và ScreenRouter
- Kết nối với Database (DAO, Service)

## 🏗️ Kiến trúc hệ thống

```
src/main/java/com/mycompany/device/
├── model/                          # Model Layer (Entities)
├── dao/                           # Data Access Layer
│   ├── Interface.java
│   └── impl/
│       └── MySQLImpl.java
├── service/                       # Business Logic Layer
│   ├── Interface.java
│   └── impl/
│       └── ServiceImpl.java
├── controller/                    # Controller Layer (Business Logic)
├── ui/
│   ├── swing/
│   │   ├── frame/                # Main frames
│   │   ├── panel/                # UI Panels (Views)
│   │   └── service/              # UI Services
│   └── route/                    # Navigation Router
└── util/                         # Utilities
```

## 🚀 Bước 1: Tạo Model

### 1.1 Tạo Entity class trong `model/`

```java
// src/main/java/com/mycompany/device/model/NewEntity.java
package com.mycompany.device.model;

/**
 * Model cho Entity mới
 * @author Your Name
 */
public class NewEntity {
    private String id;
    private String name;
    private String description;
    
    // Constructors
    public NewEntity() {}
    
    public NewEntity(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    @Override
    public String toString() {
        return "NewEntity{id='" + id + "', name='" + name + "', description='" + description + "'}";
    }
}
```

## 🗄️ Bước 2: Tạo DAO Layer

### 2.1 Tạo DAO Interface

```java
// src/main/java/com/mycompany/device/dao/NewEntityDAO.java
package com.mycompany.device.dao;

import com.mycompany.device.model.NewEntity;
import java.util.List;

public interface NewEntityDAO {
    List<NewEntity> findAll();
    NewEntity findById(String id);
    boolean save(NewEntity entity);
    boolean update(NewEntity entity);
    boolean delete(String id);
    List<NewEntity> findByName(String name);
}
```

### 2.2 Tạo MySQL Implementation

```java
// src/main/java/com/mycompany/device/dao/impl/NewEntityDAOMySQLImpl.java
package com.mycompany.device.dao.impl;

import com.mycompany.device.dao.NewEntityDAO;
import com.mycompany.device.model.NewEntity;
import com.mycompany.device.util.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NewEntityDAOMySQLImpl implements NewEntityDAO {
    private static final Logger logger = LoggerFactory.getLogger(NewEntityDAOMySQLImpl.class);
    
    @Override
    public List<NewEntity> findAll() {
        List<NewEntity> entities = new ArrayList<>();
        String sql = "SELECT * FROM new_entity ORDER BY name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                NewEntity entity = new NewEntity(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("description")
                );
                entities.add(entity);
            }
            
            logger.debug("Lấy danh sách tất cả entity: {} entities", entities.size());
            
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy danh sách entity", e);
        }
        
        return entities;
    }
    
    @Override
    public NewEntity findById(String id) {
        String sql = "SELECT * FROM new_entity WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new NewEntity(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("description")
                    );
                }
            }
            
        } catch (SQLException e) {
            logger.error("Lỗi khi tìm entity với id: {}", id, e);
        }
        
        return null;
    }
    
    @Override
    public boolean save(NewEntity entity) {
        String sql = "INSERT INTO new_entity (id, name, description) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, entity.getId());
            pstmt.setString(2, entity.getName());
            pstmt.setString(3, entity.getDescription());
            
            int rowsAffected = pstmt.executeUpdate();
            boolean success = rowsAffected > 0;
            
            if (success) {
                logger.info("Thêm entity thành công: {}", entity.getId());
            } else {
                logger.warn("Không thể thêm entity: {}", entity.getId());
            }
            
            return success;
            
        } catch (SQLException e) {
            logger.error("Lỗi khi thêm entity: {}", entity.getId(), e);
            return false;
        }
    }
    
    @Override
    public boolean update(NewEntity entity) {
        String sql = "UPDATE new_entity SET name = ?, description = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, entity.getName());
            pstmt.setString(2, entity.getDescription());
            pstmt.setString(3, entity.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            boolean success = rowsAffected > 0;
            
            if (success) {
                logger.info("Cập nhật entity thành công: {}", entity.getId());
            } else {
                logger.warn("Không thể cập nhật entity: {}", entity.getId());
            }
            
            return success;
            
        } catch (SQLException e) {
            logger.error("Lỗi khi cập nhật entity: {}", entity.getId(), e);
            return false;
        }
    }
    
    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM new_entity WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            
            int rowsAffected = pstmt.executeUpdate();
            boolean success = rowsAffected > 0;
            
            if (success) {
                logger.info("Xóa entity thành công: {}", id);
            } else {
                logger.warn("Không thể xóa entity: {}", id);
            }
            
            return success;
            
        } catch (SQLException e) {
            logger.error("Lỗi khi xóa entity: {}", id, e);
            return false;
        }
    }
    
    @Override
    public List<NewEntity> findByName(String name) {
        List<NewEntity> entities = new ArrayList<>();
        String sql = "SELECT * FROM new_entity WHERE name LIKE ? ORDER BY name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + name + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    NewEntity entity = new NewEntity(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("description")
                    );
                    entities.add(entity);
                }
            }
            
            logger.debug("Tìm kiếm entity theo tên '{}': {} kết quả", name, entities.size());
            
        } catch (SQLException e) {
            logger.error("Lỗi khi tìm kiếm entity theo tên: {}", name, e);
        }
        
        return entities;
    }
}
```

## 🔧 Bước 3: Tạo Service Layer

### 3.1 Tạo Service Interface

```java
// src/main/java/com/mycompany/device/service/NewEntityService.java
package com.mycompany.device.service;

import com.mycompany.device.model.NewEntity;
import java.util.List;

public interface NewEntityService {
    List<NewEntity> getAllEntities();
    NewEntity getEntityById(String id);
    boolean createEntity(NewEntity entity);
    boolean updateEntity(NewEntity entity);
    boolean deleteEntity(String id);
    List<NewEntity> searchEntitiesByName(String name);
    boolean isEntityExists(String id);
}
```

### 3.2 Tạo Service Implementation

```java
// src/main/java/com/mycompany/device/service/impl/NewEntityServiceImpl.java
package com.mycompany.device.service.impl;

import com.mycompany.device.dao.NewEntityDAO;
import com.mycompany.device.dao.impl.NewEntityDAOMySQLImpl;
import com.mycompany.device.model.NewEntity;
import com.mycompany.device.service.NewEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NewEntityServiceImpl implements NewEntityService {
    private static final Logger logger = LoggerFactory.getLogger(NewEntityServiceImpl.class);
    private final NewEntityDAO entityDAO;
    
    public NewEntityServiceImpl() {
        this.entityDAO = new NewEntityDAOMySQLImpl();
        logger.info("Khởi tạo NewEntityServiceImpl với MySQL database");
    }
    
    @Override
    public List<NewEntity> getAllEntities() {
        logger.info("Lấy danh sách tất cả entities");
        List<NewEntity> entities = entityDAO.findAll();
        logger.info("Lấy danh sách entities thành công: {} entities", entities.size());
        return entities;
    }
    
    @Override
    public NewEntity getEntityById(String id) {
        logger.info("Lấy entity theo id: {}", id);
        
        if (id == null || id.trim().isEmpty()) {
            logger.warn("ID entity không hợp lệ");
            return null;
        }
        
        NewEntity entity = entityDAO.findById(id.trim());
        if (entity != null) {
            logger.info("Tìm thấy entity: {}", id);
        } else {
            logger.warn("Không tìm thấy entity với id: {}", id);
        }
        
        return entity;
    }
    
    @Override
    public boolean createEntity(NewEntity entity) {
        logger.info("Tạo entity mới: {}", entity.getId());
        
        // Validation
        if (entity == null) {
            logger.error("Entity không được null");
            return false;
        }
        
        if (entity.getId() == null || entity.getId().trim().isEmpty()) {
            logger.error("ID entity không được rỗng");
            return false;
        }
        
        if (entity.getName() == null || entity.getName().trim().isEmpty()) {
            logger.error("Tên entity không được rỗng");
            return false;
        }
        
        // Kiểm tra trùng lặp
        if (isEntityExists(entity.getId())) {
            logger.error("Entity đã tồn tại với id: {}", entity.getId());
            return false;
        }
        
        boolean success = entityDAO.save(entity);
        if (success) {
            logger.info("Tạo entity thành công: {}", entity.getId());
        } else {
            logger.error("Không thể tạo entity: {}", entity.getId());
        }
        
        return success;
    }
    
    @Override
    public boolean updateEntity(NewEntity entity) {
        logger.info("Cập nhật entity: {}", entity.getId());
        
        // Validation
        if (entity == null) {
            logger.error("Entity không được null");
            return false;
        }
        
        if (entity.getId() == null || entity.getId().trim().isEmpty()) {
            logger.error("ID entity không được rỗng");
            return false;
        }
        
        if (entity.getName() == null || entity.getName().trim().isEmpty()) {
            logger.error("Tên entity không được rỗng");
            return false;
        }
        
        // Kiểm tra tồn tại
        if (!isEntityExists(entity.getId())) {
            logger.error("Entity không tồn tại với id: {}", entity.getId());
            return false;
        }
        
        boolean success = entityDAO.update(entity);
        if (success) {
            logger.info("Cập nhật entity thành công: {}", entity.getId());
        } else {
            logger.error("Không thể cập nhật entity: {}", entity.getId());
        }
        
        return success;
    }
    
    @Override
    public boolean deleteEntity(String id) {
        logger.info("Xóa entity: {}", id);
        
        if (id == null || id.trim().isEmpty()) {
            logger.error("ID entity không được rỗng");
            return false;
        }
        
        // Kiểm tra tồn tại
        if (!isEntityExists(id)) {
            logger.error("Entity không tồn tại với id: {}", id);
            return false;
        }
        
        boolean success = entityDAO.delete(id.trim());
        if (success) {
            logger.info("Xóa entity thành công: {}", id);
        } else {
            logger.error("Không thể xóa entity: {}", id);
        }
        
        return success;
    }
    
    @Override
    public List<NewEntity> searchEntitiesByName(String name) {
        logger.info("Tìm kiếm entities theo tên: {}", name);
        
        if (name == null || name.trim().isEmpty()) {
            logger.info("Tên tìm kiếm rỗng, trả về danh sách tất cả entities");
            return getAllEntities();
        }
        
        List<NewEntity> entities = entityDAO.findByName(name.trim());
        logger.info("Tìm kiếm thành công: {} entities", entities.size());
        return entities;
    }
    
    @Override
    public boolean isEntityExists(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        
        NewEntity entity = entityDAO.findById(id.trim());
        return entity != null;
    }
}
```

## 🖼️ Bước 4: Tạo Panel (View)

### 4.1 Tạo Panel Class

```java
// src/main/java/com/mycompany/device/ui/swing/panel/NewEntityPanel.java
package com.mycompany.device.ui.swing.panel;

import com.mycompany.device.model.NewEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panel quản lý NewEntity với Swing - View trong MVC pattern
 * @author Your Name
 */
public class NewEntityPanel extends JPanel {
    
    private static final Logger logger = LoggerFactory.getLogger(NewEntityPanel.class);
    
    // UI Components
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField idField;
    private JTextField nameField;
    private JTextArea descriptionTextArea;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton clearButton;
    private JTextField searchField;
    private JButton searchButton;
    
    public NewEntityPanel() {
        initializeUI();
        logger.info("Khởi tạo NewEntityPanel thành công");
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Tạo các thành phần
        createSearchPanel();
        createTablePanel();
        createFormPanel();
        createButtonPanel();
        
        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(createSearchPanel(), BorderLayout.NORTH);
        topPanel.add(createTablePanel(), BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(createFormPanel(), BorderLayout.CENTER);
        bottomPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        
        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
        splitPane.setDividerLocation(350);
        splitPane.setResizeWeight(0.6);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm"));
        
        searchPanel.add(new JLabel("Tên:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);
        
        searchButton = new JButton("Tìm kiếm");
        styleButton(searchButton);
        searchPanel.add(searchButton);
        
        refreshButton = new JButton("Làm mới");
        styleButton(refreshButton);
        searchPanel.add(refreshButton);
        
        return searchPanel;
    }
    
    private JScrollPane createTablePanel() {
        String[] columnNames = {"ID", "Tên", "Mô Tả"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setPreferredWidth(200);
        columnModel.getColumn(2).setPreferredWidth(300);
        
        table.setRowHeight(25);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setSelectionBackground(new Color(184, 207, 229));
        
        return new JScrollPane(table);
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // ID
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("ID:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        idField = new JTextField(20);
        formPanel.add(idField, gbc);
        
        // Tên
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Tên:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);
        
        // Mô tả
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        formPanel.add(new JLabel("Mô tả:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        descriptionTextArea = new JTextArea(4, 20);
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionTextArea);
        formPanel.add(scrollPane, gbc);
        
        return formPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        addButton = new JButton("Thêm");
        styleButton(addButton);
        
        updateButton = new JButton("Cập nhật");
        styleButton(updateButton);
        
        deleteButton = new JButton("Xóa");
        styleButton(deleteButton);
        
        clearButton = new JButton("Xóa form");
        styleButton(clearButton);
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        return buttonPanel;
    }
    
    // ========================= CONTROLLER INTERFACE METHODS =========================
    
    public void setAddButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }
    
    public void setUpdateButtonListener(ActionListener listener) {
        updateButton.addActionListener(listener);
    }
    
    public void setDeleteButtonListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }
    
    public void setSearchButtonListener(ActionListener listener) {
        searchButton.addActionListener(listener);
    }
    
    public void setRefreshButtonListener(ActionListener listener) {
        refreshButton.addActionListener(listener);
    }
    
    public void setClearButtonListener(ActionListener listener) {
        clearButton.addActionListener(listener);
    }
    
    public String getId() {
        return idField.getText().trim();
    }
    
    public String getName() {
        return nameField.getText().trim();
    }
    
    public String getDescription() {
        return descriptionTextArea.getText().trim();
    }
    
    public String getSearchText() {
        return searchField.getText().trim();
    }
    
    public NewEntity getSelectedEntity() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            String name = (String) tableModel.getValueAt(selectedRow, 1);
            String description = (String) tableModel.getValueAt(selectedRow, 2);
            return new NewEntity(id, name, description.isEmpty() ? null : description);
        }
        return null;
    }
    
    public void populateForm(NewEntity entity) {
        if (entity != null) {
            idField.setText(entity.getId());
            nameField.setText(entity.getName());
            descriptionTextArea.setText(entity.getDescription() != null ? entity.getDescription() : "");
        }
    }
    
    public void clearForm() {
        idField.setText("");
        nameField.setText("");
        descriptionTextArea.setText("");
        table.clearSelection();
    }
    
    public void clearSearch() {
        searchField.setText("");
    }
    
    public void updateTableData(List<NewEntity> entities) {
        tableModel.setRowCount(0);
        for (NewEntity entity : entities) {
            Object[] row = {
                entity.getId(),
                entity.getName(),
                entity.getDescription() != null ? entity.getDescription() : ""
            };
            tableModel.addRow(row);
        }
        logger.debug("Đã cập nhật table với {} bản ghi", entities.size());
    }
    
    public void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    
    public void showWarningMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
    }
    
    public int showConfirmDialog(String message, String title) {
        return JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION);
    }
    
    public void setTableSelectionListener(Runnable onSelectionChanged) {
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && onSelectionChanged != null) {
                onSelectionChanged.run();
            }
        });
    }
    
    /**
     * Style button với padding và border màu đen
     */
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
}
```

## 🎮 Bước 5: Tạo Controller

```java
// src/main/java/com/mycompany/device/controller/NewEntityController.java
package com.mycompany.device.controller;

import com.mycompany.device.model.NewEntity;
import com.mycompany.device.service.NewEntityService;
import com.mycompany.device.service.impl.NewEntityServiceImpl;
import com.mycompany.device.ui.swing.panel.NewEntityPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Controller quản lý NewEntity - Controller trong MVC pattern
 * @author Your Name
 */
public class NewEntityController {
    
    private static final Logger logger = LoggerFactory.getLogger(NewEntityController.class);
    private final NewEntityPanel view;
    private final NewEntityService service;
    
    public NewEntityController(NewEntityPanel view) {
        this.view = view;
        this.service = new NewEntityServiceImpl();
        
        initializeEventHandlers();
        loadAllEntities();
        
        logger.info("NewEntityController đã được khởi tạo thành công");
    }
    
    private void initializeEventHandlers() {
        // Add button
        view.setAddButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddEntity();
            }
        });
        
        // Update button
        view.setUpdateButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleUpdateEntity();
            }
        });
        
        // Delete button
        view.setDeleteButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDeleteEntity();
            }
        });
        
        // Search button
        view.setSearchButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSearchEntities();
            }
        });
        
        // Refresh button
        view.setRefreshButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAllEntities();
            }
        });
        
        // Clear button
        view.setClearButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.clearForm();
            }
        });
        
        // Table selection
        view.setTableSelectionListener(new Runnable() {
            @Override
            public void run() {
                handleTableSelection();
            }
        });
        
        logger.debug("Đã thiết lập tất cả event handlers");
    }
    
    private void handleAddEntity() {
        logger.info("Xử lý thêm entity mới");
        
        String id = view.getId();
        String name = view.getName();
        String description = view.getDescription();
        
        // Validation
        if (id.isEmpty()) {
            view.showWarningMessage("Vui lòng nhập ID!");
            return;
        }
        
        if (name.isEmpty()) {
            view.showWarningMessage("Vui lòng nhập tên!");
            return;
        }
        
        NewEntity entity = new NewEntity(id, name, description.isEmpty() ? null : description);
        
        if (service.createEntity(entity)) {
            view.showSuccessMessage("Thêm entity thành công!");
            view.clearForm();
            loadAllEntities();
        } else {
            view.showErrorMessage("Không thể thêm entity. Có thể ID đã tồn tại!");
        }
    }
    
    private void handleUpdateEntity() {
        logger.info("Xử lý cập nhật entity");
        
        String id = view.getId();
        String name = view.getName();
        String description = view.getDescription();
        
        // Validation
        if (id.isEmpty()) {
            view.showWarningMessage("Vui lòng chọn entity để cập nhật!");
            return;
        }
        
        if (name.isEmpty()) {
            view.showWarningMessage("Vui lòng nhập tên!");
            return;
        }
        
        NewEntity entity = new NewEntity(id, name, description.isEmpty() ? null : description);
        
        if (service.updateEntity(entity)) {
            view.showSuccessMessage("Cập nhật entity thành công!");
            view.clearForm();
            loadAllEntities();
        } else {
            view.showErrorMessage("Không thể cập nhật entity!");
        }
    }
    
    private void handleDeleteEntity() {
        logger.info("Xử lý xóa entity");
        
        NewEntity selectedEntity = view.getSelectedEntity();
        if (selectedEntity == null) {
            view.showWarningMessage("Vui lòng chọn entity để xóa!");
            return;
        }
        
        int confirm = view.showConfirmDialog(
            "Bạn có chắc chắn muốn xóa entity '" + selectedEntity.getName() + "'?",
            "Xác nhận xóa"
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (service.deleteEntity(selectedEntity.getId())) {
                view.showSuccessMessage("Xóa entity thành công!");
                view.clearForm();
                loadAllEntities();
            } else {
                view.showErrorMessage("Không thể xóa entity!");
            }
        }
    }
    
    private void handleSearchEntities() {
        logger.info("Xử lý tìm kiếm entities");
        
        String searchText = view.getSearchText();
        List<NewEntity> entities = service.searchEntitiesByName(searchText);
        view.updateTableData(entities);
        
        logger.info("Tìm kiếm với từ khóa '{}': {} kết quả", searchText, entities.size());
    }
    
    private void handleTableSelection() {
        NewEntity selectedEntity = view.getSelectedEntity();
        if (selectedEntity != null) {
            view.populateForm(selectedEntity);
            logger.debug("Đã chọn entity: {}", selectedEntity.getId());
        }
    }
    
    private void loadAllEntities() {
        logger.info("Load tất cả entities");
        
        List<NewEntity> entities = service.getAllEntities();
        view.updateTableData(entities);
        view.clearSearch();
        
        logger.info("Đã load {} entities", entities.size());
    }
}
```

## 🧭 Bước 6: Đăng ký vào ScreenRouter

### 6.1 Thêm constant cho screen mới

```java
// Trong file ScreenRouter.java, thêm constant
public static final String NEW_ENTITY = "NEW_ENTITY";
```

### 6.2 Thêm handle cho screen mới

```java
// Trong method onScreenChanged của ScreenRouter.java
case NEW_ENTITY:
    handleNewEntityScreenActivated();
    break;

// Thêm method handle
private void handleNewEntityScreenActivated() {
    logger.debug("NewEntity screen đã được kích hoạt");
}
```

## 📋 Bước 7: Đăng ký vào MainFrame Menu

### 7.1 Thêm vào Menu

```java
// Trong MainFrame.java, thêm menu item
private void createMenuBar() {
    JMenuBar menuBar = new JMenuBar();
    
    // Menu Quản lý
    JMenu quanLyMenu = new JMenu("Quản lý");
    
    // Existing menu items...
    
    // New Entity menu item
    JMenuItem newEntityMenuItem = new JMenuItem("Quản lý NewEntity");
    newEntityMenuItem.addActionListener(e -> {
        screenRouter.navigateToScreen(ScreenRouter.NEW_ENTITY);
        logger.info("Menu clicked: Quản lý NewEntity");
    });
    quanLyMenu.add(newEntityMenuItem);
    
    // Add to menu bar...
}
```

### 7.2 Đăng ký Panel vào ScreenRouter

```java
// Trong MainFrame.java, trong method initializeComponents()

// Tạo NewEntity Panel và Controller
NewEntityPanel newEntityPanel = new NewEntityPanel();
NewEntityController newEntityController = new NewEntityController(newEntityPanel);

// Đăng ký với ScreenRouter
screenRouter.registerScreen(ScreenRouter.NEW_ENTITY, newEntityPanel);
```

## 🗄️ Bước 8: Tạo Database Table

```sql
-- Tạo table trong MySQL
CREATE TABLE IF NOT EXISTS new_entity (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Index cho tìm kiếm
CREATE INDEX idx_new_entity_name ON new_entity(name);

-- Dữ liệu mẫu
INSERT INTO new_entity (id, name, description) VALUES 
('NE001', 'Entity 1', 'Mô tả cho Entity 1'),
('NE002', 'Entity 2', 'Mô tả cho Entity 2'),
('NE003', 'Entity 3', 'Mô tả cho Entity 3');
```

## ✅ Bước 9: Test và Compile

### 9.1 Compile project

```bash
mvn clean compile
```

### 9.2 Chạy ứng dụng

```bash
mvn exec:java
```

### 9.3 Test các chức năng

1. **Đăng nhập** vào ứng dụng
2. **Navigate** đến menu "Quản lý NewEntity"
3. **Test CRUD operations**:
   - Thêm entity mới
   - Cập nhật entity
   - Xóa entity
   - Tìm kiếm entity

## 📚 Checklist hoàn thành

- [ ] ✅ Tạo Model class
- [ ] ✅ Tạo DAO interface và implementation
- [ ] ✅ Tạo Service interface và implementation
- [ ] ✅ Tạo Panel (View)
- [ ] ✅ Tạo Controller
- [ ] ✅ Thêm constant vào ScreenRouter
- [ ] ✅ Thêm menu item vào MainFrame
- [ ] ✅ Đăng ký Panel với ScreenRouter
- [ ] ✅ Tạo database table
- [ ] ✅ Test toàn bộ chức năng

## 💡 Tips và Best Practices

### 1. **Naming Convention**
- Model: `NewEntity.java`
- DAO: `NewEntityDAO.java`, `NewEntityDAOMySQLImpl.java`
- Service: `NewEntityService.java`, `NewEntityServiceImpl.java`
- Panel: `NewEntityPanel.java`
- Controller: `NewEntityController.java`

### 2. **Logging**
- Sử dụng SLF4J Logger cho tất cả class
- Log ở level INFO cho operations quan trọng
- Log ở level DEBUG cho chi tiết technical
- Log ở level ERROR cho exceptions

### 3. **Error Handling**
- Luôn validate input từ user
- Catch và handle SQL exceptions
- Hiển thị message rõ ràng cho user

### 4. **Database**
- Sử dụng PreparedStatement để tránh SQL injection
- Đóng connections properly với try-with-resources
- Thêm index cho các column thường tìm kiếm

### 5. **UI/UX**
- Sử dụng method `styleButton()` để consistent styling
- Thêm tooltips cho buttons nếu cần
- Validate form trước khi submit
- Clear form sau operations thành công

## 🔧 Troubleshooting

### 1. **Compilation Errors**
```bash
# Clean và compile lại
mvn clean compile
```

### 2. **Database Connection Issues**
- Kiểm tra `config.env` file
- Verify MySQL server đang chạy
- Check username/password

### 3. **UI Issues**
- Restart ứng dụng
- Check console logs
- Verify Panel được đăng ký đúng với ScreenRouter

### 4. **Navigation Issues**
- Check constant name trong ScreenRouter
- Verify menu item action listener
- Check Panel registration

## 📖 Tài liệu tham khảo

- [Java Swing Documentation](https://docs.oracle.com/javase/tutorial/uiswing/)
- [SLF4J Logging](http://www.slf4j.org/manual.html)
- [MySQL JDBC Driver](https://dev.mysql.com/doc/connector-j/8.0/en/)
- [Maven Build Tool](https://maven.apache.org/guides/)

---

**Chúc bạn coding vui vẻ!** 🚀
